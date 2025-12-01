package inu.voucherview.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "reviewId")
public class Review {
    private Long reviewId; // PK
    private Long userId; // FK (User)
    private Long facilityId; // FK (Facility)

    private String content; // 리뷰 본문
    private Integer rating; // 별점 (1~5)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void changeContent(String content){
        this.content = content;
    }

    public void changeRating(Integer rating){
        if(rating == null || rating < 1 || rating > 5){ //Check를 통해서도 방어를 할 예정 이중 방어
            throw new IllegalArgumentException("별점은 1점에서 5점 사이여야 동작합니다");
        }
        this.rating = rating;
    }

}
