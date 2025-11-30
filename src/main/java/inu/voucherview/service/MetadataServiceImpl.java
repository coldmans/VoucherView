package inu.voucherview.service;

import inu.voucherview.dto.RegionDto;
import inu.voucherview.mapper.FacilityMapper;
import inu.voucherview.response.FilterMetadataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetadataServiceImpl implements MetadataService {
    private final FacilityMapper facilityMapper;

    @Override
    public FilterMetadataResponse getFilterMetadata() {
        return FilterMetadataResponse.builder()
                .regions(getAvailableRegions())
                .sports(facilityMapper.getDistinctSports())
                .minRating(0.0)
                .maxRating(5.0)
                .build();
    }

    @Override
    public List<RegionDto> getAvailableRegions() {
        // 모든 시/도 목록 가져오기
        List<String> provinces = facilityMapper.getDistinctProvinces();

        // 각 시/도에 대해 구/시 목록 조회
        return provinces.stream()
                .map(province -> new RegionDto(
                        province,
                        facilityMapper.getDistinctCities(province)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getCitiesByProvince(String province) {
        return facilityMapper.getDistinctCities(province);
    }

    @Override
    public List<String> getAvailableSports() {
        return facilityMapper.getDistinctSports();
    }
}
