package com.example.demo.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomResponse {
    public static ResponseEntity<Object> generate(HttpStatus status, String message, Object dataObject) {
        Map<String, Object> response = new HashMap<>();

        response.put("status", status);
        response.put("message", message);
        response.put("data", dataObject);

        return new ResponseEntity<Object>(response, status);
    }

    public static ResponseEntity<Object> generate(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();

        response.put("status", status);
        response.put("message", message);

        return new ResponseEntity<Object>(response, status);
    }
}
