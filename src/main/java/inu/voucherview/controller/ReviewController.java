package inu.voucherview.controller;

import inu.voucherview.annotation.LoginUser;
import inu.voucherview.domain.Review;
import inu.voucherview.dto.ReviewDto;
import inu.voucherview.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 작성
     * POST /api/reviews
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewDto createReview(
            @LoginUser Long userId,
            @RequestBody Map<String, Object> request
    ) {
        Long facilityId = Long.valueOf(request.get("facilityId").toString());
        String content = request.get("content").toString();
        Integer rating = Integer.valueOf(request.get("rating").toString());

        Review review = reviewService.createReview(userId, facilityId, content, rating);
        return new ReviewDto(review);
    }

    /**
     * 특정 시설의 리뷰 조회
     * GET /api/reviews/facility/{facilityId}?page=1&limit=10
     */
    @GetMapping("/facility/{facilityId}")
    public List<ReviewDto> getReviewsByFacility(
            @PathVariable Long facilityId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        return reviewService.getReviewsByFacility(facilityId, page, limit)
                .stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 특정 유저의 리뷰 조회
     * GET /api/reviews/user/{userId}?page=1&limit=10
     */
    @GetMapping("/user/{userId}")
    public List<ReviewDto> getReviewsByUser(
            @PathVariable Long userId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        return reviewService.getReviewsByUser(userId, page, limit)
                .stream()
                .map(ReviewDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 리뷰 수정
     * PUT /api/reviews/{reviewId}
     */
    @PutMapping("/{reviewId}")
    public void updateReview(
            @LoginUser Long userId,
            @PathVariable Long reviewId,
            @RequestBody Map<String, Object> request
    ) {
        String content = request.get("content").toString();
        Integer rating = Integer.valueOf(request.get("rating").toString());

        reviewService.updateReview(reviewId, userId, content, rating);
    }

    /**
     * 리뷰 삭제
     * DELETE /api/reviews/{reviewId}
     */
    @DeleteMapping("/{reviewId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(
            @LoginUser Long userId,
            @PathVariable Long reviewId
    ) {
        reviewService.deleteReview(reviewId, userId);
    }

    /**
     * 시설의 평균 별점 조회
     * GET /api/reviews/facility/{facilityId}/rating
     */
    @GetMapping("/facility/{facilityId}/rating")
    public Map<String, Object> getFacilityRating(@PathVariable Long facilityId) {
        Double avgRating = reviewService.getAverageRating(facilityId);
        int count = reviewService.getReviewCount(facilityId);

        return Map.of(
                "facilityId", facilityId,
                "averageRating", avgRating != null ? avgRating : 0.0,
                "reviewCount", count
        );
    }
}
