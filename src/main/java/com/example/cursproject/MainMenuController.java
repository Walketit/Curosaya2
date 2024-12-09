package com.example.cursproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

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

    private void Exit(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginRegister.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}