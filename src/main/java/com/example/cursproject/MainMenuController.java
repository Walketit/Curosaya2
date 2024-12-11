package com.example.cursproject;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EventListener;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static java.lang.System.exit;

public class MainMenuController {

    private static final String USER_PROFILE_DIRECTORY = "user_profiles/";
    private Stage stage;
    private Scene scene;
    private Parent root;

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
    private ChoiceBox<?> CurrencyFirstChoice;

    @FXML
    private ChoiceBox<?> CurrencySecondChoice;

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

        String name = null;
        String email = null;
        boolean isAdmin = false;


        try (BufferedReader reader = new BufferedReader(new FileReader(profileFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Имя:")) {
                    name = line.substring(4).trim();
                } else if (line.startsWith("Почта:")) {
                    email = line.substring(6).trim();
                } else if (line.startsWith("Статус:")) {
                    isAdmin = line.substring(7).trim().equals("Админ");
                }
            }
        }

        if (name == null || email == null) {
            throw new IOException("Файл профиля содержит неполные данные.");
        }

        User currentUser = new User(name, email, isAdmin);

        User.getInstance(currentUser);

        MainGreetings.setText("Добро пожаловать, " + currentUser.getName() + "!");

        UserName.setText(currentUser.getName());
        UserEmail.setText(currentUser.getEmail());

        loadAccountsFromFile(name + "Счета.txt");
    }

    // Метод для парсинга файла и добавления счетов в список
    public void loadAccountsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Разделение строки на компоненты
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String name = parts[0];
                    double balance;
                    try {
                        balance = Double.parseDouble(parts[1]);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid balance format for account: " + name);
                        continue;
                    }
                    String currency = parts[2];

                    // Создание объекта Account и добавление в список
                    Account account = new Account(name, balance, currency);
                    User.getInstance().accounts.add(account);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        for (Account account : User.getInstance().accounts) {
            AccountChoose.getItems().add(account.getName());
        }
    }

    // Метод для обновления баланса
    @FXML
    private void updateBalance(ActionEvent event) {
        String selectedAccountName = AccountChoose.getValue(); // Получаем выбранный счет
        String amountText = AccountTranscationField.getText(); // Получаем введенную сумму

        if (selectedAccountName == null) {
            showError("Please select an account.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText); // Преобразуем сумму в число
            if (amount <= 0) {
                showError("Amount must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Invalid amount format.");
            return;
        }

        // Находим счет по имени
        Account selectedAccount = User.getInstance().accounts.stream()
                .filter(account -> account.getName().equals(selectedAccountName))
                .findFirst()
                .orElse(null);

        if (selectedAccount == null) {
            showError("Selected account not found.");
            return;
        }

        // Обновляем баланс
        selectedAccount.deposit(amount);

        // Обновляем данные на экране (например, в ChoiceBox или Label)
        showSuccess("Transaction successful. New balance: " + selectedAccount.getBalance());

        // Сохраняем изменения обратно в файл
        saveAccountsToFile(User.getInstance().getName() + "Счета.txt");
    }

    // Метод для сохранения обновленных данных счетов в файл
    public void saveAccountsToFile(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Account account : User.getInstance().accounts) {
                String line = String.format("%s:%.2f:%s", account.getName(), account.getBalance(), account.getCurrency());
                bw.write(line.replace(',', '.')); // Заменяем запятую на точку
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void openProfile(ActionEvent e) {
        MainMenuScene.setVisible(false);
        GoalsScene.setVisible(false);
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(true);
    }


    public void openMain(ActionEvent event) {
        MainMenuScene.setVisible(true);
        GoalsScene.setVisible(false);
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(false);
    }

    public void openAccounts(ActionEvent event) {
        MainMenuScene.setVisible(false);
        GoalsScene.setVisible(false);
        AccountScene.setVisible(true);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(false);
    }

    public void openGoals(ActionEvent event) {
        MainMenuScene.setVisible(false);
        GoalsScene.setVisible(true);
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(false);
        ProfileScene.setVisible(false);
    }

    public void openCurrencyChange(ActionEvent event) {
        MainMenuScene.setVisible(false);
        GoalsScene.setVisible(false);
        AccountScene.setVisible(false);
        CurrencyChangeScene.setVisible(true);
        ProfileScene.setVisible(false);
    }

    public void openNotes(ActionEvent event) {
    }

    public void openLogs(ActionEvent event) {
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
}