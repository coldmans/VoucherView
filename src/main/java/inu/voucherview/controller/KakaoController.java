package inu.voucherview.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import inu.voucherview.domain.User;
import inu.voucherview.service.KakaoService;
import inu.voucherview.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @Value("${kakao.client.id}")
    String clientId;

    @Value("${kakao.redirect.uri}")
    String redirectUri;

    @Value("${kakao.client.secret}")
    String clientSecret;

    @GetMapping("/oauth/kakao")
    public String getAuthKakao(){
        System.out.println(1);
        return "https://kauth.kakao.com/oauth/authorize?"
                +"client_id=" + clientId
                +"&redirect_uri=" + redirectUri
                +"&response_type=code";
    }

    @GetMapping("/login/oauth2/code/kakao")
    public void kakaoCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        System.out.println("카카오 콜백 받음, code: " + code);
        // 카카오에서 받은 code를 프론트엔드로 리다이렉트
        response.sendRedirect("https://webfront-750840454420.asia-northeast3.run.app/login?code=" + code);
    }

    @PostMapping("/oauth/kakao/token")
    public String postAuthKakao(@RequestBody MultiValueMap<String, String> params){
        System.out.println(2);
        String code = params.getFirst("code");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        MultiValueMap<String, String> httpBody = new LinkedMultiValueMap<>();
        httpBody.add("grant_type", "authorization_code");
        httpBody.add("client_id", clientId);
        httpBody.add("redirect_uri", redirectUri);
        httpBody.add("code", code);
        httpBody.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(httpBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token", httpEntity, String.class
        );

        System.out.println(response.getBody());
        return response.getBody();
    }

    @PostMapping("/oauth/kakao/access")
    public Map<String, String> postGetUserInfo(@RequestBody String accessToken) throws JsonProcessingException{
        System.out.println(3);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://kapi.kakao.com/v2/user/me", httpEntity, String.class
        );
        System.out.println(response.getBody());

        // 카카오 사용자 정보로 회원가입/로그인 후 사용자 정보 조회
        User user = kakaoService.loginOrSignup(response.getBody());

        // JWT 발급
        String jwt = jwtUtil.generateToken(user.getUserId());

        // JWT와 닉네임 함께 반환
        return Map.of(
                "token", jwt,
                "nickname", user.getNickname() != null ? user.getNickname() : ""
        );
    }
}
