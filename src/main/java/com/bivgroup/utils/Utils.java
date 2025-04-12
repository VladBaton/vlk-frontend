package com.bivgroup.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    protected static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Сериализуем объект в JSON
     */
    public static String serialize(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Десериализуем объект из JSON
     */
    public static <T> T deserializeResponse(String jsonResponse, Class<T> responseType) throws JsonProcessingException {
        return objectMapper.readValue(jsonResponse, responseType);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
