package com.example.cursproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.Objects;

public class LoginRegisterController {
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
        }
        else {
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
    }
}