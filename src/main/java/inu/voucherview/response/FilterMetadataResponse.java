package inu.voucherview.response;

import inu.voucherview.dto.RegionDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FilterMetadataResponse {
    private List<RegionDto> regions;  // 지역 계층 구조 (시/도 → 구/시)
    private List<String> sports;      // 사용 가능한 종목 목록
    private Double minRating;         // 최소 평점 (0.0)
    private Double maxRating;         // 최대 평점 (5.0)
}
