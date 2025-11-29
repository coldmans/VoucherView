package inu.voucherview.service;

import inu.voucherview.domain.Facility;
import inu.voucherview.dto.FacilityDto;
import inu.voucherview.exception.BusinessException;
import inu.voucherview.exception.ErrorCode;
import inu.voucherview.mapper.FacilityMapper;
import inu.voucherview.response.FacilityListResponse;
import inu.voucherview.util.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacilityServiceImplTest {
    @Mock
    private FacilityMapper facilityMapper;

    @InjectMocks
    private FacilityServiceImpl facilityService;

    @Test
    @DisplayName("시설 목록 조회(정상, 필터 없음)")
    void getFacilityList_Success_NoFilter(){
        int page = 1;
        int limit = 10;
        int fakeTotalCount = 50;

        List<Facility> fakeList = new ArrayList<>();
        Facility f = Facility.of(1L, "테스트시설");
        fakeList.add(f);

        // 필터 없어도 findWithFilters 사용
        when(facilityMapper.countWithFilters(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(fakeTotalCount);
        when(facilityMapper.findWithFilters(any(Pagination.class), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(fakeList);

        FacilityListResponse result = facilityService.getFacilityList(
                page, limit, null, null, null, null, null, null, null, null, null, null
        );

        assertThat(result).isNotNull();
        assertThat(result.getFacilityList()).hasSize(1);

        FacilityDto resultDto = result.getFacilityList().get(0);
        assertThat(resultDto.getFacilityId()).isEqualTo(f.getFacilityId());
        assertThat(resultDto.getName()).isEqualTo(f.getName());
        assertThat(result.getPagination()).isInstanceOf(Pagination.class);

        Pagination resultPagination = result.getPagination();
        assertThat(resultPagination.getTotalCount()).isEqualTo(fakeTotalCount);
        assertThat(resultPagination.getTotalPages()).isEqualTo(5);

        verify(facilityMapper, times(1)).countWithFilters(any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(facilityMapper, times(1)).findWithFilters(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("시설 목록 조회(정상, 키워드 검색)")
    void getFacilityList_Success_WithKeyword(){
        int page = 1;
        int limit = 10;
        String keyword = "수영";
        int fakeTotalCount = 5;

        List<Facility> fakeList = new ArrayList<>();
        Facility f = Facility.of(1L, "수영장");
        fakeList.add(f);

        when(facilityMapper.countWithFilters(eq(keyword), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(fakeTotalCount);
        when(facilityMapper.findWithFilters(any(Pagination.class), eq(keyword), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(fakeList);

        FacilityListResponse result = facilityService.getFacilityList(
                page, limit, keyword, null, null, null, null, null, null, null, null, null
        );

        assertThat(result).isNotNull();
        assertThat(result.getFacilityList()).hasSize(1);
        assertThat(result.getFacilityList().get(0).getName()).isEqualTo("수영장");

        verify(facilityMapper, times(1)).countWithFilters(eq(keyword), any(), any(), any(), any(), any(), any(), any(), any());
        verify(facilityMapper, times(1)).findWithFilters(any(), eq(keyword), any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(facilityMapper, never()).countAll();
        verify(facilityMapper, never()).findAll(any());
    }

    @Test
    @DisplayName("시설 목록 조회(정상, 지역 필터)")
    void getFacilityList_Success_WithRegionFilter(){
        int page = 1;
        int limit = 10;
        String ctNm = "서울";
        String ctDetailNm = "강남구";
        int fakeTotalCount = 3;

        List<Facility> fakeList = new ArrayList<>();
        Facility f = Facility.of(1L, "강남시설");
        fakeList.add(f);

        when(facilityMapper.countWithFilters(any(), eq(ctNm), eq(ctDetailNm), any(), any(), any(), any(), any(), any()))
                .thenReturn(fakeTotalCount);
        when(facilityMapper.findWithFilters(any(), any(), eq(ctNm), eq(ctDetailNm), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(fakeList);

        FacilityListResponse result = facilityService.getFacilityList(
                page, limit, null, ctNm, ctDetailNm, null, null, null, null, null, null, null
        );

        assertThat(result).isNotNull();
        assertThat(result.getFacilityList()).hasSize(1);

        verify(facilityMapper, times(1)).countWithFilters(any(), eq(ctNm), eq(ctDetailNm), any(), any(), any(), any(), any(), any());
        verify(facilityMapper, times(1)).findWithFilters(any(), any(), eq(ctNm), eq(ctDetailNm), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("시설 목록 조회(정상, 종목 필터)")
    void getFacilityList_Success_WithSportFilter(){
        int page = 1;
        int limit = 10;
        String mainSport = "수영";
        int fakeTotalCount = 7;

        List<Facility> fakeList = new ArrayList<>();
        Facility f = Facility.of(1L, "수영시설");
        fakeList.add(f);

        when(facilityMapper.countWithFilters(any(), any(), any(), eq(mainSport), any(), any(), any(), any(), any()))
                .thenReturn(fakeTotalCount);
        when(facilityMapper.findWithFilters(any(), any(), any(), any(), eq(mainSport), any(), any(), any(), any(), any(), any()))
                .thenReturn(fakeList);

        FacilityListResponse result = facilityService.getFacilityList(
                page, limit, null, null, null, mainSport, null, null, null, null, null, null
        );

        assertThat(result).isNotNull();
        assertThat(result.getFacilityList()).hasSize(1);

        verify(facilityMapper, times(1)).countWithFilters(any(), any(), any(), eq(mainSport), any(), any(), any(), any(), any());
        verify(facilityMapper, times(1)).findWithFilters(any(), any(), any(), any(), eq(mainSport), any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("시설 목록 조회(정상, 평점 범위 필터)")
    void getFacilityList_Success_WithRatingFilter(){
        int page = 1;
        int limit = 10;
        Double minRating = 4.0;
        Double maxRating = 5.0;
        int fakeTotalCount = 10;

        List<Facility> fakeList = new ArrayList<>();
        Facility f = Facility.of(1L, "고평점시설");
        fakeList.add(f);

        when(facilityMapper.countWithFilters(any(), any(), any(), any(), eq(minRating), eq(maxRating), any(), any(), any()))
                .thenReturn(fakeTotalCount);
        when(facilityMapper.findWithFilters(any(), any(), any(), any(), any(), eq(minRating), eq(maxRating), any(), any(), any(), any()))
                .thenReturn(fakeList);

        FacilityListResponse result = facilityService.getFacilityList(
                page, limit, null, null, null, null, minRating, maxRating, null, null, null, null
        );

        assertThat(result).isNotNull();
        assertThat(result.getFacilityList()).hasSize(1);

        verify(facilityMapper, times(1)).countWithFilters(any(), any(), any(), any(), eq(minRating), eq(maxRating), any(), any(), any());
        verify(facilityMapper, times(1)).findWithFilters(any(), any(), any(), any(), any(), eq(minRating), eq(maxRating), any(), any(), any(), any());
    }

    @Test
    @DisplayName("시설 목록 조회(정상, 거리 필터)")
    void getFacilityList_Success_WithDistanceFilter(){
        int page = 1;
        int limit = 10;
        Double lat = 37.5665;
        Double lng = 126.9780;
        Integer radius = 5000;
        int fakeTotalCount = 2;

        List<Facility> fakeList = new ArrayList<>();
        Facility f = Facility.of(1L, "근처시설");
        fakeList.add(f);

        when(facilityMapper.countWithFilters(any(), any(), any(), any(), any(), any(), eq(lat), eq(lng), eq(radius)))
                .thenReturn(fakeTotalCount);
        when(facilityMapper.findWithFilters(any(), any(), any(), any(), any(), any(), any(), any(), eq(lat), eq(lng), eq(radius)))
                .thenReturn(fakeList);

        FacilityListResponse result = facilityService.getFacilityList(
                page, limit, null, null, null, null, null, null, null, lat, lng, radius
        );

        assertThat(result).isNotNull();
        assertThat(result.getFacilityList()).hasSize(1);

        verify(facilityMapper, times(1)).countWithFilters(any(), any(), any(), any(), any(), any(), eq(lat), eq(lng), eq(radius));
        verify(facilityMapper, times(1)).findWithFilters(any(), any(), any(), any(), any(), any(), any(), any(), eq(lat), eq(lng), eq(radius));
    }

    @Test
    @DisplayName("시설 목록 조회(정상, 복합 필터)")
    void getFacilityList_Success_WithMultipleFilters(){
        int page = 1;
        int limit = 10;
        String keyword = "수영";
        String ctNm = "서울";
        Double minRating = 4.5;
        int fakeTotalCount = 1;

        List<Facility> fakeList = new ArrayList<>();
        Facility f = Facility.of(1L, "서울수영장");
        fakeList.add(f);

        when(facilityMapper.countWithFilters(eq(keyword), eq(ctNm), any(), any(), eq(minRating), any(), any(), any(), any()))
                .thenReturn(fakeTotalCount);
        when(facilityMapper.findWithFilters(any(), eq(keyword), eq(ctNm), any(), any(), eq(minRating), any(), any(), any(), any(), any()))
                .thenReturn(fakeList);

        FacilityListResponse result = facilityService.getFacilityList(
                page, limit, keyword, ctNm, null, null, minRating, null, null, null, null, null
        );

        assertThat(result).isNotNull();
        assertThat(result.getFacilityList()).hasSize(1);

        verify(facilityMapper, times(1)).countWithFilters(eq(keyword), eq(ctNm), any(), any(), eq(minRating), any(), any(), any(), any());
        verify(facilityMapper, times(1)).findWithFilters(any(), eq(keyword), eq(ctNm), any(), any(), eq(minRating), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("시설 목록 조회(0건, 빈 목록)")
    void getFacilityList_ShouldReturnEmptyList_WhenNoFacilitiesExist(){
        int page = 1;
        int limit = 10;

        List<Facility> fakeEmptyList = new ArrayList<>();
        when(facilityMapper.countWithFilters(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(0);
        when(facilityMapper.findWithFilters(any(Pagination.class), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(fakeEmptyList);

        FacilityListResponse result = facilityService.getFacilityList(
                page, limit, null, null, null, null, null, null, null, null, null, null
        );
        assertThat(result).isNotNull();

        assertThat(result.getFacilityList()).isNotNull();
        assertThat(result.getFacilityList()).isEmpty();

        Pagination p = result.getPagination();
        assertThat(p.getTotalPages()).isEqualTo(1);
        assertThat(p.getTotalCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("시설 단건 조회(실패, 존재하지 않는 ID)")
    void getFacilityById_ShouldThrowException_WhenFacilityNotFound(){
        Long nonExistId = 999L;
        when(facilityMapper.findById(nonExistId)).thenReturn(null);
        assertThatThrownBy(() -> {
            facilityService.getFacilityById(nonExistId);
        })
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FACILITY_NOT_FOUND);
        verify(facilityMapper, times(1)).findById(nonExistId);
    }

    @Test
    @DisplayName("시설 단건 조회(성공)")
    void getFacilityById_Success(){
        Long facilityId = 10L;
        Facility facility = Facility.of(facilityId, "테스트시설");
        when(facilityMapper.findById(facilityId)).thenReturn(facility);

        FacilityDto result = facilityService.getFacilityById(facilityId);

        assertThat(result.getFacilityId()).isEqualTo(facilityId);
        assertThat(result.getName()).isEqualTo(facility.getName());
        verify(facilityMapper, times(1)).findById(facilityId);
    }

    @Test
    @DisplayName("시설 목록 조회(실패, 잘못된 입력 - page)")
    void getFacilityList_ShouldThrowException_WhenInvalidPage(){
        assertThatThrownBy(() -> facilityService.getFacilityList(
                0, 10, null, null, null, null, null, null, null, null, null, null
        ))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("시설 목록 조회(실패, 잘못된 입력 - limit)")
    void getFacilityList_ShouldThrowException_WhenInvalidLimit(){
        assertThatThrownBy(() -> facilityService.getFacilityList(
                1, 0, null, null, null, null, null, null, null, null, null, null
        ))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("시설 목록 조회(실패, distance 정렬인데 좌표 없음)")
    void getFacilityList_ShouldThrowException_WhenDistanceSortWithoutCoordinates(){
        assertThatThrownBy(() -> facilityService.getFacilityList(
                1, 10, null, null, null, null, null, null, "distance", null, null, null
        ))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("시설 목록 조회(실패, distance 정렬인데 위도만 있음)")
    void getFacilityList_ShouldThrowException_WhenDistanceSortWithOnlyLatitude(){
        assertThatThrownBy(() -> facilityService.getFacilityList(
                1, 10, null, null, null, null, null, null, "distance", 37.5665, null, null
        ))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

    @Test
    @DisplayName("시설 목록 조회(실패, distance 정렬인데 경도만 있음)")
    void getFacilityList_ShouldThrowException_WhenDistanceSortWithOnlyLongitude(){
        assertThatThrownBy(() -> facilityService.getFacilityList(
                1, 10, null, null, null, null, null, null, "distance", null, 126.9780, null
        ))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }
}