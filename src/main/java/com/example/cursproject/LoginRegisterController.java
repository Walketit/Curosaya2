package com.example.cursproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LoginRegisterController {

    private static final String USER_DATABASE_FILE = "users.txt";

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private RadioButton AdminBtn;

    @FXML
    private TextField EmailRegField;

    @FXML
    private TextField LogPassField;

    @FXML
    private Text LogRegTitle;

    @FXML
    private Button LoginLogBtn;

    @FXML
    private TextField LoginLogField;

    @FXML
    private TextField PswrdConfirmField;

    @FXML
    private TextField RegLogField;

    @FXML
    private TextField RegPassField;

    @FXML
    private Button RegisterBtn;

    @FXML
    private Text TglText;

    @FXML
    private Button ToggleButton;

    @FXML
    private RadioButton UserBtn;

    @FXML
    private Text statusMessage; // Для вывода сообщений об ошибках или успехе

    // Метод для обработки переключения между окнами
    @FXML
    private void click(ActionEvent event) {
        if (ToggleButton.getText().equals("Регистрация")) {
            ToggleButton.setText("Вход");
            LogRegTitle.setText("Регистрация");
            TglText.setText("Есть профиль?");

            LoginLogField.setVisible(false);
            LogPassField.setVisible(false);
            LoginLogBtn.setVisible(false);

            RegLogField.setVisible(true);
            RegisterBtn.setVisible(true);
            RegPassField.setVisible(true);
            PswrdConfirmField.setVisible(true);
            EmailRegField.setVisible(true);
            UserBtn.setVisible(true);
            AdminBtn.setVisible(true);
        } else {
            ToggleButton.setText("Регистрация");
            LogRegTitle.setText("Авторизация");
            TglText.setText("Нет профиля?");

            LoginLogField.setVisible(true);
            LogPassField.setVisible(true);
            LoginLogBtn.setVisible(true);

            RegLogField.setVisible(false);
            RegisterBtn.setVisible(false);
            RegPassField.setVisible(false);
            PswrdConfirmField.setVisible(false);
            EmailRegField.setVisible(false);
            UserBtn.setVisible(false);
            AdminBtn.setVisible(false);
        }
        statusMessage.setText(""); // Очистить сообщение
    }

    // Метод для входа
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String login = LoginLogField.getText().trim();
        String password = LogPassField.getText().trim();
        statusMessage.setVisible(true);

        if (login.isEmpty() || password.isEmpty()) {
            statusMessage.setText("Логин и пароль не могут быть пустыми.");
            return;
        }

        Map<String, String> users = loadUsers();
        if (users.containsKey(login) && users.get(login).equals(password)) {
            statusMessage.setText("Успешный вход!");
            // Загрузка второго окна
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
            Parent root = loader.load();
            // Получение контроллера второго окна
            MainMenuController mainMenuController = loader.getController();
            mainMenuController.setUser(login); // Передача логина
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            statusMessage.setText("Неверный логин или пароль.");
        }
    }

    // Метод для регистрации
    @FXML
    private void handleRegister(ActionEvent event) {
        String login = RegLogField.getText().trim();
        String password = RegPassField.getText().trim();
        String confirmPassword = PswrdConfirmField.getText().trim();
        String email = EmailRegField.getText().trim();
        Boolean isAdmin = false;
        if (AdminBtn.isSelected()) isAdmin = true;
        statusMessage.setVisible(true);

        if (login.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusMessage.setText("Пожалуйста, заполните все поля.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            statusMessage.setText("Пароли не совпадают.");
            return;
        }

        Map<String, String> users = loadUsers();
        if (users.containsKey(login)) {
            statusMessage.setText("Пользователь с таким логином уже существует.");
            return;
        }

        if (saveUser(login, password)) {
            click(event);
            statusMessage.setText("Регистрация успешна! Теперь вы можете войти.");
            User newUser = new User(login, email, isAdmin);
        } else {
            statusMessage.setText("Ошибка при сохранении пользователя.");
        }
    }

    // Метод для загрузки пользователей из файла
    private Map<String, String> loadUsers() {
        Map<String, String> users = new HashMap<>();
        if (!Files.exists(Paths.get(USER_DATABASE_FILE))) {
            return users; // Если файл не существует, возвращаем пустую коллекцию
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATABASE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Метод для сохранения пользователя
    private boolean saveUser(String login, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DATABASE_FILE, true))) {
            writer.write(login + ":" + password);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
