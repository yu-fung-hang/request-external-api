package com.singfung.demo.service;

import com.singfung.demo.model.dto.SecurityQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import mjson.Json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sing-fung
 * @since 2/18/2023
 */

@Service
public class DemoService {
    private RestTemplate restTemplate;

    @Autowired
    public DemoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<SecurityQuestion> getAllSecurityQuestions() {
        String url = "http://localhost:9000/api/security-questions";
        SecurityQuestion[] result = restTemplate.getForObject(url, SecurityQuestion[].class);
        return Arrays.asList(result);
    }

    private String getLogInResponse(String usernameOrEmail, String password) {
        try {
            String url = "http://localhost:9000/api/login";

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            Map<String, String> map = new HashMap<>();
            map.put("usernameOrEmail", usernameOrEmail);
            map.put("password", password);
            HttpEntity<Map<String, String>> loginRequest = new HttpEntity<>(map, httpHeaders);

            return restTemplate.postForObject(url, loginRequest, String.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
    }

    private String extractTokenFromResponse(String loginResponse) {
        Json json = Json.read(loginResponse);
        Json token = json.at("token");
        return token.asString();
    }

    private String getToken(String usernameOrEmail, String password) {
        String loginResponse = getLogInResponse(usernameOrEmail, password);
        return extractTokenFromResponse(loginResponse);
    }

    public String verifyToken(String usernameOrEmail, String password) {
        try {
            String url = "http://localhost:9000/api/verify-token";

            String token = getToken(usernameOrEmail, password);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(token);
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(map, httpHeaders);

            String response = restTemplate.postForObject(url, request, String.class);

            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
    }
}
