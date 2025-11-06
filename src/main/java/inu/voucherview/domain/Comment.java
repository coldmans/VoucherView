package inu.voucherview.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Comment {
    private Long commentId; // PK
    private Long postId; // FK (Post)
    private Long userId; // FK (User)

    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
