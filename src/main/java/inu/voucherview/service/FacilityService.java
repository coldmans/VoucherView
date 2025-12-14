package inu.voucherview.service;

import inu.voucherview.domain.Facility;
import inu.voucherview.dto.FacilityDto;
import inu.voucherview.response.FacilityListResponse;

public interface FacilityService {
    /**
     * 시설 전체 목록 조회
     * @param userId 사용자 ID (로그인하지 않은 경우 null)
     * @param page 현재 페이지
     * @param limit 페이지당 개수
     * @return 'facilities' (목록)와 'pagination'이 담긴 Map
     */
    FacilityListResponse getFacilityList(Long userId, int page, int limit, String keyword, String ctNm, String ctDetailNm, String mainSport,
                                         Double minRating, Double maxRating, String sortBy, Double lat, Double lng, Integer radius);

    /**
     * 시설 상세 정보 조회
     * @param userId 사용자 ID (로그인하지 않은 경우 null)
     * @param facilityId 시설 ID
     * @return Facility 객체
     */
    FacilityDto getFacilityById(Long userId, Long facilityId);

    /**
     * 인기 시설 3개 조회
     * @return 'facilities' 목록
     */
    FacilityListResponse getFacilityFavoriteList();
}
