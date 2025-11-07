package inu.voucherview.mapper;

import inu.voucherview.domain.Facility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FacilityMapper {
    Facility findById(@Param("facilityId") Long facilityId);
    List<Facility> findAll();
    void updateAverRating(@Param("facilityId") Long facilityId,
                          @Param("averRating") Double averRating);
}
