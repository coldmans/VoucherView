package inu.voucherview.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode(of = "favoriteId")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FavoriteFacility {
    private Long favoriteId;
    private Long userId;
    private Long facilityId;
    private LocalDateTime createdAt;

    public static FavoriteFacility of(Long userId, Long facilityId) {
        return FavoriteFacility.builder()
                .userId(userId)
                .facilityId(facilityId)
                .build();
    }
}