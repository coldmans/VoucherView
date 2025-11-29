package inu.voucherview.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacilitySearchRequest {
    private String keyword;          // 통합 검색 (이름, 주소, 종목)
    private String ctNm;             // 지역 (시/도)
    private String ctDetailNm;       // 세부 지역 (구)
    private String mainSport;        // 주 종목
    private Double minRating;        // 최소 평점
    private Double maxRating;        // 최대 평점
    private String sortBy = "rating"; // 정렬 기준 (rating, distance, name)
    private Double lat;              // 위도 (거리 정렬 시)
    private Double lng;              // 경도 (거리 정렬 시)
    private Integer radius;          // 반경 (미터)
}
