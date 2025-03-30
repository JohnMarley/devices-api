package com.example.devices.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SerializationUtil {

    private SerializationUtil() {
    }

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static <T> T deepCopy(T object, Class<T> clazz) {
        try {
            String json = objectMapper.writeValueAsString(object);
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error during deep copy", e);
        }
    }

    public static String serializeObject(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserializeJsonString(String jsonPayload, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonPayload, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
