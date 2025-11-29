package inu.voucherview.service;

import inu.voucherview.dto.RegionDto;
import inu.voucherview.response.FilterMetadataResponse;

import java.util.List;

public interface MetadataService {
    /**
     * 필터링에 필요한 모든 메타데이터 반환
     */
    FilterMetadataResponse getFilterMetadata();

    /**
     * DB에 실제로 존재하는 지역 계층 구조 반환 (시/도 → 구/시)
     */
    List<RegionDto> getAvailableRegions();

    /**
     * 특정 시/도의 구/시 목록 반환
     */
    List<String> getCitiesByProvince(String province);

    /**
     * DB에 실제로 존재하는 종목 목록 반환
     */
    List<String> getAvailableSports();
}
