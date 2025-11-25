package inu.voucherview.controller;

import inu.voucherview.domain.Course;
import inu.voucherview.dto.CourseDto;
import inu.voucherview.dto.FacilityDto;
import inu.voucherview.exception.BusinessException;
import inu.voucherview.exception.ErrorCode;
import inu.voucherview.response.CourseListResponse;
import inu.voucherview.response.FacilityListResponse;
import inu.voucherview.service.CourseService;
import inu.voucherview.service.FacilityService;
import inu.voucherview.util.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacilityController.class)
@Import(TestSecurityConfig.class)
class FacilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacilityService facilityService;

    @MockitoBean
    private CourseService courseService;

    @Test
    @DisplayName("시설 목록 조회 - 성공")
    void getFacilityList_Success() throws Exception {
        // Given
        List<FacilityDto> dtoList = new ArrayList<>();
        FacilityDto dto = new FacilityDto(createMockFacility(1L, "테스트 시설", 37.5, 127.0));
        dtoList.add(dto);

        Pagination pagination = new Pagination(1, 10, 1);
        FacilityListResponse response = new FacilityListResponse(dtoList, pagination);

        when(facilityService.getFacilityList(1, 10)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/facilities")
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facilityList").isArray())
                .andExpect(jsonPath("$.facilityList[0].facilityId").value(1))
                .andExpect(jsonPath("$.facilityList[0].name").value("테스트 시설"))
                .andExpect(jsonPath("$.facilityList[0].latitude").value(37.5))
                .andExpect(jsonPath("$.facilityList[0].longitude").value(127.0))
                .andExpect(jsonPath("$.pagination.page").value(1))
                .andExpect(jsonPath("$.pagination.limit").value(10))
                .andExpect(jsonPath("$.pagination.totalCount").value(1));
    }

    @Test
    @DisplayName("시설 목록 조회 - 기본 파라미터 사용")
    void getFacilityList_WithDefaultParams() throws Exception {
        // Given
        List<FacilityDto> dtoList = new ArrayList<>();
        Pagination pagination = new Pagination(1, 10, 0);
        FacilityListResponse response = new FacilityListResponse(dtoList, pagination);

        when(facilityService.getFacilityList(1, 10)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/facilities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facilityList").isArray())
                .andExpect(jsonPath("$.pagination").exists());
    }

    @Test
    @DisplayName("시설 목록 조회 - 잘못된 파라미터로 400 에러")
    void getFacilityList_InvalidParams() throws Exception {
        // Given
        when(facilityService.getFacilityList(0, 10))
                .thenThrow(new BusinessException(ErrorCode.INVALID_INPUT));

        // When & Then
        mockMvc.perform(get("/api/facilities")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("시설 상세 조회 - 성공")
    void getFacilityById_Success() throws Exception {
        // Given
        Long facilityId = 1L;
        FacilityDto dto = new FacilityDto(createMockFacility(facilityId, "테스트 시설", 37.5, 127.0));

        when(facilityService.getFacilityById(facilityId)).thenReturn(dto);

        // When & Then
        mockMvc.perform(get("/api/facilities/{facilityId}", facilityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facilityId").value(1))
                .andExpect(jsonPath("$.name").value("테스트 시설"))
                .andExpect(jsonPath("$.latitude").value(37.5))
                .andExpect(jsonPath("$.longitude").value(127.0));
    }

    @Test
    @DisplayName("시설 상세 조회 - 존재하지 않는 시설")
    void getFacilityById_NotFound() throws Exception {
        // Given
        Long nonExistentId = 999L;
        when(facilityService.getFacilityById(nonExistentId))
                .thenThrow(new BusinessException(ErrorCode.FACILITY_NOT_FOUND));

        // When & Then
        mockMvc.perform(get("/api/facilities/{facilityId}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("시설 상세 조회 - Point 직렬화 테스트 (null 처리)")
    void getFacilityById_WithNullLocation() throws Exception {
        // Given
        Long facilityId = 1L;
        FacilityDto dto = new FacilityDto(createMockFacility(facilityId, "테스트 시설", null, null));

        when(facilityService.getFacilityById(facilityId)).thenReturn(dto);

        // When & Then
        mockMvc.perform(get("/api/facilities/{facilityId}", facilityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.facilityId").value(1))
                .andExpect(jsonPath("$.name").value("테스트 시설"))
                .andExpect(jsonPath("$.latitude").isEmpty())
                .andExpect(jsonPath("$.longitude").isEmpty());
    }

    @Test
    @DisplayName("특정 시설의 강좌 목록 조회 - 성공")
    void getCoursesByFacilityId_Success() throws Exception {
        // Given
        Long facilityId = 1L;
        List<CourseDto> courseList = new ArrayList<>();
        courseList.add(new CourseDto(Course.of(1L, facilityId)));
        courseList.add(new CourseDto(Course.of(2L, facilityId)));

        Pagination pagination = new Pagination(1, 10, 2);
        CourseListResponse response = new CourseListResponse(courseList, pagination);

        when(courseService.getCoursesByFacilityId(facilityId, 1, 10)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/facilities/{facilityId}/courses", facilityId)
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses.length()").value(2))
                .andExpect(jsonPath("$.courses[0].courseId").value(1))
                .andExpect(jsonPath("$.courses[0].facilityId").value(facilityId))
                .andExpect(jsonPath("$.pagination.page").value(1))
                .andExpect(jsonPath("$.pagination.limit").value(10))
                .andExpect(jsonPath("$.pagination.totalCount").value(2));
    }

    @Test
    @DisplayName("특정 시설의 강좌 목록 조회 - 기본 파라미터")
    void getCoursesByFacilityId_WithDefaultParams() throws Exception {
        // Given
        Long facilityId = 1L;
        List<CourseDto> courseList = new ArrayList<>();
        Pagination pagination = new Pagination(1, 10, 0);
        CourseListResponse response = new CourseListResponse(courseList, pagination);

        when(courseService.getCoursesByFacilityId(facilityId, 1, 10)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/facilities/{facilityId}/courses", facilityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.pagination").exists());
    }

    @Test
    @DisplayName("특정 시설의 강좌 목록 조회 - 존재하지 않는 시설")
    void getCoursesByFacilityId_FacilityNotFound() throws Exception {
        // Given
        Long nonExistentId = 999L;
        when(courseService.getCoursesByFacilityId(nonExistentId, 1, 10))
                .thenThrow(new BusinessException(ErrorCode.FACILITY_NOT_FOUND));

        // When & Then
        mockMvc.perform(get("/api/facilities/{facilityId}/courses", nonExistentId)
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isNotFound());
    }

    /**
     * 테스트용 Mock Facility 생성 헬퍼 메서드
     */
    private inu.voucherview.domain.Facility createMockFacility(Long id, String name, Double lat, Double lng) {
        inu.voucherview.domain.Facility facility = inu.voucherview.domain.Facility.of(id, name);

        // Reflection으로 private 필드 설정 (테스트 목적)
        try {
            java.lang.reflect.Field addressField = facility.getClass().getDeclaredField("address");
            addressField.setAccessible(true);
            addressField.set(facility, "서울시 테스트구");

            java.lang.reflect.Field phoneField = facility.getClass().getDeclaredField("phoneNumber");
            phoneField.setAccessible(true);
            phoneField.set(facility, "02-1234-5678");

            java.lang.reflect.Field sportField = facility.getClass().getDeclaredField("mainSport");
            sportField.setAccessible(true);
            sportField.set(facility, "축구");

            if (lat != null && lng != null) {
                org.locationtech.jts.geom.GeometryFactory geometryFactory = new org.locationtech.jts.geom.GeometryFactory();
                org.locationtech.jts.geom.Point point = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(lng, lat));

                java.lang.reflect.Field locationField = facility.getClass().getDeclaredField("location");
                locationField.setAccessible(true);
                locationField.set(facility, point);
            }
        } catch (Exception e) {
            throw new RuntimeException("테스트 데이터 생성 실패", e);
        }

        return facility;
    }
}
