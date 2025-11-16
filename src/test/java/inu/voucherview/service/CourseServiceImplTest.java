package inu.voucherview.service;

import inu.voucherview.domain.Course;
import inu.voucherview.exception.BusinessException;
import inu.voucherview.exception.ErrorCode;
import inu.voucherview.mapper.CourseMapper;
import inu.voucherview.response.CourseListResponse;
import inu.voucherview.util.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {
    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    private final Course fakeCourse = Course.of(1L, 1L);

    @Test
    @DisplayName("전체 강좌 목록 조회(성공, 페이지네이션 포함)")
    void getAllCourses_Success(){
        int page = 1;
        int limit = 10;
        int fakeTotalCount = 30;
        List<Course> fakeList = List.of(fakeCourse);
        when(courseMapper.countAll()).thenReturn(fakeTotalCount);
        when(courseMapper.findAll(any(Pagination.class))).thenReturn(fakeList);
        CourseListResponse result = courseService.getAllCourses(page, limit);

        assertThat(result).isNotNull();
        assertThat(result.getCourses()).isEqualTo(fakeList);
        Pagination p = result.getPagination();
        assertThat(p.getTotalCount()).isEqualTo(fakeTotalCount);
        assertThat(p.getTotalPages()).isEqualTo(3); // 30 / 10 = 3
    }

    @Test
    @DisplayName("전체 강좌 목록 조회(0건, 빈 목록)")
    void getAllCourses_ShouldReturnEmptyList(){
        int page = 1;
        int limit = 10;
        when(courseMapper.countAll()).thenReturn(0);
        when(courseMapper.findAll(any(Pagination.class))).thenReturn(new ArrayList<>());

        CourseListResponse result = courseService.getAllCourses(page, limit);

        assertThat(result).isNotNull();
        assertThat(result.getCourses()).isEqualTo(new ArrayList<>());
        assertThat(result.getPagination().getTotalCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("전체 강좌 목록 조회(실패, 잘못된 입력)")
    void getAllCourses_ShouldThrowException_WhenInvalidInput(){
        assertThatThrownBy(() -> courseService.getAllCourses(0, 10))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
        assertThatThrownBy(() -> courseService.getAllCourses(1, -1))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("특정 시설의 강좌 목록 조회(성공)")
    void getCoursesByFacilityId_Success(){
        int page = 1;
        int limit = 10;
        Long facilityId = 2L;
        int fakeTotalCount = 15;
        List<Course> fakeList = List.of(fakeCourse);
        when(courseMapper.countAll()).thenReturn(fakeTotalCount);
        when(courseMapper.findByFacilityId(eq(facilityId), any(Pagination.class))).thenReturn(fakeList);

        CourseListResponse result = courseService.getCoursesByFacilityId(facilityId, page, limit);

        assertThat(result).isNotNull();
        assertThat(result.getCourses()).isEqualTo(fakeList);
        assertThat(result.getPagination().getTotalCount()).isEqualTo(fakeTotalCount);
        verify(courseMapper, times(1)).findByFacilityId(eq(facilityId), any(Pagination.class));
    }

    @Test
    @DisplayName("특정 시설의 강좌 목록 조회(실패, 잘못된 입력)")
    void getCoursesByFacilityId_ShouldThrowException_WhenInvalidInput(){
        Long facilityId = 1L;
        assertThatThrownBy(() -> courseService.getCoursesByFacilityId(facilityId, 0, 10))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
        assertThatThrownBy(() -> courseService.getCoursesByFacilityId(facilityId, 1, 0))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("강좌 단건 조회(성공)")
    void getCourseById_Success(){
        Long courseId = 99L;
        when(courseMapper.findById(courseId)).thenReturn(fakeCourse);

        Course result = courseService.getCourseById(courseId);

        assertThat(result).isEqualTo(fakeCourse);
        verify(courseMapper, times(1)).findById(courseId);
    }

    @Test
    @DisplayName("강좌 단건 조회(실패, 존재하지 않는 ID)")
    void getCourseById_ShouldThrowException_WhenNotFound(){
        Long courseId = 123L;
        when(courseMapper.findById(courseId)).thenReturn(null);

        assertThatThrownBy(() -> courseService.getCourseById(courseId))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.COURSE_NOT_FOUND);
        verify(courseMapper, times(1)).findById(courseId);
    }

    @Test
    @DisplayName("특정 시설 강좌 목록 조회 결과가 없을 때 빈 목록 반환")
    void getCoursesByFacilityId_ShouldReturnEmptyList_WhenNoCourses(){
        int page = 1;
        int limit = 10;
        Long facilityId = 3L;
        int fakeTotalCount = 0;
        when(courseMapper.countAll()).thenReturn(fakeTotalCount);
        when(courseMapper.findByFacilityId(eq(facilityId), any(Pagination.class))).thenReturn(new ArrayList<>());

        CourseListResponse result = courseService.getCoursesByFacilityId(facilityId, page, limit);

        assertThat(result).isNotNull();
        assertThat(result.getCourses()).isEqualTo(new ArrayList<>());
        assertThat(result.getPagination().getTotalCount()).isEqualTo(fakeTotalCount);
    }
}
