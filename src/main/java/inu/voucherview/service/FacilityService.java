package inu.voucherview.service;

import inu.voucherview.domain.Facility;

import java.util.Map;

public interface FacilityService {
    /**
     * 시설 전체 목록 조회
     * @param page 현재 페이지
     * @param limit 페이지당 개수
     * @param ctprvnNm 필터: 시/도 명
     * @param itemNm 필터: 종목 명
     * @return 'facilities' (목록)와 'pagination'이 담긴 Map
     */
    Map<String, Object> getFacilityList(int page, int limit, String ctprvnNm, String itemNm);

    /**
     * 시설 상세 정보 조회
     * @param facilityId 시설 ID
     * @return Facility 객체
     */
    Facility getFacilityById(Long facilityId);
}
