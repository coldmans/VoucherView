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
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "ctNm", required = false) String ctNm, // 지역 ex(서울, 경기)
            @RequestParam(name = "ctDetailNm", required = false) String ctDetailNm, // 구
            @RequestParam(name = "mainSport", required = false) String mainSport,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "maxRating", required = false) Double maxRating,
            @RequestParam(name = "sortBy", defaultValue = "rating") String sortBy, // rating, distance
            @RequestParam(name = "lat", required = false) Double lat,
            @RequestParam(name = "lng", required = false) Double lng,
            @RequestParam(name = "radius", required = false) int radius // 반경
    ){
        return facilityService.getFacilityList(page, limit, keyword, ctNm, ctDetailNm, mainSport,
                minRating, maxRating, sortBy, lat, lng, radius
        );
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
