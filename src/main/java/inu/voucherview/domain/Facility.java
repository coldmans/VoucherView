package inu.voucherview.domain;

import lombok.*;
import org.locationtech.jts.geom.Point;

@Getter
@ToString
@EqualsAndHashCode(of = "facilityId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Facility {
    private Long facilityId; // PK
    private String name;
    private String address;
    private String ctNm; // 지역
    private String ctDetailNm; // 구
    private String detailAddress;
    private String phoneNumber;
    private Long mainSportId;
    private String mainSport;
    private String zipNo;

    private Point location;
    private Double latitude;  // ST_Y(location)
    private Double longitude; // ST_X(location)
    private Double averRating;
    private Integer reviewCount; // 리뷰 개수

    public void changeAverRating(Double averRating){
        this.averRating = averRating;
    }

    public void changeReviewCount(Integer reviewCount){
        this.reviewCount = reviewCount;
    }

    public static Facility of(Long facilityId, String name){
        Facility f = new Facility();
        f.name = name;
        f.facilityId = facilityId;
        return f;
    }
}
