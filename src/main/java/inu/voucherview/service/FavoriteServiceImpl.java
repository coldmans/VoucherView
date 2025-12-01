package inu.voucherview.service;

import inu.voucherview.domain.FavoriteFacility;
import inu.voucherview.mapper.FavoriteFacilityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteFacilityMapper favoriteFacilityMapper;

    @Override
    @Transactional
    public void addFavorite(Long userId, Long facilityId) {
        // 중복 찜 체크
        if (favoriteFacilityMapper.exists(userId, facilityId)) {
            throw new IllegalArgumentException("이미 찜한 시설입니다");
        }

        FavoriteFacility favorite = FavoriteFacility.of(userId, facilityId);
        try {
            favoriteFacilityMapper.insert(favorite);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("이미 찜한 시설입니다");
        }
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long facilityId) {
        int deleted = favoriteFacilityMapper.delete(userId, facilityId);
        if (deleted == 0) {
            throw new IllegalArgumentException("찜하지 않은 시설입니다");
        }
    }

    @Override
    public boolean isFavorite(Long userId, Long facilityId) {
        return favoriteFacilityMapper.exists(userId, facilityId);
    }

    @Override
    public List<FavoriteFacility> getUserFavorites(Long userId) {
        return favoriteFacilityMapper.findByUserId(userId);
    }

    @Override
    public int getFavoriteCount(Long facilityId) {
        return favoriteFacilityMapper.countByFacilityId(facilityId);
    }
}