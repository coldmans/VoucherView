package inu.voucherview.dto;

import inu.voucherview.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReviewDto {
    private Long reviewId; // PK
    private Long userId; // FK (User)
    private Long facilityId; // FK (Facility)

    private String content; // 리뷰 본문
    private Integer rating; // 별점 (1~5)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ReviewDto(Review review) {
        this.reviewId = review.getReviewId();
        this.userId = review.getUserId();
        this.facilityId = review.getFacilityId();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
