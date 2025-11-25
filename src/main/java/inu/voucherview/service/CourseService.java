package inu.voucherview.service;

import inu.voucherview.dto.CourseDto;
import inu.voucherview.response.CourseListResponse;

public interface CourseService {
    /**
     * 전체 강좌 목록 조회
     */
    CourseListResponse getAllCourses(int page, int limit);

    /**
     * 특정 시설에 속한 강좌 목록 조회
     */
    CourseListResponse getCoursesByFacilityId(Long facilityId, int page, int limit);

    /**
     * 강좌 단건 상세 조회
     */
    CourseDto getCourseById(Long courseId);

}
