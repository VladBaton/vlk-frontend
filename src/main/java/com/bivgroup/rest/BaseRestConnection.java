package com.bivgroup.rest;

import com.bivgroup.pojo.request.BaseRequest;
import com.bivgroup.utils.Utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class BaseRestConnection {

    /**
     * Отправка POST-запроса
     */
    public <T> T post(BaseRequest request, String url, String token, Class<T> responseType) throws IOException {
        String payload = Utils.serialize(request);
        HttpURLConnection connection = getHttpURLConnection(url, token, payload);

        // Читаем ответ сервера
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return Utils.deserializeResponse(response.toString(), responseType);
    }

    /**
     * Создание подключения
     */
    private static HttpURLConnection getHttpURLConnection(String url, String token, String payload) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection(); // Открываем соединение

        //Настройки запроса
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        if (Objects.nonNull(token)) {
            connection.setRequestProperty("Authorization", "Bearer " + token);
        }

        // Включаем режим отправки данных
        connection.setDoOutput(true);

        // Записываем данные в тело запроса
        try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
            wr.write(payload.getBytes(StandardCharsets.UTF_8)); // Гарантирует корректную передачу JSON
            wr.flush();
        }
        return connection;
    }
}
