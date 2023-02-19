package com.singfung.demo.service;

import com.singfung.demo.model.dto.SecurityQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

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
}
