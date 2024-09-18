package com.sparta.mixin.domain.auth.service;

import com.sparta.mixin.domain.auth.dto.MajorResponseDto;
import com.sparta.mixin.domain.auth.dto.UniversityResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${api.url}")
    private String apiUrl;

    @Value("${university.api.key}")
    private String universityApiKey;

    @Value("${major.api.key}")
    private String majorApiKey;

    private final RestTemplate restTemplate;

    public List<UniversityResponseDto> searchUniversities(String universityName) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("searchSchulNm", universityName);

        String url = buildUrl("SCHOOL", universityApiKey, queryParams);

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> dataSearch = (Map<String, Object>) response.get("dataSearch");

        return ((List<Map<String, Object>>) dataSearch.get("content"))
                .stream()
                .map(item -> new UniversityResponseDto(
                        (String) item.get("schoolName"),
                        (String) item.get("adres")
                ))
                .toList();
    }

    public List<MajorResponseDto> searchMajor(String major) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("searchTitle", major);

        String url = buildUrl("MAJOR", majorApiKey, queryParams);

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> dataSearch = (Map<String, Object>) response.get("dataSearch");

        return ((List<Map<String, Object>>) dataSearch.get("content"))
                .stream()
                .map(item -> new MajorResponseDto(
                        (String) item.get("lClass"),
                        (String) item.get("mClass")
                ))
                .toList();
    }

    private String buildUrl(String serviceCode, String apiKey, Map<String, String> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("apiKey", apiKey)
                .queryParam("svcType", "api")
                .queryParam("svcCode", serviceCode)
                .queryParam("contentType", "json")
                .queryParam("gubun", "univ_list");

        queryParams.forEach(builder::queryParam);

        return builder.build().toUriString();
    }
}

