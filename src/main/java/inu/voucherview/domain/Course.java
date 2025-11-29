package inu.voucherview.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "courseId")
public class Course {
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

    public static Course of(Long courseId, Long facilityId){
        Course course = new Course();
        course.facilityId = facilityId;
        course.courseId = courseId;
        return course;
    }
}