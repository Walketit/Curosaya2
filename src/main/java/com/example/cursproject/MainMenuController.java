package com.example.cursproject;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenuController {

    private static final String USER_PROFILE_DIRECTORY = "user_profiles/";
    private Stage stage;
    private Scene scene;
    private Parent root;
    private User user = new User();


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
    private VBox AccountContainer;

    @FXML
    private Button CurrencyRefresh;

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

        String name;
        String email;
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

        user.setAccounts();

        MainGreetings.setText("Добро пожаловать, " + user.getName() + "!");
        UserName.setText(user.getName());
    }

    @FXML
    public void refreshAccounts() {
        // Очищаем текущий список блоков
        AccountContainer.getChildren().clear();

        // Создаём блоки для каждого счета
        for (Account account : user.getAccounts()) {
            // Создаём контейнер (HBox) для счета
            VBox accountBox = new VBox();
            accountBox.getStyleClass().add("account-box");
            accountBox.setSpacing(5);

            // Создаём элементы для отображения информации о счетеБИ
            Label nameLabel = new Label("Название: " + account.getName());
            Label balanceLabel = new Label("Баланс: " + account.getBalance());
            Label currencyLabel = new Label("Валюта: " + account.getCurrency());

            // Кнопка для изменения баланса
            Button editBalanceButton = new Button("Изменить баланс");
            editBalanceButton.setOnAction(event -> user.getAccount(account.getName()));

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


    // Метод для сохранения обновленных данных счетов в файл
    public void saveInfo(ActionEvent e) {
        user.saveAccountsToFile();
    }
    public void refreshCurrency(ActionEvent e) throws IOException {
        CurrencyChange change = new CurrencyChange();
        change.setCurrencyChange();
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
        refreshAccounts();
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