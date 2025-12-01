package inu.voucherview.dto;

import inu.voucherview.domain.Facility;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

@Getter
public class FacilityDto {
    private Long facilityId; // PK
    private String name;
    private String address;
    private String phoneNumber;
    private String mainSport;
    private Double averRating;
    private Integer reviewCount; // 리뷰 개수
    private Boolean isFavorite; // 찜 여부 (로그인하지 않은 경우 false)

    private Double latitude; // 위도
    private Double longitude; // 경도

    public FacilityDto(Facility facility){
        this.facilityId = facility.getFacilityId();
        this.name = facility.getName();
        this.address = facility.getAddress();
        this.phoneNumber = facility.getPhoneNumber();
        this.mainSport = facility.getMainSport();
        this.averRating = facility.getAverRating();
        this.reviewCount = facility.getReviewCount();
        this.isFavorite = false; // 기본값

        // Domain에서 직접 latitude/longitude 가져오기 (DB에서 ST_X/ST_Y로 추출됨)
        this.latitude = facility.getLatitude();
        this.longitude = facility.getLongitude();
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

}
