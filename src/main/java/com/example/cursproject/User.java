package com.example.cursproject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class User {
    private String name; // Имя пользователя
    private String email; // Адрес электронной почты
    private boolean isAdmin; // Флаг администратора (0 - обычный пользователь, 1 - администратор)

    // Конструктор для создания пользователя
    public User(String name, String email, boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin;

        new File("usersProfiles/" + name);
        // Создание файла профиля пользователя
        String filename = "profile" + name + ".txt";
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Имя:" + name + "\n");
            writer.write("Почта:" + email + "\n");
            if (isAdmin) {
                writer.write("Статус:Админ\n");
            } else {
                writer.write("Статус:Юзер\n");
            }
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }

        // Логирование создания профиля

    }

    // Геттеры

    public String getName() {
        return name;
    }
    public String getEmail() {return email;}

    public boolean isAdmin() {
        if (isAdmin) return true;
        return false;
    }

    // Методы обработки строк
    // Метод для форматирования информации о пользователе
    public String getFormattedInfo() {
        return String.format("Пользователь #%d: Имя: %s, Email: %s, Статус: %s",
                name, email, isAdmin ? "Администратор" : "Юзер");
    }
}