package inu.voucherview.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Data
@NoArgsConstructor
public class Facility {
    private Long facilityId;
    private String name;
    private String address;
    private String phoneNumber;
    private String mainSport;

    private Point location;
    private Double averRating;
}
