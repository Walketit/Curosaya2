package com.example.cursproject;

public class Note {
    private String title; // Заголовок заметки
    private String content; // Содержание заметки
    private String category; // Категория заметки

    // Конструктор по умолчанию
    public Note() {
        this.title = "";
        this.content = "";
        this.category = "";
    }

    // Конструктор с параметрами
    public Note(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }

    // Метод для вывода заметки
    public void printNote() {
        System.out.println("Заметка: " + title);
        System.out.println(content);
    }

    // Геттер для заголовка заметки
    public String getTitle() {
        return title;
    }

    // Геттер для содержания заметки
    public String getDescription() {
        return content;
    }

    // Геттер для категории заметки
    public String getCategory() {
        return category;
    }

    // Сеттер для заголовка заметки
    public void setTitle(String title) {
        this.title = title;
    }

    // Сеттер для содержания заметки
    public void setDescription(String description) {
        this.content = description;
    }

    // Сеттер для категории заметки
    public void setCategory(String category) {
        this.category = category;
    }
}