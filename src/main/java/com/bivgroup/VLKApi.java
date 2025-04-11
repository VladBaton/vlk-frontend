package com.bivgroup;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class VLKApi {

    private final String API_URL = "http://localhost:8085/vlk/hello";

    public String callHelloEndpoint() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            return reader.lines().collect(Collectors.joining());
        }
    }

    public String callGetNotificationsByUserId() throws IOException {

        // Создаем объект URL
        URL obj = new URL("http://localhost:8085/vlk/notifications/getByClient");

        // Открываем соединение
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        // Устанавливаем метод запроса на POST
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json"); // Установите нужный Content-Type
        connection.setRequestProperty("Authorization", "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJ2bGstand0Iiwic3ViIjoidmxrLWp3dCIsImdyb3VwcyI6WyJhZG1pbiJdLCJleHAiOjE3NDQzOTkzNDM0MDEsImlhdCI6MTc0NDM5OTMzOSwianRpIjoiN2M3OWM2OTctYjY3Ni00ZGNjLTkxYmUtNWY4ZWJiNmFkYTE3In0.eHUfoyecVEGapMV78ogxAWK9XcP1eKD_tXweZHo_XhmxDfSWxOyI4Ngizc_IAQ2VmK1WZMR30C_rlW7dmbkzMzMLMWcRMzFnevGUhoDU9iX7kGKH0Hhl_FdZ6ADuxn0NzQYkJHxytdbB4JcDWen3VsKqAuecJcI-b8j-leJEsThVndYyCQWQZ8PdktLJNHOan4KfGdKhOh0YF3e9GJ0nioQEOOyrpaoTkATt-UPT79pQC_f_N7-Ya9QJezDhSUQG1yW_Ud9OXrqjoEGdA_xKrN56xcodX6T--EY2wtkdslu8HSLPRpSbIvZMhDXeZHc1jFoTAOrBlUY_l0hpzD1smQ"); // Добавляем JWT-токен

        // Включаем режим отправки данных
        connection.setDoOutput(true);

        // Записываем данные в тело запроса
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.writeBytes("{\"insurerId\": 1}");
            wr.flush(); // Заканчиваем запись
        }

        // Получаем код ответа
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // Читаем ответ сервера
        StringBuilder response = new StringBuilder();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
            }
        } else {
            return null;
        }

        // Закрываем соединение
        connection.disconnect();

        return response.toString();
    }
}