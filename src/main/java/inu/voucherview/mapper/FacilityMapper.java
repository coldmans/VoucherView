package inu.voucherview.mapper;

import inu.voucherview.domain.Facility;
import inu.voucherview.util.Pagination;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FacilityMapper {
    Facility findById(@Param("facilityId") Long facilityId);
    List<Facility> findAll(@Param("page") Pagination pagination);
    int countAll();
    void updateAverRating(@Param("facilityId") Long facilityId,
                          @Param("averRating") Double averRating);
}
