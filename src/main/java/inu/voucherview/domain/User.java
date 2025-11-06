package inu.voucherview.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String nickname;

    // 사용자의 위치를 저장하는 변수
    private Point location;
    private LocalDateTime createdAt;


}
