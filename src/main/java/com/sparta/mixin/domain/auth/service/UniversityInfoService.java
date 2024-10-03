package com.sparta.mixin.domain.auth.service;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UniversityInfoService {

    private final RestTemplate restTemplate;

    @Value("${OPEN_API_BASE_URL}")
    private String BASE_URL;

    @Value("${OPEN_API_SERVICE_KEY}")
    private String SERVICE_KEY;

    public String getUniversities(int page, int perPage) {
        try {
            String encodedServiceKey = URLEncoder.encode(SERVICE_KEY, StandardCharsets.UTF_8.toString());
            String urlString = String.format("%s?page=%d&perPage=%d&serviceKey=%s",
                BASE_URL, page, perPage, encodedServiceKey);

            URI uri = new URI(urlString);
            System.out.println("requestURL: " + uri.toString());

            String response = restTemplate.getForObject(uri, String.class);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}