package it.swam.backend.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class BaseTest {

    public static final String USER_ID = "6808c3a783c5ef53d36f9e45";
    public static final String USER_FIRSTNAME = "John";
    public static final String USER_LASTNAME = "Middle";
    public static final String USER_EMAIL = "john@middle.com";

    protected byte[] getBody(Object value) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
