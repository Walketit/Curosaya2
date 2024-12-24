package com.example.cursproject;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class User {
    private static User instance;
    private String name; // Имя пользователя
    private String email; // Адрес электронной почты
    protected boolean isAdmin; // Флаг администратора (0 - обычный пользователь, 1 - администратор)

    List<Account> accounts;

    public User() {
        this.name = "";
        this.email = "";
        this.isAdmin = false;
        this.accounts = new ArrayList<>();
    }
    // Конструктор для создания пользователя
    public User(String name, String email, boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin;
        this.accounts = new ArrayList<>();

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

    }

    // Геттеры

    public String getName() {
        return name;
    }
    public String getEmail() {return email;}

    public List<Account> getAccounts() {return accounts;}

    public boolean isAdmin() {
        if (isAdmin) return true;
        return false;
    }

    public void setName(String name) {this.name = name;};
    public void setEmail(String email) {this.email = email;}
    public void setAccounts() {
        try (BufferedReader br = new BufferedReader(new FileReader(name + "Счета.txt"))) {
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
                    accounts.add(account);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Методы обработки строк
    // Метод для форматирования информации о пользователе
    public String getFormattedInfo() {
        return String.format("Пользователь #%d: Имя: %s, Email: %s, Статус: %s",
                name, email, isAdmin ? "Администратор" : "Юзер");
    }

    public void saveAccountsToFile(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Account account : accounts) {
                String line = String.format("%s:%.2f:%s", account.getName(), account.getBalance(), account.getCurrency());
                bw.write(line.replace(',', '.')); // Заменяем запятую на точку
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public Account getAccount(String name) {
        for (Account account: accounts) {
            if (account.getName().equals(name)) return account;
        }
        return null;
    }
}