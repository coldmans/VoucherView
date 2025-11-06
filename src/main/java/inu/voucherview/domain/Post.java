package inu.voucherview.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(of = "postId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    private Long postId; // PK
    private Long userId; // FK (User)

    private PostCategory category; // 카테고리 (예: "Q&A", "FREE")
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void changeCategory(PostCategory category){
        this.category = category;
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }
}
