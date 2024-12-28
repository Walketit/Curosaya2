package com.example.cursproject;

public class Goal {
    private String name; // Название цели
    private String description; // Описание цели
    private double targetAmount; // Целевая сумма
    private double currentAmount; // Текущая сумма

    // Конструктор по умолчанию
    public Goal() {
        this.name = "";
        this.description = "";
        this.targetAmount = 0;
        this.currentAmount = 0;
    }

    // Конструктор с параметрами
    public Goal(String name, double targetAmount, double currentAmount, String description) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.description = description;
    }

    // Метод для вывода информации о цели
    public void printGoal() {
        System.out.println("Цель: " + name);
        System.out.printf("%.0f/%.0f\n", currentAmount, targetAmount);
        System.out.println("Описание: " + description);
    }

    // Метод для добавления суммы к текущей сумме
    public void addToBalance(double amount) {
        currentAmount += amount;
    }

    // Метод для проверки, достигнута ли цель
    public boolean isAchieved() {
        return currentAmount >= targetAmount;
    }

    // Геттер для названия цели
    public String getTitle() {
        return name;
    }

    // Геттер для описания цели
    public String getDescription() {
        return description;
    }

    // Геттер для текущей суммы
    public double getCurrentBalance() {
        return currentAmount;
    }

    // Геттер для целевой суммы
    public double getTargetAmount() {
        return targetAmount;
    }

    // Сеттер для названия цели
    public void setTitle(String title) {
        this.name = title;
    }

    // Сеттер для описания цели
    public void setDescription(String description) {
        this.description = description;
    }

    // Сеттер для текущей суммы
    public void setCurrentBalance(double currentBalance) {
        this.currentAmount = currentBalance;
    }

    // Сеттер для целевой суммы
    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }
}