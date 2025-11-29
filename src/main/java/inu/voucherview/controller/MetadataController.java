package inu.voucherview.controller;

import inu.voucherview.dto.RegionDto;
import inu.voucherview.response.FilterMetadataResponse;
import inu.voucherview.service.MetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/metadata")
public class MetadataController {
    private final MetadataService metadataService;

    /**
     * [필터 메타데이터 전체 조회] GET /api/metadata/filters
     * 응답 예시:
     * {
     *   "regions": [
     *     {"province": "서울", "cities": ["강남구", "서초구", ...]},
     *     {"province": "경기", "cities": ["수원시", "성남시", ...]}
     *   ],
     *   "sports": ["수영", "헬스", "요가", ...],
     *   "minRating": 0.0,
     *   "maxRating": 5.0
     * }
     */
    @GetMapping("/filters")
    public FilterMetadataResponse getFilterMetadata() {
        return metadataService.getFilterMetadata();
    }

    /**
     * [지역 계층 구조 조회] GET /api/metadata/regions
     */
    @GetMapping("/regions")
    public List<RegionDto> getAvailableRegions() {
        return metadataService.getAvailableRegions();
    }

    /**
     * [특정 시/도의 구/시 목록] GET /api/metadata/regions/{province}/cities
     * 예: GET /api/metadata/regions/서울/cities
     */
    @GetMapping("/regions/{province}/cities")
    public List<String> getCitiesByProvince(@PathVariable String province) {
        return metadataService.getCitiesByProvince(province);
    }

    /**
     * [사용 가능한 종목 목록] GET /api/metadata/sports
     */
    @GetMapping("/sports")
    public List<String> getAvailableSports() {
        return metadataService.getAvailableSports();
    }
}
