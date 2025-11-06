package inu.voucherview.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Course {
    private Long courseId; // PK
    private Long facilityId; // FK
    private String courseNm; // 강좌명
    private String teacher; // 강사명
    private String beginTime; // 시작시간
    private String endTime; // 종료시간
    private String weekDay; // 요일명
    private String courseDetail; // 상세 내용
    private BigDecimal price; // 수강 가격
    private BigDecimal priceValue; // 결제 금액 값

}
