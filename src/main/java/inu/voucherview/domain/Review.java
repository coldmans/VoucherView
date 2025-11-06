package inu.voucherview.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "reviewId")
public class Review {
    private Long reviewId; // PK
    private Long userId; // FK (User)
    private Long courseId; // FK (Course)

    private String content; // 리뷰 본문
    private Integer rating; // 별점 (1~5)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void changeContent(String content){
        this.content = content;
    }

    public void changeRating(Integer rating){
        this.rating = rating;
    }

}
