package com.example.cursproject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time {
    private String hour; // Часы
    private String min; // Минуты
    private String sec; // Секунды
    private String day; // День
    private String month; // Месяц
    private String year; // Год
    private String fullDate; // Полная дата и время

    public void Time() {
        Calendar calendar = Calendar.getInstance(); // Получаем текущую дату и время
        SimpleDateFormat dateFormat = new SimpleDateFormat("|yyyy-MM-dd HH:mm:ss|");
        fullDate = dateFormat.format(calendar.getTime()); // Форматируем полную дату и время

        // Форматируем отдельные компоненты даты и времени
        dateFormat = new SimpleDateFormat("dd");
        day = dateFormat.format(calendar.getTime());

        dateFormat = new SimpleDateFormat("MM");
        month = dateFormat.format(calendar.getTime());

        dateFormat = new SimpleDateFormat("yyyy");
        year = dateFormat.format(calendar.getTime());

        dateFormat = new SimpleDateFormat("HH");
        hour = dateFormat.format(calendar.getTime());

        dateFormat = new SimpleDateFormat("mm");
        min = dateFormat.format(calendar.getTime());

        dateFormat = new SimpleDateFormat("ss");
        sec = dateFormat.format(calendar.getTime());
    }

    // Метод для получения текущего времени и даты
    public void currentTime() {
        Calendar calendar = Calendar.getInstance(); // Получаем текущую дату и время
        SimpleDateFormat dateFormat = new SimpleDateFormat("|yyyy-MM-dd HH:mm:ss|");
        fullDate = dateFormat.format(calendar.getTime()); // Форматируем полную дату и время

        // Форматируем отдельные компоненты даты и времени
        dateFormat = new SimpleDateFormat("dd");
        day = dateFormat.format(calendar.getTime());

        dateFormat = new SimpleDateFormat("MM");
        month = dateFormat.format(calendar.getTime());

        dateFormat = new SimpleDateFormat("yyyy");
        year = dateFormat.format(calendar.getTime());

        dateFormat = new SimpleDateFormat("HH");
        hour = dateFormat.format(calendar.getTime());

        dateFormat = new SimpleDateFormat("mm");
        min = dateFormat.format(calendar.getTime());

        dateFormat = new SimpleDateFormat("ss");
        sec = dateFormat.format(calendar.getTime());
    }

    // Метод для получения полной даты и времени в виде строки
    public String getFullDate() {
        currentTime(); // Получаем текущую дату и время
        return fullDate; // Возвращаем полную дату и время
    }
}