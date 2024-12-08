package com.example.cursproject;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginRegister extends Application {
    private static final String USER_DATABASE_FILE = "users.txt";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginRegister.fxml")));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        primaryStage.setTitle("Добро пожаловать в СУФ!");
        primaryStage.show();


    }


}