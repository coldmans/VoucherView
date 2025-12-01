package inu.voucherview.mapper;

import inu.voucherview.domain.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReviewMapper {
    // 리뷰 생성
    void insertReview(Review review);

    // 리뷰 조회 (ID로)
    Review findById(Long reviewId);

    // 특정 시설의 모든 리뷰 조회 (페이지네이션)
    List<Review> findByFacilityId(@Param("facilityId") Long facilityId,
                                   @Param("offset") int offset,
                                   @Param("limit") int limit);

    // 특정 유저의 모든 리뷰 조회 (페이지네이션)
    List<Review> findByUserId(@Param("userId") Long userId,
                               @Param("offset") int offset,
                               @Param("limit") int limit);

    // 리뷰 수정
    void updateReview(Review review);

    // 리뷰 삭제
    void deleteReview(Long reviewId);

    // 시설의 평균 별점 조회
    Double getAverageRating(Long facilityId);

    // 시설의 리뷰 개수
    int countByFacilityId(Long facilityId);

    // 특정 유저가 특정 시설에 리뷰를 작성했는지 확인
    Review findByUserIdAndFacilityId(@Param("userId") Long userId,
                                      @Param("facilityId") Long facilityId);
}
