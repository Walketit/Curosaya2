package com.example.cursproject;

public class Transaction {
    private int id; // Уникальный идентификатор транзакции
    private Account account; // Счет, с которым связана транзакция
    private int type; // Тип транзакции (0 - пополнение, 1 - списание)
    private String name; // Название транзакции
    private double amount; // Сумма транзакции

    // Конструктор по умолчанию
    public Transaction() {
        this.id = 0;
        this.account = null;
        this.type = 0;
        this.name = "";
        this.amount = 0.0;
    }

    // Конструктор с параметрами
    public Transaction(int id, Account account) {
        setTransaction(id, account);
    }

    // Метод для установки параметров транзакции
    public void setTransaction(int id, Account account) {
        this.id = id;
        this.account = account;
        this.type = 0; // По умолчанию тип транзакции - пополнение
        this.name = "";
        this.amount = 0.0;
    }

    // Метод для изменения значений транзакции и выполнения операции
    public void changeValue(String name, int type, double value) {
        this.name = name;
        this.type = type;
        this.amount = value;

        // В зависимости от типа транзакции выполняем соответствующую операцию
        if (type == 0) {
            account.deposit(amount); // Пополнение счета
            System.out.printf("Пополнение на %.2f, на счёт %s\n", amount, account.getName());
        } else if (type == 1) {
            account.withdraw(amount); // Списание со счета
            System.out.printf("Списание %.2f, со счёта %s\n", amount, account.getName());
        }
    }
}