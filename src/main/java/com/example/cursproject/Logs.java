package com.example.cursproject;

import java.io.*;

public class Logs {
    private User user;
    private String name; // Имя файла логов
    private Time time; // Объект для работы с датой и временем

    // Конструктор по умолчанию
    public Logs() {
        this.user = null;
        this.name = "";
        this.time = new Time();
    }

    // Метод для создания файла логов
    public void logfileCreate() {
        try {
            File file = new File(name);
            if (!file.createNewFile()) {
                System.out.println("Ошибка создания файла!");
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для обновления файла логов
    public void logfileUpdate(String log) {
        time.currentTime(); // Получаем текущее время
        String mes = log + time.getFullDate(); // Формируем сообщение с датой и временем
        try (FileWriter writer = new FileWriter(name, true)) {
            writer.write(mes + "\n"); // Добавляем сообщение в файл
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для чтения логов
    public void readLogs() {
        try (BufferedReader reader = new BufferedReader(new FileReader(name))) {
            System.out.println("История операций:");
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|"); // Разделяем строку на части
                if (parts.length == 2) {
                    String operation = parts[0]; // Название операции
                    String dateStr = parts[1]; // Дата и время операции

                    time.parseDate(dateStr); // Парсим дату и время

                    System.out.println("Название операции: " + operation);
                    System.out.println("Дата: " + time.getFullDate());
                    System.out.println("-----------------------------");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        this.user = user;
        this.name = user.getName() + "logs.txt";
    }

    // Геттер для объекта Time
    public Time getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}