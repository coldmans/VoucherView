package inu.voucherview.dto;

import inu.voucherview.domain.Course;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class CourseDto {
    private Long courseId; // PK
    private Long facilityId; // FK

    private String courseNo; // 강좌번호
    private String courseName; // 강좌명

    private String sportCd; // 종목 코드
    private String sportName; // 종목명

    private LocalDate startDate; // 시작일
    private LocalDate endDate; // 종료일

    private String establishmentYear; // 개설년도
    private String establishmentMonth; // 개설월

    private Integer requestCount; // 신청인원
    private BigDecimal price; // 가격

    public CourseDto(Course course) {
        this.courseId = course.getCourseId();
        this.facilityId = course.getFacilityId();
        this.courseNo = course.getCourseNo();
        this.courseName = course.getCourseName();
        this.sportCd = course.getSportCd();
        this.sportName = course.getSportName();
        this.startDate = course.getStartDate();
        this.endDate = course.getEndDate();
        this.establishmentYear = course.getEstablishmentYear();
        this.establishmentMonth = course.getEstablishmentMonth();
        this.requestCount = course.getRequestCount();
        this.price = course.getPrice();
    }
}