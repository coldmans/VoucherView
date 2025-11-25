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

    private Double latitude; // 위도
    private Double longitude; // 경도

    public FacilityDto(Facility facility){
        this.facilityId = facility.getFacilityId();
        this.name = facility.getName();
        this.address = facility.getAddress();
        this.phoneNumber = facility.getPhoneNumber();
        this.mainSport = facility.getMainSport();
        this.averRating = facility.getAverRating();

        if(facility.getLocation() != null){
            this.latitude = facility.getLocation().getY();
            this.longitude = facility.getLocation().getX();
        }
    }

}
