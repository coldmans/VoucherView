package inu.voucherview.service;

import inu.voucherview.domain.Facility;
import inu.voucherview.mapper.FacilityMapper;
import inu.voucherview.util.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacilityServiceImpl implements FacilityService{
    private final FacilityMapper facilityMapper;

    @Override
    public Map<String, Object> getFacilityList(int page, int limit, String ctprvnNm, String itemNm) {
        int totalCount = facilityMapper.countAll();
        Pagination pagination = new Pagination(page, limit, totalCount);
        List<Facility> facilityList = facilityMapper.findAll(pagination);
        Map<String, Object> result = new HashMap<>();
        result.put("facilities", facilityList);
        result.put("pagination", pagination);
        return result;
    }

    @Override
    public Facility getFacilityById(Long facilityId) {
        return facilityMapper.findById(facilityId);
    }
}
