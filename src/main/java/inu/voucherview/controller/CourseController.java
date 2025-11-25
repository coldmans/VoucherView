package inu.voucherview.controller;

import inu.voucherview.dto.CourseDto;
import inu.voucherview.response.CourseListResponse;
import inu.voucherview.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    /**
     * [전체 목록 조회] GET /api/courses?page=1&limit=10
     */
    @GetMapping
    public CourseListResponse getAllCourses(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit
    ){
        return courseService.getAllCourses(page,limit);
    }


    /**
     * [단건 상세 조회] GET /api/courses/{courseId}
     */
    @GetMapping("/{courseId}")
    public CourseDto getCourseById(
            @PathVariable Long courseId
    ){
        return courseService.getCourseById(courseId);
    }
}
