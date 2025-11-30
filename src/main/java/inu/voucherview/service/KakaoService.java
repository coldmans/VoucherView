package inu.voucherview.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import inu.voucherview.domain.User;
import inu.voucherview.mapper.UserMapper;
import inu.voucherview.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    /**
     * 카카오 사용자 정보로 회원가입/로그인 처리 후 JWT 토큰 반환
     */
    @Transactional
    public String loginOrSignup(String kakaoUserInfoJson) throws JsonProcessingException {
        // 카카오 사용자 정보 파싱
        JsonNode userInfo = objectMapper.readTree(kakaoUserInfoJson);
        Long kakaoId = userInfo.get("id").asLong();

        // 닉네임과 이메일 추출 (없을 수 있음)
        String nickname = null;
        String email = null;

        if (userInfo.has("kakao_account")) {
            JsonNode kakaoAccount = userInfo.get("kakao_account");

            // 이메일 추출
            if (kakaoAccount.has("email")) {
                email = kakaoAccount.get("email").asText();
            }

            // 닉네임 추출
            if (kakaoAccount.has("profile")) {
                JsonNode profile = kakaoAccount.get("profile");
                if (profile.has("nickname")) {
                    nickname = profile.get("nickname").asText();
                }
            }
        }

        // 기존 유저 조회
        User user = userMapper.findByUserId(kakaoId);

        // 없으면 회원가입
        if (user == null) {
            user = User.builder()
                    .userId(kakaoId)
                    .nickname(nickname)
                    .email(email)
                    .createdAt(LocalDateTime.now())
                    .build();
            userMapper.insertUser(user);
        }

        // JWT 토큰 발급
        return jwtUtil.generateToken(kakaoId);
    }
}