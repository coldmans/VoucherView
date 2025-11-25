package inu.voucherview.response;

import inu.voucherview.dto.CourseDto;
import inu.voucherview.util.Pagination;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Course 목록 조회를 위한 전용 응답 DTO
 */
@Getter
@RequiredArgsConstructor
public class CourseListResponse {
    private final List<CourseDto> courses;
    private final Pagination pagination;
}
