package com.example.cursproject;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainMenuController {

    private static final String USER_PROFILE_DIRECTORY = "user_profiles/";
    private Stage stage;
    private Scene scene;
    private Parent root;
    private User user = new User();
    CurrencyChange change = new CurrencyChange();

    Logs log = new Logs();
    Time time = new Time();


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text AccountBalance;

    @FXML
    private ChoiceBox<String> AccountChoose;

    @FXML
    private Button AccountDeleteBtn;

    @FXML
    private Text AccountName;

    @FXML
    private AnchorPane AccountScene;

    @FXML
    private TextField AccountTranscationField;

    @FXML
    private Button AccountsBtn;

    @FXML
    private TextField CurrencyAmountIn;

    @FXML
    private Text CurrencyAmountOut;

    @FXML
    private Button CurrencyChangeBtn;

    @FXML
    private AnchorPane CurrencyChangeScene;

    @FXML
    private ChoiceBox<String> CurrencyFirstChoice;

    @FXML
    private ChoiceBox<String> CurrencySecondChoice;

    @FXML
    private Button ExitBtn;

    @FXML
    private ChoiceBox<?> GoalChoice;

    @FXML
    private Text GoalCurrentStatus;

    @FXML
    private Button GoalDeleteBtn;

    @FXML
    private Text GoalName;

    @FXML
    private Button GoalRedoBtn;

    @FXML
    private Button GoalsBtn;

    @FXML
    private AnchorPane GoalsScene;

    @FXML
    private Button LogsBtn;

    @FXML
    private Button MainBtn;

    @FXML
    private Text MainGreetings;

    @FXML
    private AnchorPane MainMenu;

    @FXML
    private AnchorPane MainMenuScene;

    @FXML
    private Button MakeTransactionBtn;

    @FXML
    private Button NotesBtn;

    @FXML
    private Button ProfileBtn;

    @FXML
    private ImageView ProfileImage;

    @FXML
    private AnchorPane ProfileScene;

    @FXML
    private Button RedoProfile;

    @FXML
    private Button SettingsBtn;

    @FXML
    private Text UserEmail;

    @FXML
    private Text UserName;

    @FXML
    private VBox AccountContainer;

    @FXML
    private Button CurrencyRefresh;

    @FXML
    private HBox GoalsContainer;

    @FXML
    private ScrollPane GoalsScrollPane;

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
    private void Exit(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginRegister.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Создание объекта User из файла профиля
    public void setUser(String username) throws IOException {


        String filePath = "profile" + username + ".txt";
        File profileFile = new File(filePath);

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
                }
            }
        }

        user.loadAccountsFromFile();
        user.loadGoalsFromFile();
        user.loadNotesFromFile();
        refreshCurrency();
        refreshLogs();

        log.setUser(user);

        File file = new File(log.getName());
        if (!file.exists()) {
            log.logfileCreate();
        }

        MainGreetings.setText("Добро пожаловать, " + user.getName() + "!");
        UserName.setText(user.getName());

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
            Label nameLabel = new Label("Название: " + account.getName());
            Label balanceLabel = new Label("Баланс: " + account.getBalance());
            Label currencyLabel = new Label("Валюта: " + account.getCurrency());

            // Кнопка для изменения баланса
            Button editBalanceButton = new Button("Изменить баланс");
            editBalanceButton.setOnAction(event -> openAccountWindow(account));

            // Кнопка для удаления счета
            Button deleteAccountButton = new Button("Удалить");
            deleteAccountButton.setOnAction(event -> user.getAccounts().remove(account));

            editBalanceButton.getStyleClass().add("button");
            deleteAccountButton.getStyleClass().add("button");

            // Добавляем элементы в HBox
            accountBox.getChildren().addAll(nameLabel, balanceLabel, currencyLabel, editBalanceButton, deleteAccountButton);

            // Добавляем HBox в основной контейнер (VBox)
            AccountContainer.getChildren().add(accountBox);
        }
    }

    private void openAccountWindow(Account account) {
        // Создаем окно для ввода суммы и действия
        Stage accountWindow = new Stage();
        accountWindow.initModality(Modality.APPLICATION_MODAL);  // делает окно модальным
        accountWindow.setTitle("Операции с счетом #" + account.getName());

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
                log.logfileUpdate("Пополнение;"+account.getName()+";"+amount+";"+reasonField.getText());
                showAlert("Успех", "Сумма успешно добавлена на счет #" + account.getName());
                accountWindow.close();
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
                log.logfileUpdate("Списание;"+account.getName()+";"+amount+";"+reasonField.getText());
                showAlert("Успех", "Сумма успешно снята с счета #" + account.getName());
                accountWindow.close();
                refreshAccounts();
            } catch (NumberFormatException ex) {
                showAlert("Ошибка", "Введите правильную сумму для снятия.");
            }
        });

        // Добавляем элементы в окно
        accountLayout.getChildren().addAll(amountField,reasonField, depositButton, withdrawButton);

        // Сцена для окна с операциями
        Scene accountScene = new Scene(accountLayout, 450, 200);
        accountWindow.setScene(accountScene);
        accountWindow.show();
    }

    @FXML
    public void refreshGoals() {
        GoalsContainer.getChildren().clear();
        for (Goal goal : user.getGoals()) {
            Pane pane = new Pane();
            double centerX = 100; // Координата центра X
            double centerY = 100; // Координата центра Y
            double radius = 80;   // Радиус круга

            // Создаем окружность (фон)
            Circle circle = new Circle(centerX, centerY, radius);
            circle.setFill(Color.LIGHTGRAY);

            // Создаем дугу (прогресс)
            Arc progressArc = new Arc(centerX, centerY, radius, radius, 90, 0);
            progressArc.setType(ArcType.ROUND);
            progressArc.setFill(Color.GREEN);

            // Рассчитываем угол в зависимости от прогресса
            double progressPercentage = goal.getCurrentBalance() / goal.getTargetAmount();
            progressArc.setLength(-progressPercentage * 360);

            // Добавляем текстовое описание цели
            Text goalText = new Text(centerX - 70, centerY + radius + 20, goal.getTitle() + ": " + (int) (progressPercentage * 100) + "%");
            goalText.setFill(Color.BLACK);

            // Добавляем элементы на панель
            pane.getChildren().addAll(circle, progressArc, goalText);
            pane.setPrefSize(500, 500); // Устанавливаем размер панели для размещения внутри HBox
            GoalsContainer.getChildren().add(pane);
        }
        GoalsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS); // Полоса прокрутки всегда
        GoalsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);  // Не показывать вертикальную полосу
        GoalsScrollPane.setPannable(true); // Возможность прокрутки мышью

    }

    @FXML
    public void refreshNotes() {
        noteContainer.getChildren().clear();
        for (Note note : user.getNotes()) {
            // Создаем HBox для каждой заметки
            HBox noteBlock = new HBox(10);

            // Текст для заголовка
            Text titleText = new Text(note.getTitle());
            // Текст для содержания
            Text contentText = new Text(note.getDescription());

            // Кнопка удаления
            Button deleteButton = new Button("Удалить");
            deleteButton.setOnAction(event -> {
                user.getNotes().remove(note); // Удаляем блок
            });

            // Добавляем обработчик клика для заметки
            noteBlock.setOnMouseClicked(event -> handleNoteClick(note));

            // Добавляем текст и кнопку в HBox
            noteBlock.getChildren().addAll(titleText, contentText, deleteButton);

            // Добавляем HBox в контейнер
            noteContainer.getChildren().add(noteBlock);
        }
    }

    // Обработчик клика на заметку
    private void handleNoteClick(Note note) {
        // Например, выводим содержание заметки в консоль
        System.out.println("Вы кликнули на заметку: " + note.getTitle());
        System.out.println("Содержание: " + note.getDescription());

        // Или можно открыть диалоговое окно для отображения заметки
        // Например, можно использовать Alert или создать собственное окно
    }

    public void refreshCurrency() throws IOException {
        CurrencyFirstChoice.getItems().clear();
        CurrencySecondChoice.getItems().clear();
        try (BufferedReader reader = new BufferedReader(new FileReader("CurrencyCodes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                CurrencyFirstChoice.getItems().add(line);
                CurrencySecondChoice.getItems().add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для сохранения обновленных данных счетов в файл
    public void saveInfo(ActionEvent e) {
        user.saveAccountsToFile();
        user.saveGoalsToFile();
        user.saveNotesToFile();
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

                    // Текст с операцией
                    Text operationText = new Text("Операция: " + operation);
                    // Текст с датой и временем
                    Text dateText = new Text("Дата и время: " + dateStr);

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

    public void openProfile(ActionEvent e) {
        MainMenuScene.setVisible(false);
        GoalsScene.setVisible(false);
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(true);
        notesScene.setVisible(false);
        logsScene.setVisible(false);
    }

    public void openMain(ActionEvent event) {
        MainMenuScene.setVisible(true);
        GoalsScene.setVisible(false);
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(false);
        notesScene.setVisible(false);
        logsScene.setVisible(false);
    }

    public void openAccounts(ActionEvent event) {
        MainMenuScene.setVisible(false);
        GoalsScene.setVisible(false);
        refreshAccounts();
        AccountScene.setVisible(true);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(false);
        notesScene.setVisible(false);
        logsScene.setVisible(false);
    }

    public void openGoals(ActionEvent event) {
        MainMenuScene.setVisible(false);
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
        MainMenuScene.setVisible(false);
        GoalsScene.setVisible(false);
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(true);
        ProfileScene.setVisible(false);
        notesScene.setVisible(false);
        logsScene.setVisible(false);
    }

    public void openNotes(ActionEvent event) {
        refreshNotes();
        MainMenuScene.setVisible(false);
        GoalsScene.setVisible(false);
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(false);
        notesScene.setVisible(true);
        logsScene.setVisible(false);
    }

    public void openLogs(ActionEvent event) {
        MainMenuScene.setVisible(false);
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
        System.err.println("Error: " + message);
        // Здесь можно добавить вывод сообщения в Label или Alert
    }

    // Метод для отображения успешных операций
    private void showSuccess(String message) {
        System.out.println("Success: " + message);
        // Здесь можно добавить вывод сообщения в Label или Alert
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}