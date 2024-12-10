package com.example.cursproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    private Button AccountsBtn;

    @FXML
    private Button CurrencyChangeBtn;

    @FXML
    private Button ExitBtn;

    @FXML
    private Button GoalsBtn;

    @FXML
    private Button HistoryBtn;

    @FXML
    private Button NotesBtn;

    @FXML
    private Button ProfileBtn;

    @FXML
    private Button SettingsBtn;

    @FXML
    private Text userName;

    @FXML
    private Text userEmail;

    @FXML
    private Text userStatus;

    @FXML
    private Text Name;

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

        Name.setText("Добро пожаловать, " + currentUser.getName() + "!");

        userName.setText(currentUser.getName());
        userEmail.setText(currentUser.getEmail());

    }


}