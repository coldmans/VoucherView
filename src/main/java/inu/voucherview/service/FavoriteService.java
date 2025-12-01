package inu.voucherview.service;

import inu.voucherview.domain.FavoriteFacility;

import java.util.List;

public interface FavoriteService {
    /**
     * 찜 추가
     */
    void addFavorite(Long userId, Long facilityId);

    /**
     * 찜 삭제
     */
    void removeFavorite(Long userId, Long facilityId);

    /**
     * 찜 여부 확인
     */
    boolean isFavorite(Long userId, Long facilityId);

    /**
     * 사용자의 찜 목록 조회
     */
    List<FavoriteFacility> getUserFavorites(Long userId);

    /**
     * 시설의 찜 개수 조회
     */
    int getFavoriteCount(Long facilityId);
}