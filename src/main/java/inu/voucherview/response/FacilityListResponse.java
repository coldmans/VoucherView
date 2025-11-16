package inu.voucherview.response;

import inu.voucherview.domain.Facility;
import inu.voucherview.util.Pagination;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Facility 목록 조회를 위한 전용 응답 DTO
 */
@Getter
@RequiredArgsConstructor
public class FacilityListResponse {
    private final List<Facility> facilityList;
    private final Pagination pagination;
}
