package inu.voucherview.service;

import inu.voucherview.domain.Review;
import inu.voucherview.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public Review createReview(Long userId, Long facilityId, String content, Integer rating) {
        if(rating == null || rating < 1 || rating > 5){
            throw new IllegalArgumentException("별점은 1점에서 5점 사이에 주어져야함");
        }

        // 중복 리뷰 체크
        Review existingReview = reviewMapper.findByUserIdAndFacilityId(userId, facilityId);
        if(existingReview != null){
            throw new IllegalArgumentException("이미 해당 시설에 리뷰를 작성하셨습니다");
        }

        Review review = Review.builder()
                .userId(userId)
                .facilityId(facilityId)
                .content(content)
                .rating(rating)
                .createdAt(LocalDateTime.now())
                .build();
        reviewMapper.insertReview(review);
        return review;
    }

    @Override
    public Review getReview(Long reviewId) {
        return reviewMapper.findById(reviewId);
    }

    @Override
    public List<Review> getReviewsByFacility(Long facilityId, int page, int limit) {
        int offset = (page - 1) * limit;
        return reviewMapper.findByFacilityId(facilityId, offset, limit);
    }

    @Override
    public List<Review> getReviewsByUser(Long userId, int page, int limit) {
        int offset = (page - 1) * limit;
        return reviewMapper.findByUserId(userId, offset, limit);
    }

    @Override
    @Transactional
    public void updateReview(Long reviewId, Long userId, String content, Integer rating) {
        Review review = reviewMapper.findById(reviewId);

        if(review == null){
            throw new IllegalArgumentException("존재하지 않는 리뷰입니다.");
        }

        if(!review.getUserId().equals(userId)){
            throw new IllegalArgumentException("본인의 리뷰만 수정할 수 있습니다.");
        }

        review.changeContent(content);
        review.changeRating(rating);

        reviewMapper.updateReview(review);
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewMapper.findById(reviewId);

        if(review == null){
            throw new IllegalArgumentException("존재하지 않는 리뷰입니다.");
        }

        // 작성자 본인 확인
        if (!review.getUserId().equals(userId)){
            throw new IllegalArgumentException("일치하지 않는 사용자입니다.");
        }

        reviewMapper.deleteReview(reviewId);
    }

    @Override
    public Double getAverageRating(Long facilityId) {
        return reviewMapper.getAverageRating(facilityId);
    }

    @Override
    public int getReviewCount(Long facilityId) {
        return reviewMapper.countByFacilityId(facilityId);
    }
}
