package inu.voucherview.service;

import inu.voucherview.domain.Facility;
import inu.voucherview.dto.FacilityDto;
import inu.voucherview.exception.BusinessException;
import inu.voucherview.exception.ErrorCode;
import inu.voucherview.mapper.FacilityMapper;
import inu.voucherview.mapper.FavoriteFacilityMapper;
import inu.voucherview.response.FacilityListResponse;
import inu.voucherview.util.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacilityServiceImpl implements FacilityService {
    private final FacilityMapper facilityMapper;
    private final FavoriteFacilityMapper favoriteFacilityMapper;

    @Override
    public FacilityListResponse getFacilityList(Long userId, int page, int limit, String keyword, String ctNm, String ctDetailNm, String mainSport,
                                                Double minRating, Double maxRating, String sortBy, Double lat, Double lng, Integer radius) {
        if(page <= 0 || limit <= 0){
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        if("distance".equals(sortBy) && (lat == null || lng == null)){
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        // MyBatis <where> 절이 빈 조건을 처리하므로 항상 findWithFilters 사용
        int totalCount = facilityMapper.countWithFilters(keyword, ctNm, ctDetailNm, mainSport, minRating,
                maxRating, lat, lng, radius);
        Pagination pagination = new Pagination(page, limit, totalCount);

        List<Facility> facilityList = facilityMapper.findWithFilters(pagination, keyword, ctNm, ctDetailNm, mainSport,
                minRating, maxRating, sortBy, lat, lng, radius);

        List<FacilityDto> dtoList = facilityList.stream()
                .map(facility -> {
                    FacilityDto dto = new FacilityDto(facility);
                    // 로그인한 사용자인 경우에만 찜 여부 조회
                    if (userId != null) {
                        boolean isFavorite = favoriteFacilityMapper.exists(userId, facility.getFacilityId());
                        dto.setIsFavorite(isFavorite);
                    }
                    return dto;
                })
                .toList();

        return new FacilityListResponse(dtoList, pagination);
    }

    @Override
    public FacilityDto getFacilityById(Long userId, Long facilityId) {
        Facility facility = facilityMapper.findById(facilityId);
        if(facility == null){
            throw new BusinessException(ErrorCode.FACILITY_NOT_FOUND);
        }

        FacilityDto dto = new FacilityDto(facility);
        // 로그인한 사용자인 경우에만 찜 여부 조회
        if (userId != null) {
            boolean isFavorite = favoriteFacilityMapper.exists(userId, facilityId);
            dto.setIsFavorite(isFavorite);
        }

        return dto;
    }
}
