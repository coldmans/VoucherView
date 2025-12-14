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
    List<Facility> findWithFilters(@Param("pagination") Pagination pagination,
                                   @Param("keyword") String keyword,
                                   @Param("ctNm") String ctNm,
                                   @Param("ctDetailNm") String ctDetailNm,
                                   @Param("mainSport") String mainSport,
                                   @Param("minRating") Double minRating,
                                   @Param("maxRating") Double maxRating,
                                   @Param("sortBy") String sortBy,
                                   @Param("lat") Double lat,
                                   @Param("lng") Double lng,
                                   @Param("radius") Integer radius);
    int countWithFilters(@Param("keyword") String keyword,
                         @Param("ctNm") String ctNm,
                         @Param("ctDetailNm") String ctDetailNm,
                         @Param("mainSport") String mainSport,
                         @Param("minRating") Double minRating,
                         @Param("maxRating") Double maxRating,
                         @Param("lat") Double lat,
                         @Param("lng") Double lng,
                         @Param("radius") Integer radius);

    // 메타데이터 조회
    List<String> getDistinctProvinces();  // 시/도 목록
    List<String> getDistinctCities(@Param("province") String province);  // 특정 시/도의 구/시 목록
    List<String> getDistinctSports();
    List<Facility> getFavorite3Facilities();
}
