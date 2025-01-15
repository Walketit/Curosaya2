package com.example.cursproject;

import javafx.scene.shape.Path;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class Logs {
    private User user;
    private String name; // Имя файла логов
    private Time time; // Объект для работы с датой и временем

    private LinkedList<String> content;

    // Конструктор по умолчанию
    public Logs() {
        this.user = null;
        this.name = "";
        this.time = new Time();
        this.content = new LinkedList<>();
    }

    // Метод для обновления файла логов
    public void logfileUpdate(String log) {
        time.currentTime(); // Получаем текущее время
        String mes = log + time.getFullDate(); // Формируем сообщение с датой и временем
        content.addFirst(mes);
        try (FileWriter writer = new FileWriter(name)) {
            for (String line: content
                 ) {
                writer.write(line + "\n");
            }
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
                content.addFirst(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        this.user = user;
        this.name = user.getUserDir() + "/" + user.getName() + "Логи.txt";
        readLogs();
    }

    // Геттер для объекта Time
    public Time getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public void exportLogsToFile(LocalDate fromDate, LocalDate toDate, String outputFile) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedReader reader = new BufferedReader(new FileReader(name));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    String logData = parts[1].trim(); // "2025-01-15 22:21:01"
                    LocalDateTime logDateTime = LocalDateTime.parse(logData, dateTimeFormatter);
                    LocalDate logDate = logDateTime.toLocalDate();

                    // Проверяем, входит ли дата в диапазон
                    if (!logDate.isBefore(fromDate) && !logDate.isAfter(toDate)) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }

            System.out.println("Логи успешно экспортированы в файл: " + outputFile);

        } catch (IOException e) {
            System.err.println("Ошибка при обработке файлов: " + e.getMessage());
        }
    }
}