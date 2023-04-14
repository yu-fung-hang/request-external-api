package com.singfung.demo.service;

import com.singfung.demo.model.dto.SecurityQuestion;
import com.singfung.demo.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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

    public UserDTO getUserById(Integer id) {
        try {
            String url = "http://localhost:9100/api/user/" + id;
            UserDTO user = restTemplate.getForObject(url, UserDTO.class);
            return user;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
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

    public String testCognito() {
        try {
            String url = "http://192.168.2.247:8081/api/hello";
            String token = "eyJraWQiOiJNWFhWMHhkNVBIbzhxQ2VSaitDWnJ1d1huMUtoVHZDZFpYR0JBVGpFN1wvUT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiI5YWVhNTg3Mi04OTZlLTQ0ZmUtYWI5ZS05Yjg0YmVlMThhODkiLCJjb2duaXRvOmdyb3VwcyI6WyJBRE1JTiJdLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuY2EtY2VudHJhbC0xLmFtYXpvbmF3cy5jb21cL2NhLWNlbnRyYWwtMV9LN3B1cXJIejgiLCJjbGllbnRfaWQiOiI3bG1rOTN1b25hbWhhbTM0Nzdta2Nybm5pNSIsIm9yaWdpbl9qdGkiOiI5M2ZjMGQ0OS00ZGM2LTQ1NDItYTNlZC05NDAwMzlhZjY1YzEiLCJldmVudF9pZCI6ImFmZTRkMjU0LTA1NzQtNDc5Ny04MWVlLTU1NmQ5NDBlZmVmZSIsInRva2VuX3VzZSI6ImFjY2VzcyIsInNjb3BlIjoiYXdzLmNvZ25pdG8uc2lnbmluLnVzZXIuYWRtaW4iLCJhdXRoX3RpbWUiOjE2ODE0ODUxNzUsImV4cCI6MTY4MTU3MTU3NSwiaWF0IjoxNjgxNDg1MTc1LCJqdGkiOiIwMjZlZjkwOS0yOWI4LTRlMDQtYjQ3YS1iZjQyZmY1YzE3MjIiLCJ1c2VybmFtZSI6Im90dGVhdHByZWthQG91dGxvb2suY29tIn0.w5mmxRQYtGocmrJM_vv-mnlxZeObHs19XYzcTaaE36IHXbQoufnuZI_rJpxGsv0exLI_gUFumSlMjRX3VhG2h-_6tC-r1p8WfoyD5oyG2J9ygD5K6jJz557OjpAHnem4GmFn7tRvZ_IuhJp2DMWnwViZOjGVtnBniQVYFOCCZOK4GmpOa74ql90Me301ZifQs5qMH5iVRpoTSRRxYdre-y15z9jhH5KvhfPMuZB1fiRX940fS_YY8k-1gSmmDbc-jECFnsnipaWSK5rSqf-QU31IHWtRZhmqXcJgSf4pPFqHANZ8CX2w0x-k71DYKCw9J6BolhAeRhkiRktBEKH1rw";

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(token);
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(httpHeaders);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            return response.getBody();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
    }
}
