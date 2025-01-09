package com.example.cursproject;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class User {
    private String name; // Имя пользователя
    private String email; // Адрес электронной почты
    protected boolean isAdmin; // Флаг администратора (0 - обычный пользователь, 1 - администратор)
    private List<Account> accounts;

    private List<Goal> goals;

    private List<Note> notes;

    private String userDir;

    public User() {
        this.name = "";
        this.email = "";
        this.isAdmin = false;
        this.accounts = new ArrayList<>();
        this.goals = new ArrayList<>();
        this.notes = new ArrayList<>();
    }

    // Конструктор для создания пользователя
    public User(String name, String email, boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin;
        this.accounts = new ArrayList<>();
        this.userDir = name + "dir";

        File folder = new File(userDir);

        // Проверяем, существует ли папка, если нет — создаём
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                System.out.println("Папка успешно создана!");

                // Создаём объект файла внутри нужной папки
                File userFile = new File(folder, "profile" + name + ".txt");
                File accountsFile = new File(folder, name+"Счета.txt");
                File goalsFile = new File(folder, name + "Цели.txt");
                File notesFile = new File(folder, name + "Заметки.txt");
                File logFile = new File(folder, name + "Логи.txt");

                try (FileWriter writer = new FileWriter(userFile)) {
                    writer.write("Имя:" + name + "\n");
                    writer.write("Почта:" + email + "\n");
                    if (isAdmin) {
                        writer.write("Статус:Админ\n");
                    } else {
                        writer.write("Статус:Юзер\n");
                    }
                    if (accountsFile.createNewFile()) {
                        System.out.println("Файл успешно создан: " + accountsFile.getAbsolutePath());
                    } else {
                        System.out.println("Файл уже существует: " + accountsFile.getAbsolutePath());
                    }
                    if (goalsFile.createNewFile()) {
                        System.out.println("Файл успешно создан: " + goalsFile.getAbsolutePath());
                    } else {
                        System.out.println("Файл уже существует: " + goalsFile.getAbsolutePath());
                    }
                    if (notesFile.createNewFile()) {
                        System.out.println("Файл успешно создан: " + notesFile.getAbsolutePath());
                    } else {
                        System.out.println("Файл уже существует: " + notesFile.getAbsolutePath());
                    }
                    if (logFile.createNewFile()) {
                        System.out.println("Файл успешно создан: " + logFile.getAbsolutePath());
                    } else {
                        System.out.println("Файл уже существует: " + logFile.getAbsolutePath());
                    }
                    System.out.println("Файл успешно сохранён: " + userFile.getAbsolutePath());
                } catch (IOException | RuntimeException e) {
                    e.printStackTrace();
                }

            } else {
                System.out.println("Не удалось создать папку!");
                return;
            }
        }


    }

    // Геттеры

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public boolean isAdmin() {
        if (isAdmin) return true;
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserDir(String name) {
        this.userDir = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void loadAccountsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(userDir + "/" + name + "Счета.txt"))) {
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

    // Класс для чтения целей из файла
    public void loadGoalsFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(userDir + "/" + name +  "Цели.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";"); // Предполагается, что данные разделены ";"
                if (parts.length == 4) {
                    String name = parts[0];
                    double currentAmount = Double.parseDouble(parts[1]);
                    double targetAmount = Double.parseDouble(parts[2]);
                    String description = parts[3];
                    Goal goal = new Goal(name, currentAmount, targetAmount, description);
                    goals.add(goal);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Goal goal : goals
        ) {
            goal.printGoal();
        }
    }

    public void loadNotesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(userDir + "/" + name +  "Заметки.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Предполагаем, что файл разделен на части: заголовок;содержание;категория
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    Note note = new Note(parts[0], parts[1], parts[2]);
                    notes.add(note);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Методы обработки строк
    // Метод для форматирования информации о пользователе
    public String getFormattedInfo() {
        return String.format("Пользователь #%d: Имя: %s, Email: %s, Статус: %s",
                name, email, isAdmin ? "Администратор" : "Юзер");
    }

    public void saveAccountsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userDir + "/" + name + "Счета.txt"))) {
            for (Account account : accounts) {
                String line = String.format("%s:%.2f:%s", account.getName(), account.getBalance(), account.getCurrency());
                bw.write(line.replace(',', '.')); // Заменяем запятую на точку
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void saveGoalsToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userDir + "/" + name +  "Цели.txt"))) {
            for (Goal goal : goals) {
                String line = String.format("%s;%.2f;%.2f;%s", goal.getTitle(), goal.getCurrentBalance(), goal.getTargetAmount(), goal.getDescription());
                bw.write(line.replace(',', '.')); // Заменяем запятую на точку
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void saveNotesToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userDir + "/" + name +  "Заметки.txt"))) {
            for (Note note : notes) {
                String line = String.format("%s;%s;%s", note.getTitle(), note.getDescription(), note.getCategory());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public Account getAccount(String name) {
        for (Account account : accounts) {
            if (account.getName().equals(name)) return account;
        }
        return null;
    }

    public String getUserDir() {
        return userDir;
    }
}