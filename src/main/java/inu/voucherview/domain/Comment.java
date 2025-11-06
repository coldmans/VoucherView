package inu.voucherview.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(of = "commentId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    private Long commentId; // PK
    private Long postId; // FK (Post)
    private Long userId; // FK (User)

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void changeContent(String content){
        this.content = content;
    }

}
