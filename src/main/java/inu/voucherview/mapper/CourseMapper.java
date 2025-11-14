package inu.voucherview.mapper;

import inu.voucherview.domain.Course;
import inu.voucherview.util.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseMapper {
    /**
     * 특정 강좌 1건을 ID로 조회
     * @Param courseId 강좌 ID
     * @return Course 객체
     */
    Course findById(@Param("courseId") Long courseId);

    /**
     * @param pagination 페이지네이션 객체
     * @return 특정 페이지의 강좌 목록
     */
    List<Course> findAll(@Param("page") Pagination pagination);

    int countAll();

    /**
     * 특정 시설에 속한 강좌 목록 조회
     * @param facilityId (FK)
     * @param pagination 페이지네이션 객체
     * @return 특정 시설의 특정 페이지 강좌 목록
     */
    List<Course> findByFacilityId(@Param("facilityId") Long facilityId,
                                  @Param("page") Pagination pagination);

    /**
     * 특정 시설에 속한 강좌의 총 개수 조회
     * @param facilityId 시설 ID (FK)
     * @return 특정 시설의 강좌 개수
     */
    int countByFacilityId(@Param("facilityId") Long facilityId);
}
