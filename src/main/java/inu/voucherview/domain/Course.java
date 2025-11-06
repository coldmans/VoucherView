package inu.voucherview.domain;

import lombok.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "courseId")
public class Course {
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

}
