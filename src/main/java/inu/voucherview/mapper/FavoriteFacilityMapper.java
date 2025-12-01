package inu.voucherview.mapper;

import inu.voucherview.domain.FavoriteFacility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteFacilityMapper {

    // 찜 추가
    int insert(FavoriteFacility favoriteFacility);

    // 찜 삭제
    int delete(@Param("userId") Long userId, @Param("facilityId") Long facilityId);

    // 찜 여부 확인
    boolean exists(@Param("userId") Long userId, @Param("facilityId") Long facilityId);

    // 사용자의 찜 목록 조회
    List<FavoriteFacility> findByUserId(@Param("userId") Long userId);

    // 시설의 찜 개수 조회
    int countByFacilityId(@Param("facilityId") Long facilityId);
}
