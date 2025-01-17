package com.example.cursproject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CurrencyChange {
    private Map<String, String> currencyNames; // Название валюты

    // Конструктор по умолчанию
    CurrencyChange() {
        currencyNames = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("CurrencyCodes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    currencyNames.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для установки курса обмена валюты
    public String setCurrencyChange(String base_name, String target_name, double amount) throws IOException {
        Dotenv dotenv = Dotenv.load();
        // Setting URL
        String url_str = "https://v6.exchangerate-api.com/v6/"+dotenv.get("API_KEY") +"/pair/" + currencyNames.get(base_name) + "/" + currencyNames.get(target_name);

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
            DecimalFormat df = new DecimalFormat("#.00");
            return df.format((amount * jsonobj.get("conversion_rate").getAsDouble()));
        }
        else {
            System.out.println("Error");
            return "Ошибка! Проверьте подключение к интернету.";
        }
    }

    public String getCode(String value) {
        return currencyNames.get(value);
    }
}