package inu.voucherview.dto;

import inu.voucherview.domain.Course;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
public class CourseDto {
    private Long courseId; // PK
    private Long facilityId; // FK
    private String courseNm; // 강좌명
    private String teacher; // 강사명
    private LocalTime beginTime; // 시작시간
    private LocalTime endTime; // 종료시간
    private String weekDay; // 요일명
    private String courseDetail; // 상세 내용
    private BigDecimal price; // 수강 가격
    private BigDecimal priceValue; // 결제 금액 값

    public CourseDto(Course course) {
        this.courseId = course.getCourseId();
        this.facilityId = course.getFacilityId();
        this.courseNm = course.getCourseNm();
        this.teacher = course.getTeacher();
        this.beginTime = course.getBeginTime();
        this.endTime = course.getEndTime();
        this.weekDay = course.getWeekDay();
        this.courseDetail = course.getCourseDetail();
        this.price = course.getPrice();
        this.priceValue = course.getPriceValue();
    }

}
