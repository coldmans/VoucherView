package inu.voucherview.controller;

import inu.voucherview.domain.Course;
import inu.voucherview.exception.BusinessException;
import inu.voucherview.exception.ErrorCode;
import inu.voucherview.response.CourseListResponse;
import inu.voucherview.service.CourseService;
import inu.voucherview.util.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@Import(TestSecurityConfig.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    @Test
    @DisplayName("전체 강좌 목록 조회 - 성공")
    void getAllCourses_Success() throws Exception {
        // Given
        List<Course> courseList = new ArrayList<>();
        courseList.add(Course.of(1L, 1L));
        courseList.add(Course.of(2L, 1L));
        courseList.add(Course.of(3L, 2L));

        Pagination pagination = new Pagination(1, 10, 3);
        CourseListResponse response = new CourseListResponse(courseList, pagination);

        when(courseService.getAllCourses(1, 10)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/courses")
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses.length()").value(3))
                .andExpect(jsonPath("$.courses[0].courseId").value(1))
                .andExpect(jsonPath("$.courses[1].courseId").value(2))
                .andExpect(jsonPath("$.courses[2].courseId").value(3))
                .andExpect(jsonPath("$.pagination.page").value(1))
                .andExpect(jsonPath("$.pagination.limit").value(10))
                .andExpect(jsonPath("$.pagination.totalCount").value(3));
    }

    @Test
    @DisplayName("전체 강좌 목록 조회 - 기본 파라미터 사용")
    void getAllCourses_WithDefaultParams() throws Exception {
        // Given
        List<Course> courseList = new ArrayList<>();
        Pagination pagination = new Pagination(1, 10, 0);
        CourseListResponse response = new CourseListResponse(courseList, pagination);

        when(courseService.getAllCourses(1, 10)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.pagination").exists())
                .andExpect(jsonPath("$.pagination.page").value(1))
                .andExpect(jsonPath("$.pagination.limit").value(10));
    }

    @Test
    @DisplayName("전체 강좌 목록 조회 - 빈 목록")
    void getAllCourses_EmptyList() throws Exception {
        // Given
        List<Course> emptyList = new ArrayList<>();
        Pagination pagination = new Pagination(1, 10, 0);
        CourseListResponse response = new CourseListResponse(emptyList, pagination);

        when(courseService.getAllCourses(1, 10)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/courses")
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses").isArray())
                .andExpect(jsonPath("$.courses").isEmpty())
                .andExpect(jsonPath("$.pagination.totalCount").value(0));
    }

    @Test
    @DisplayName("전체 강좌 목록 조회 - 잘못된 파라미터로 400 에러")
    void getAllCourses_InvalidParams() throws Exception {
        // Given
        when(courseService.getAllCourses(0, 10))
                .thenThrow(new BusinessException(ErrorCode.INVALID_INPUT));

        // When & Then
        mockMvc.perform(get("/api/courses")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("강좌 단건 조회 - 성공")
    void getCourseById_Success() throws Exception {
        // Given
        Long courseId = 1L;
        Course course = Course.of(courseId, 1L);

        when(courseService.getCourseById(courseId)).thenReturn(course);

        // When & Then
        mockMvc.perform(get("/api/courses/{courseId}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(courseId))
                .andExpect(jsonPath("$.facilityId").value(1L));
    }

    @Test
    @DisplayName("강좌 단건 조회 - 존재하지 않는 강좌")
    void getCourseById_NotFound() throws Exception {
        // Given
        Long nonExistentId = 999L;
        when(courseService.getCourseById(nonExistentId))
                .thenThrow(new BusinessException(ErrorCode.COURSE_NOT_FOUND));

        // When & Then
        mockMvc.perform(get("/api/courses/{courseId}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("강좌 단건 조회 - 잘못된 ID 형식")
    void getCourseById_InvalidIdFormat() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/courses/{courseId}", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("전체 강좌 목록 조회 - 다양한 페이지네이션 테스트")
    void getAllCourses_Pagination() throws Exception {
        // Given
        List<Course> courseList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            courseList.add(Course.of((long) i, 1L));
        }

        Pagination pagination = new Pagination(2, 5, 25);
        CourseListResponse response = new CourseListResponse(courseList, pagination);

        when(courseService.getAllCourses(2, 5)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/courses")
                        .param("page", "2")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses.length()").value(5))
                .andExpect(jsonPath("$.pagination.page").value(2))
                .andExpect(jsonPath("$.pagination.limit").value(5))
                .andExpect(jsonPath("$.pagination.totalCount").value(25))
                .andExpect(jsonPath("$.pagination.totalPages").value(5));
    }
}
