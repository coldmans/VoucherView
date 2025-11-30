package inu.voucherview.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class KakaoController {
    @Value("${kakao.client.id}")
    String clientId;

    @Value("${kakao.redirect.uri}")
    String redirectUri;

    @Value("${kakao.client.secret}")
    String clientSecret;

    @GetMapping("/kakao")
    public String getAuthKakao(){
        return "https://kauth.kakao.com/oauth/authorize?"
                +"client_id=" + clientId
                +"&redirect_uri=" + redirectUri
                +"&response_type=code";
    }
}
