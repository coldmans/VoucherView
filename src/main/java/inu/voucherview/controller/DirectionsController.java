package inu.voucherview.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directions")
public class DirectionsController {

    @Value("${naver.map.client-id}")
    private String clientId;

    @Value("${naver.map.client-secret}")
    private String clientSecret;

    @GetMapping("/driving")
    public ResponseEntity<String> getDrivingDirections(
            @RequestParam String start,
            @RequestParam String goal,
            @RequestParam(defaultValue = "traoptimal") String option
    ) {
        String url = String.format(
                "https://maps.apigw.ntruss.com/map-direction/v1/driving?start=%s&goal=%s&option=%s",
                start, goal, option
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-ncp-apigw-api-key-id", clientId);
        headers.add("x-ncp-apigw-api-key", clientSecret);

        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                httpEntity,
                String.class
        );

        return response;
    }
}
