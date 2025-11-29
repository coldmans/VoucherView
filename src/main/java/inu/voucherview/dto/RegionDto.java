package inu.voucherview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegionDto {
    private String province;           // 시/도 (서울, 경기, 부산 등)
    private List<String> cities;       // 해당 시/도의 구/시 목록
}
