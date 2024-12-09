package com.example.cursproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class LoginRegisterController {

    private static final String USER_DATABASE_FILE = "users.txt";

    @FXML
    private Button ToggleButton;
    @FXML
    private Text LogRegTitle;
    @FXML
    private Text TglText;
    @FXML
    private TextField LoginLogField;
    @FXML
    private TextField LogPassField;
    @FXML
    private TextField RegLogField;
    @FXML
    private TextField RegPassField;
    @FXML
    private TextField PswrdConfirmField;
    @FXML
    private Button RegisterBtn;
    @FXML
    private Button LoginLogBtn;

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
        }
        statusMessage.setText(""); // Очистить сообщение
    }

    // Метод для входа
    @FXML
    private void handleLogin(ActionEvent event) {
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
            statusMessage.setText("Регистрация успешна! Теперь вы можете войти.");
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
