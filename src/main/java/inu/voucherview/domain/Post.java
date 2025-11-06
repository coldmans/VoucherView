package inu.voucherview.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Post {
    private Long postId; // PK
    private Long userId; // FK (User)

    private String category; // 카테고리 (예: "Q&A", "FREE")
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
