package inu.voucherview.service;

import inu.voucherview.domain.Review;

import java.util.List;

public interface ReviewService {
    /**
     * 리뷰 작성
     */
    Review createReview(Long userId, Long facilityId, String content, Integer rating);

    /**
     * 리뷰 조회
     */
    Review getReview(Long reviewId);

    /**
     * 특정 시설의 모든 리뷰 조회 (페이지네이션)
     */
    List<Review> getReviewsByFacility(Long facilityId, int page, int limit);

    /**
     * 특정 유저의 모든 리뷰 조회 (페이지네이션)
     */
    List<Review> getReviewsByUser(Long userId, int page, int limit);

    /**
     * 리뷰 수정
     */
    void updateReview(Long reviewId, Long userId, String content, Integer rating);

    /**
     * 리뷰 삭제
     */
    void deleteReview(Long reviewId, Long userId);

    /**
     * 시설의 평균 별점 조회
     */
    Double getAverageRating(Long facilityId);

    /**
     * 시설의 리뷰 개수
     */
    int getReviewCount(Long facilityId);
}