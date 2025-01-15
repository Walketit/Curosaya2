package com.example.cursproject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainMenuController {

    private Stage stage;
    private Scene scene;
    private User user = new User();
    CurrencyChange change = new CurrencyChange();

    Logs log = new Logs();
    Time time = new Time();

    @FXML
    private AnchorPane AccountScene;
    @FXML
    private TextField CurrencyAmountIn;
    @FXML
    private Text CurrencyAmountOut;
    @FXML
    private AnchorPane CurrencyChangeScene;
    @FXML
    private ChoiceBox<String> CurrencyFirstChoice;
    @FXML
    private ChoiceBox<String> CurrencySecondChoice;
    @FXML
    private AnchorPane GoalsScene;
    @FXML
    private AnchorPane ProfileScene;
    @FXML
    private Text UserEmail;
    @FXML
    private Text UserName;
    @FXML
    private VBox AccountContainer;
    @FXML
    private HBox GoalsContainer;
    @FXML
    private VBox noteContainer;
    @FXML
    private AnchorPane notesScene;
    @FXML
    private Text timeLabel;
    @FXML
    private AnchorPane logsScene;
    @FXML
    private VBox logsContainer;
    @FXML
    private TextField newAccountName;
    @FXML
    private TextField newAccountBalance;
    @FXML
    private ChoiceBox<String> newAccountCurrency;
    @FXML
    private TextField newGoalName;
    @FXML
    private TextField newGoalTarget;
    @FXML
    private TextArea newGoalDescription;
    @FXML
    private TextField newNoteName;
    @FXML
    private TextArea newNoteDescription;
    @FXML
    private TextField newNoteCategory;
    @FXML
    private Text userAge;
    @FXML
    private Text userOccupation;

    @FXML
    private void Exit(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginRegister.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Создание объекта User из файла профиля
    public void setUser(String username) throws IOException {
        user.setUserDir(username + "dir");

        File profileFile = new File(user.getUserDir() + "/profile" + username + ".txt");

        if (!profileFile.exists()) {
            throw new IOException("Файл профиля для пользователя " + username + " не найден.");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(profileFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Имя:")) {
                    user.setName(line.substring(4).trim());
                } else if (line.startsWith("Почта:")) {
                    user.setEmail(line.substring(6).trim());
                } else if (line.startsWith("Возраст:")) {
                    user.setAge(line.substring(8).trim());
                } else if (line.startsWith("Занятость:")) {
                    user.setOccupation(line.substring(10).trim());
                }
            }
        }
        log.setUser(user);
        user.loadAccountsFromFile();
        user.loadGoalsFromFile();
        user.loadNotesFromFile();

        refreshCurrency();
        refreshLogs();
        refreshProfile();

        UserName.setText(user.getName());
        UserEmail.setText(user.getEmail());

        // Обновление времени каждую секунду с помощью Timeline
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateTime())
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Бесконечное обновление
        timeline.play(); // Запуск таймлайна для обновления времени
        // Инициализируем отображение времени сразу при запуске
        updateTime();
    }

    @FXML
    public void refreshAccounts() {
        // Очищаем текущий список блоков
        AccountContainer.getChildren().clear();

        // Создаём блоки для каждого счета
        for (Account account : user.getAccounts()) {
            // Создаём контейнер (HBox) для счета
            HBox accountBox = new HBox();
            accountBox.getStyleClass().add("account-box");
            accountBox.setSpacing(5);

            // Создаём элементы для отображения информации о счетеБИ
            Label nameLabel = new Label("Название: " + account.getName() + " |");
            Label balanceLabel = new Label("Баланс: " + account.getBalance() + " " + account.getCurrency() + " |");
            nameLabel.getStyleClass().add("objects-text");
            balanceLabel.getStyleClass().add("objects-text");

            // Кнопка для изменения баланса
            Button editBalanceButton = new Button("Изменить баланс");
            editBalanceButton.setOnAction(event -> openAccountWindow(account));

            // Кнопка для удаления счета
            Button deleteAccountButton = new Button("Удалить");
            deleteAccountButton.getStyleClass().add("delete-button");
            deleteAccountButton.setOnAction(event -> {
                if (showConfirmationDialog() == true) {
                    user.getAccounts().remove(account);
                    user.saveAccountsToFile();
                    refreshAccounts();
                }
            });

            editBalanceButton.getStyleClass().add("button");
            deleteAccountButton.getStyleClass().add("button");

            // Добавляем элементы в HBox
            accountBox.getChildren().addAll(nameLabel, balanceLabel, editBalanceButton, deleteAccountButton);

            // Добавляем HBox в основной контейнер (VBox)
            AccountContainer.getChildren().add(accountBox);
        }
    }

    private void openAccountWindow(Account account) {
        // Создаем окно для ввода суммы и действия
        Stage accountWindow = new Stage();
        accountWindow.initModality(Modality.APPLICATION_MODAL);  // делает окно модальным
        accountWindow.setTitle("Операции с счетом #" + account.getName() + " | " + account.getBalance() + account.getCurrency());

        VBox accountLayout = new VBox(10);
        accountLayout.setPadding(new Insets(20));

        // Поле для ввода суммы
        TextField amountField = new TextField();
        amountField.setPromptText("Введите сумму");
        // Поле для ввода причины транзакции
        TextField reasonField = new TextField();
        reasonField.setPromptText("Введите причину");

        // Кнопка для пополнения счета
        Button depositButton = new Button("Положить на счет");
        depositButton.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
                // Логика пополнения счета
                account.deposit(amount);
                log.logfileUpdate("Пополнение;" + account.getName() + ";" + amount + account.getCurrency() + ";" + reasonField.getText());
                showAlert("Успех", "Сумма успешно добавлена на счет #" + account.getName());
                accountWindow.close();
                user.saveAccountsToFile();
                refreshAccounts();
            } catch (NumberFormatException ex) {
                showAlert("Ошибка", "Введите правильную сумму для пополнения.");
            }
        });

        // Кнопка для снятия со счета
        Button withdrawButton = new Button("Снять со счета");
        withdrawButton.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    throw new NumberFormatException();
                }
                // Логика снятия со счета
                account.withdraw(amount);
                log.logfileUpdate("Списание;" + account.getName() + ";" + amount + account.getCurrency() + ";" + reasonField.getText());
                showAlert("Успех", "Сумма успешно снята с счета #" + account.getName());
                accountWindow.close();
                user.saveAccountsToFile();
                refreshAccounts();
            } catch (NumberFormatException ex) {
                showAlert("Ошибка", "Введите правильную сумму для снятия.");
            }
        });

        // Добавляем элементы в окно
        accountLayout.getChildren().addAll(amountField, reasonField, depositButton, withdrawButton);

        // Сцена для окна с операциями
        Scene accountScene = new Scene(accountLayout, 450, 200);
        accountWindow.setScene(accountScene);
        accountWindow.show();
    }

    public void createNewAccount(ActionEvent event) {
        if (newAccountName.getText().isEmpty() || newAccountCurrency.getValue().isEmpty() ||
                newAccountBalance.getText().isEmpty()) {
            showError("Заполните все поля!");
            return;
        }
        for (Account account : user.getAccounts()) {
            if (account.getName().equals(newAccountName.getText())) {
                showError("Счёт с таким названием уже существует!");
                return;
            }
        }
        user.getAccounts().add(new Account(newAccountName.getText(), Double.parseDouble(newAccountBalance.getText()), newAccountCurrency.getValue()));
        showSuccess("Новый счёт: " + newAccountName.getText() + " успешно создан!");
        user.saveAccountsToFile();
        log.logfileUpdate("Создан Счёт;" + newAccountName.getText() + ";" + newAccountBalance.getText() + ";");
        newAccountName.setText("");
        newAccountBalance.setText("");
        refreshAccounts();
    }

    @FXML
    public void refreshGoals() {
        GoalsContainer.getChildren().clear();
        GoalsContainer.setSpacing(240); // Отступы между элементами

        for (Goal goal : user.getGoals()) {
            Pane pane = new Pane();
            double centerX = 100; // Координата центра X
            double centerY = 100; // Координата центра Y
            double radius = 80;   // Радиус круга

            // Создаем окружность (фон)
            Circle circle = new Circle(centerX, centerY, radius);
            circle.setFill(Color.LIGHTGRAY);
            circle.setStroke(Color.DARKBLUE); // Обводка
            circle.setStrokeWidth(2);

            // Создаем дугу (прогресс)
            Arc progressArc = new Arc(centerX, centerY, radius, radius, 90, 0);
            progressArc.setType(ArcType.ROUND);
            progressArc.setFill(Color.GREEN);

            // Создаем Tooltip для отображения описания цели
            Tooltip goalTooltip = new Tooltip(goal.getDescription());
            Tooltip.install(circle, goalTooltip); // Привязываем Tooltip к окружности
            Tooltip.install(progressArc, goalTooltip); // Привязываем Tooltip к окружности

            // Добавляем обработчик для изменения цвета при наведении
            circle.setOnMouseEntered(e -> progressArc.setFill(Color.LIGHTGREEN)); // Изменение цвета на зеленый
            circle.setOnMouseExited(e -> progressArc.setFill(Color.GREEN)); // Возврат к исходному цвету
            progressArc.setOnMouseEntered(e -> progressArc.setFill(Color.LIGHTGREEN)); // Изменение цвета на зеленый
            progressArc.setOnMouseExited(e -> progressArc.setFill(Color.GREEN)); // Возврат к исходному цвету

            // Рассчитываем угол в зависимости от прогресса
            double progressPercentage = goal.getCurrentBalance() / goal.getTargetAmount();
            progressArc.setLength(-progressPercentage * 360);

            // Добавляем текстовое описание цели
            Text goalText = new Text(centerX - 70, centerY + radius + 20,
                    goal.getTitle() + ": " + (int) (progressPercentage * 100) + "%");
            goalText.setFill(Color.BLACK);

            // Устанавливаем абсолютные координаты для элементов


            // Устанавливаем размер панели
            pane.setPrefSize(250, 250); // Фиксированный размер для каждой панели
            pane.getChildren().addAll(circle, progressArc, goalText);

            // Делаем окружность кликабельной
            circle.setOnMouseClicked(event -> openGoalWindow(goal));
            progressArc.setOnMouseClicked(event -> openGoalWindow(goal));

            // Добавляем панель в контейнер
            GoalsContainer.getChildren().add(pane);
        }
    }

    private void openGoalWindow(Goal goal) {
        // Создаем окно для добавления суммы к цели
        Stage goalWindow = new Stage();
        goalWindow.initModality(Modality.APPLICATION_MODAL); // делает окно модальным
        goalWindow.setTitle("Операции с целью: " + goal.getTitle());

        VBox goalLayout = new VBox(10);
        goalLayout.setPadding(new Insets(20));

        // Метка с текущим балансом и необходимым балансом
        Label balanceLabel = new Label("Текущий баланс: " + goal.getCurrentBalance() +
                " / Необходимый баланс: " + goal.getTargetAmount());
        balanceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Поле для ввода суммы
        TextField amountField = new TextField();
        amountField.setPromptText("Введите сумму для добавления к цели");

        // Кнопка для добавления суммы к цели
        Button addButton = new Button("Добавить к цели");
        addButton.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    throw new NumberFormatException();
                }

                // Добавляем сумму к прогрессу цели
                goal.addToBalance(amount);
                log.logfileUpdate("Добавление к цели;" + goal.getTitle() + ";" + amount + ";");
                showAlert("Успех", "Сумма успешно добавлена к цели: " + goal.getTitle());
                goalWindow.close();
                user.saveGoalsToFile();
                refreshGoals(); // Обновляем отображение целей
            } catch (NumberFormatException ex) {
                showAlert("Ошибка", "Введите правильную сумму для добавления.");
            }
        });

        // Добавляем элементы в окно
        goalLayout.getChildren().addAll(balanceLabel, amountField, addButton);

        // Сцена для окна с операциями
        Scene goalScene = new Scene(goalLayout, 450, 200);
        goalWindow.setScene(goalScene);
        goalWindow.show();
    }

    @FXML
    private void createNewGoal(ActionEvent e) {
        if (newGoalName.getText().isEmpty() || newGoalTarget.getText().isEmpty()) {
            showError("Заполните все поля!");
            return;
        }
        for (Goal goal : user.getGoals()) {
            if (goal.getTitle().equals(newGoalName.getText())) {
                showError("Счёт с таким названием уже существует!");
                return;
            }
        }
        user.getGoals().add(new Goal(newGoalName.getText(), 0.0, Double.parseDouble(newGoalTarget.getText()), newGoalDescription.getText()));
        showSuccess("Новая цель: " + newGoalName.getText() + " успешно создана!");
        user.saveGoalsToFile();
        log.logfileUpdate("Создана цель;" + newGoalName.getText() + ";" + newGoalTarget.getText() + ";");
        newGoalName.setText("");
        newGoalTarget.setText("");
        newGoalDescription.setText("");
        refreshGoals();
    }

    @FXML
    public void refreshNotes() {
        noteContainer.getChildren().clear();
        for (Note note : user.getNotes()) {
            // Создаем HBox для каждой заметки
            HBox noteBlock = new HBox(10);
            noteBlock.getStyleClass().add("account-box");
            noteBlock.setPadding(new Insets(10));


            // Текст для заголовка
            Text titleText = new Text(note.getTitle() + " |");
            titleText.getStyleClass().add("objects-text");
            // Текст для категории
            Text categoryText = new Text(note.getCategory() + " |");
            categoryText.getStyleClass().add("objects-text");
            // Кнопка удаления
            Button deleteButton = new Button("Удалить");
            deleteButton.getStyleClass().add("delete-button");
            deleteButton.setOnAction(event -> {
                if (showConfirmationDialog() == true) {
                    user.getNotes().remove(note); // Удаляем блок
                    refreshNotes();
                    user.saveNotesToFile();
                }
            });

            // Добавляем обработчик клика для заметки
            noteBlock.setOnMouseClicked(event -> openNoteWindow(note));

            // Добавляем текст и кнопку в HBox
            noteBlock.getChildren().addAll(titleText, categoryText, deleteButton);

            // Добавляем HBox в контейнер
            noteContainer.getChildren().add(noteBlock);
        }
    }

    // Обработчик клика на заметку
    private void openNoteWindow(Note note) {
        // Создаем окно для прочтения и/или изменения записки
        Stage noteWindow = new Stage();
        noteWindow.initModality(Modality.APPLICATION_MODAL); // делает окно модальным
        noteWindow.setTitle("Записка: " + note.getTitle());

        VBox noteLayout = new VBox(10);
        noteLayout.setPadding(new Insets(20));

        TextField noteTitle = new TextField();
        noteTitle.setText(note.getTitle());

        TextField noteCategory = new TextField();
        noteCategory.setText(note.getCategory());

        TextArea noteText = new TextArea();
        noteText.setText(note.getDescription());
        noteText.setWrapText(true);

        Button saveChangedNote = new Button();
        saveChangedNote.setText("Сохранить");

        saveChangedNote.setOnAction(e -> {
            if (noteTitle.getText().isEmpty() || noteCategory.getText().isEmpty() ||
                    noteText.getText().isEmpty()) {
                showError("Заполните все поля!");
                return;
            }
            note.setTitle(noteTitle.getText());
            note.setCategory(noteCategory.getText());
            note.setDescription(noteText.getText());
            noteWindow.close();
            showSuccess("Изменения сохранены!");
            refreshNotes();
            user.saveNotesToFile();
        });

        noteLayout.getChildren().addAll(noteTitle, noteCategory, noteText, saveChangedNote);

        // Сцена для окна с операциями
        Scene goalScene = new Scene(noteLayout, 450, 200);
        noteWindow.setScene(goalScene);
        noteWindow.show();
    }

    public void createNewNote(ActionEvent event) {
        if (newNoteName.getText().isEmpty() || newNoteDescription.getText().isEmpty() ||
                newNoteCategory.getText().isEmpty()) {
            showError("Заполните все поля!");
            return;
        }
        user.getNotes().add(new Note(newNoteName.getText(), newNoteDescription.getText(), newNoteCategory.getText()));
        showSuccess("Новая записка: " + newNoteName.getText() + " успешно создана!");
        newNoteName.setText("");
        newNoteDescription.setText("");
        user.saveNotesToFile();
        refreshNotes();
    }

    public void refreshCurrency() throws IOException {
        CurrencyFirstChoice.getItems().clear();
        CurrencySecondChoice.getItems().clear();
        newAccountCurrency.getItems().clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("CurrencyCodes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                CurrencyFirstChoice.getItems().add(line);
                CurrencySecondChoice.getItems().add(line);
                newAccountCurrency.getItems().add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void currencyChange(ActionEvent event) throws IOException {
        CurrencyAmountOut.setText(change.setCurrencyChange((String) CurrencyFirstChoice.getValue(), (String) CurrencySecondChoice.getValue(),
                Double.parseDouble(CurrencyAmountIn.getText())));
    }

    // Метод для обновления времени
    private void updateTime() {
        // Получаем текущее время с помощью объекта Time
        time.currentTime();
        // Отображаем полное время в формате "yyyy-MM-dd HH:mm:ss"
        timeLabel.setText(time.getFullDate());
    }

    private void refreshLogs() {
        logsContainer.getChildren().clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(log.getName()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|"); // Разделяем строку на части
                if (parts.length == 2) {
                    String operation = parts[0]; // Название операции
                    String dateStr = parts[1]; // Дата и время операции

                    // Создаем новый блок для отображения лога
                    // Создаем контейнер для лога
                    VBox logBlock = new VBox(5);
                    logBlock.getStyleClass().add("logs-box");
                    // Текст с операцией
                    Text operationText = new Text("Операция: " + operation);
                    operationText.getStyleClass().add("logs-text");
                    // Текст с датой и временем
                    Text dateText = new Text("Дата и время: " + dateStr);
                    dateText.getStyleClass().add("logs-text");
                    // Добавляем текстовые элементы в VBox
                    logBlock.getChildren().addAll(operationText, dateText);

                    // Добавляем разделитель
                    Text separator = new Text("______________________________________________________");
                    logBlock.getChildren().add(separator);

                    // Добавляем блок в контейнер
                    logsContainer.getChildren().add(logBlock);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openExportWindow(ActionEvent event) {
        Stage exportWindow = new Stage();
        exportWindow.initModality(Modality.APPLICATION_MODAL); // делает окно модальным
        exportWindow.setTitle("Экспорт");

        Label fromDateLabel = new Label("С даты:");
        DatePicker fromDatePicker = new DatePicker();

        Label toDateLabel = new Label("По дату:");
        DatePicker toDatePicker = new DatePicker();

        Button exportButton = new Button("Экспортировать");
        exportButton.setOnAction(e -> {
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();

            if (fromDate != null && toDate != null && !fromDate.isAfter(toDate)) {
                // Открываем FileChooser для выбора места сохранения
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Сохранить логи");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));
                File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());

                // Если пользователь выбрал файл
                if (file != null) {
                    String outputFile = file.getAbsolutePath();
                    log.exportLogsToFile(fromDate, toDate, outputFile);
                    showAlert("Успех", "Логи успешно экспортированы в файл: " + outputFile);
                } else {
                    showAlert("Ошибка", "Укажите корректный диапазон дат.");
                }
            }
        });

        VBox layout = new VBox(10, fromDateLabel, fromDatePicker, toDateLabel, toDatePicker, exportButton);
        layout.setPadding(new Insets(10));

        Scene exportScene = new Scene(layout, 450, 200);
        exportWindow.setScene(exportScene);
        exportWindow.show();
    }

    public void openImportWindow(ActionEvent event) {
        Stage importWindow = new Stage();
        importWindow.initModality(Modality.APPLICATION_MODAL); // делает окно модальным
        importWindow.setTitle("Экспорт");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane(vbox);

        Button selectFileButton = new Button("Выбрать файл");
        selectFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Текстовые файлы", "*.txt"));
            File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

            if (file != null) {
                try {
                    vbox.getChildren().clear();
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            vbox.getChildren().add(new Label(line));
                        }
                    }
                } catch (IOException ex) {
                    showAlert("Ошибка", "Не удалось прочитать файл.");
                }
            }
        });

        VBox layout = new VBox(10, selectFileButton, scrollPane);
        layout.setPadding(new Insets(10));

        Scene importScene = new Scene(layout, 450, 200);
        importWindow.setScene(importScene);
        importWindow.show();
    }

    @FXML
    private void redoProfile(ActionEvent event) {
        Stage profileWindow = new Stage();
        profileWindow.initModality(Modality.APPLICATION_MODAL);  // делает окно модальным
        profileWindow.setTitle("Редактирование профиля");

        VBox profileLayout = new VBox(10);
        profileLayout.setPadding(new Insets(20));

        Label emailLabel = new Label("Почта:");
        TextField email = new TextField(user.getEmail());
        Label ageLabel = new Label("Возраст:");
        TextField age = new TextField(user.getAge());
        Label occupationLabel = new Label("Занятость:");
        TextField occupation = new TextField(user.getOccupation());

        Button saveProfile = new Button();
        saveProfile.setText("Сохранить");
        saveProfile.setOnAction(e -> {
            try {
                user.saveProfile(email.getText(), age.getText(), occupation.getText());
                profileWindow.close();
                refreshProfile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        profileLayout.getChildren().addAll(emailLabel, email, ageLabel, age, occupationLabel, occupation, saveProfile);

        Scene profileScene = new Scene(profileLayout, 450, 250);
        profileWindow.setScene(profileScene);
        profileWindow.show();
    }

    private void refreshProfile() {
        UserName.setText(user.getName());
        UserEmail.setText(user.getEmail());
        userAge.setText(user.getAge());
        userOccupation.setText(user.getOccupation());
    }

    public void deleteProfile(ActionEvent event) {
        if (showConfirmationDialog()) {
            try {
                File file = new File(user.getUserDir());
                for (File subfile : file.listFiles()) {
                    subfile.delete();
                }
                file.delete();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginRegister.fxml"));
                Parent root = loader.load();
                LoginRegisterController loginRegisterController = loader.getController();
                loginRegisterController.deleteUser(user.getName());
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void openProfile(ActionEvent e) {
        GoalsScene.setVisible(false);
        refreshProfile();
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(true);
        notesScene.setVisible(false);
        logsScene.setVisible(false);
    }

    public void openAccounts(ActionEvent event) {
        GoalsScene.setVisible(false);
        refreshAccounts();
        AccountScene.setVisible(true);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(false);
        notesScene.setVisible(false);
        logsScene.setVisible(false);
    }

    public void openGoals(ActionEvent event) {

        refreshGoals();
        GoalsScene.setVisible(true);
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(false);
        notesScene.setVisible(false);
        logsScene.setVisible(false);
    }

    public void openCurrencyChange(ActionEvent event) throws IOException {
        refreshCurrency();
        GoalsScene.setVisible(false);
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(true);
        ProfileScene.setVisible(false);
        notesScene.setVisible(false);
        logsScene.setVisible(false);
    }

    public void openNotes(ActionEvent event) {
        refreshNotes();
        GoalsScene.setVisible(false);
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(false);
        notesScene.setVisible(true);
        logsScene.setVisible(false);
    }

    public void openLogs(ActionEvent event) {
        GoalsScene.setVisible(false);
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(false);
        notesScene.setVisible(false);
        refreshLogs();
        logsScene.setVisible(true);
    }

    // Метод для отображения ошибок
    private void showError(String message) {
        showAlert("Ошибка!", "Error: " + message);
    }

    // Метод для отображения успешных операций
    private void showSuccess(String message) {
        showAlert("Успех!", "Success: " + message);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Метод для создания и отображения окна подтверждения
    public static boolean showConfirmationDialog() {
        // Создаем Alert (диалоговое окно)
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null); // Убираем заголовок, если не нужен
        alert.setContentText("Вы уверены?"); // Устанавливаем сообщение

        // Добавляем кнопки "Да" и "Нет"
        ButtonType yesButton = new ButtonType("Да");
        ButtonType noButton = new ButtonType("Нет");

        // Устанавливаем кнопки
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Показываем окно и ждем ответ пользователя
        alert.showAndWait();

        // Проверяем, какая кнопка была нажата
        if (alert.getResult() == yesButton) {
            return true;  // Пользователь подтвердил действие
        } else {
            return false; // Пользователь отменил действие
        }
    }
}