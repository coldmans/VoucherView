package inu.voucherview.domain;

import lombok.*;
import org.locationtech.jts.geom.Point;


import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "userId")
public class User {
    private Long userId;
    private String email;
    private String password;
    private String nickname;

    // 사용자의 위치를 저장하는 변수
    private Point location;
    private LocalDateTime createdAt;

    public void changeEmail(String email){
        this.email = email;
    }

    public void changePassword(String password){
        this.password = password;
    }

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

}
