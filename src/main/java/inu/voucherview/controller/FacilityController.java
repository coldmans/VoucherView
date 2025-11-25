package inu.voucherview.controller;

import inu.voucherview.domain.Facility;
import inu.voucherview.dto.FacilityDto;
import inu.voucherview.response.CourseListResponse;
import inu.voucherview.response.FacilityListResponse;
import inu.voucherview.service.CourseService;
import inu.voucherview.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/facilities")
public class FacilityController {
    private final FacilityService facilityService;
    private final CourseService courseService;

    /**
     * [목록 조회] GET /api/facilities?page=1&limit=10
     */
    @GetMapping
    public FacilityListResponse getFacilityList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ){
        return facilityService.getFacilityList(page, limit);
    }

    /**
     * [상세 조회] GET /api/facilities/{facilityId}
     */
    @GetMapping("/{facilityId}")
    public FacilityDto getFacilityById(
            @PathVariable Long facilityId
    ){
        return facilityService.getFacilityById(facilityId);
    }

    /**
     * [특정 시설의 강좌 목록 조회] GET /api/facilities/{facilityId}/courses?page=1&limit=10
     */
    @GetMapping("/{facilityId}/courses")
    public CourseListResponse getCoursesByFacilityId(
            @PathVariable Long facilityId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ){
        return courseService.getCoursesByFacilityId(facilityId, page, limit);
    }



}
