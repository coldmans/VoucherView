package inu.voucherview.service;

import inu.voucherview.domain.Course;
import inu.voucherview.exception.BusinessException;
import inu.voucherview.exception.ErrorCode;
import inu.voucherview.mapper.CourseMapper;
import inu.voucherview.response.CourseListResponse;
import inu.voucherview.util.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {
    private final CourseMapper courseMapper;

    @Override
    public CourseListResponse getAllCourses(int page, int limit) {
        if(page <= 0 || limit <= 0){
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        int totalCount = courseMapper.countAll();
        Pagination pagination = new Pagination(page, limit, totalCount);
        List<Course> courseList = courseMapper.findAll(pagination);
        return new CourseListResponse(courseList, pagination);

    }

    @Override
    public CourseListResponse getCoursesByFacilityId(Long facilityId, int page, int limit) {
        if(page <= 0 || limit <= 0){
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        int totalCount = courseMapper.countByFacilityId(facilityId);
        Pagination pagination = new Pagination(page, limit, totalCount);
        List<Course> courseList = courseMapper.findByFacilityId(facilityId, pagination);
        return new CourseListResponse(courseList, pagination);
    }

    @Override
    public Course getCourseById(Long courseId) {
        return Optional.ofNullable(courseMapper.findById(courseId))
                .orElseThrow(() -> new BusinessException(ErrorCode.COURSE_NOT_FOUND));
    }
}
