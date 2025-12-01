package inu.voucherview.controller;

import inu.voucherview.annotation.LoginUser;
import inu.voucherview.domain.FavoriteFacility;
import inu.voucherview.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    /**
     * 찜 추가
     * POST /api/favorites/{facilityId}
     */
    @PostMapping("/{facilityId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> addFavorite(
            @LoginUser Long userId,
            @PathVariable Long facilityId
    ) {
        favoriteService.addFavorite(userId, facilityId);
        return Map.of("message", "찜이 추가되었습니다");
    }

    /**
     * 찜 삭제
     * DELETE /api/favorites/{facilityId}
     */
    @DeleteMapping("/{facilityId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavorite(
            @LoginUser Long userId,
            @PathVariable Long facilityId
    ) {
        favoriteService.removeFavorite(userId, facilityId);
    }

    /**
     * 찜 여부 확인
     * GET /api/favorites/{facilityId}/status
     */
    @GetMapping("/{facilityId}/status")
    public Map<String, Boolean> checkFavoriteStatus(
            @LoginUser Long userId,
            @PathVariable Long facilityId
    ) {
        boolean isFavorite = favoriteService.isFavorite(userId, facilityId);
        return Map.of("isFavorite", isFavorite);
    }

    /**
     * 내 찜 목록 조회
     * GET /api/favorites
     */
    @GetMapping
    public List<FavoriteFacility> getMyFavorites(@LoginUser Long userId) {
        return favoriteService.getUserFavorites(userId);
    }

    /**
     * 시설의 찜 개수 조회
     * GET /api/favorites/facility/{facilityId}/count
     */
    @GetMapping("/facility/{facilityId}/count")
    public Map<String, Integer> getFavoriteCount(@PathVariable Long facilityId) {
        int count = favoriteService.getFavoriteCount(facilityId);
        return Map.of("facilityId", facilityId.intValue(), "favoriteCount", count);
    }
}