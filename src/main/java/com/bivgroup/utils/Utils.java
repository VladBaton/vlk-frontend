package com.bivgroup.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
}
