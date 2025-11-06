package inu.voucherview.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Review {
    private Long reviewId; // PK
    private Long userId; // FK (User)
    private Long courseID; // FK (Facility)

    private String content; // 리뷰 본문
    private Integer rating; // 별점 (1~5)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
