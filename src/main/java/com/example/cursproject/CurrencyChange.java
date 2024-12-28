package com.example.cursproject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CurrencyChange {
    private String code; // Код валюты
    private String name; // Название валюты
    private float rate; // Курс обмена

    // Конструктор по умолчанию
    CurrencyChange() {
        this.code = "";
        this.name = "";
        this.rate = 0;
    }


    // Метод для установки курса обмена валюты
    public String setCurrencyChange(String base_code, String target_code, double amount) throws IOException {
        // Setting URL
        String url_str = "https://v6.exchangerate-api.com/v6/5fdc25f18976669688828416/pair/" + base_code + "/" + target_code;

        // Making Request
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        // Accessing object
        String req_result = jsonobj.get("result").getAsString();
        if (req_result.equals("success")) {
            return String.valueOf((amount * jsonobj.get("conversion_rate").getAsDouble()));
        }
        else {
            System.out.println("Error");
            return null;
        }
    }

    // Метод для выполнения обмена валюты
    public void change(float amount) {
        System.out.println("Обмен " + name);
        System.out.printf("%.3f = %.3f\n", amount, amount * rate);
    }
}