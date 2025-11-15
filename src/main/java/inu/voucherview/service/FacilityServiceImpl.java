package inu.voucherview.service;

import inu.voucherview.domain.Facility;
import inu.voucherview.exception.BusinessException;
import inu.voucherview.exception.ErrorCode;
import inu.voucherview.mapper.FacilityMapper;
import inu.voucherview.response.FacilityListResponse;
import inu.voucherview.util.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacilityServiceImpl implements FacilityService{
    private final FacilityMapper facilityMapper;

    @Override
    public FacilityListResponse getFacilityList(int page, int limit) {
        if(page <= 0 || limit <= 0){
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        int totalCount = facilityMapper.countAll();
        Pagination pagination = new Pagination(page, limit, totalCount);
        List<Facility> facilityList = facilityMapper.findAll(pagination);

        return new FacilityListResponse(facilityList, pagination);
    }

    @Override
    public Facility getFacilityById(Long facilityId) {
        Facility facility = facilityMapper.findById(facilityId);
        if(facility == null){
            throw new BusinessException(ErrorCode.FACILITY_NOT_FOUND);
        }
        return facility;
    }
}
