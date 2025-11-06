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
    private String phoneNumber;
    private String mainSport;

    private Point location;
    private Double averRating;

    public void changeAverRating(Double averRating){
        this.averRating = averRating;
    }
}
