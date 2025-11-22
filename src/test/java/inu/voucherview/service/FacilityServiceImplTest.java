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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacilityServiceImplTest {
    @Mock
    private FacilityMapper facilityMapper;

    @InjectMocks
    private FacilityServiceImpl facilityService;

    @Test
    @DisplayName("시설 목록 조회(정상, 페이지네이션 포함)")
    void getFacilityList_Success(){
        int page = 1;
        int limit = 10;
        int fakeTotalCount = 50;

        List<Facility> fakeList = new ArrayList<>();
        Facility f = Facility.of(1L, "테스트시설");
        fakeList.add(f);

        // if facilityMapper.countAll() 호출 시 fakeTotalCount(50) 반환
        when(facilityMapper.countAll()).thenReturn(fakeTotalCount);

        // if facilityMapper.findAll(Pagination 객체 아무거나)가 호출되면 fakeList를 반환
        when(facilityMapper.findAll(any(Pagination.class))).thenReturn(fakeList);

        // [When]- 실행 (진짜 서비스 로직 호출)
        FacilityListResponse result = facilityService.getFacilityList(page, limit);

        assertThat(result).isNotNull();
        assertThat(result.getFacilityList()).isEqualTo(fakeList);
        assertThat(result.getPagination()).isInstanceOf(Pagination.class);

        Pagination resultPagination = result.getPagination();
        assertThat(resultPagination.getTotalCount()).isEqualTo(fakeTotalCount);
        assertThat(resultPagination.getTotalPages()).isEqualTo(5);

        verify(facilityMapper, times(1)).countAll();
        verify(facilityMapper, times(1)).findAll(any(Pagination.class));
    }

    @Test
    @DisplayName("시설 목록 조회(0건, 빈 목록)")
    void getFacilityList_ShouldReturnEmptyList_WhenNoFacilitiesExist(){
        int page = 1;
        int limit = 10;

        // 빈 리스트
        List<Facility> fakeEmptyList = new ArrayList<>();
        when(facilityMapper.countAll()).thenReturn(0);
        when(facilityMapper.findAll(any(Pagination.class))).thenReturn(fakeEmptyList);

        FacilityListResponse result = facilityService.getFacilityList(page, limit);
        assertThat(result).isNotNull();

        assertThat(result.getFacilityList()).isNotNull();
        assertThat(result.getFacilityList()).isEqualTo(fakeEmptyList);

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

        assertThat(result).isEqualTo(facility);
        verify(facilityMapper, times(1)).findById(facilityId);
    }

    @Test
    @DisplayName("시설 목록 조회(실패, 잘못된 입력)")
    void getFacilityList_ShouldThrowException_WhenInvalidInput(){
        assertThatThrownBy(() -> facilityService.getFacilityList(0, 10))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
        assertThatThrownBy(() -> facilityService.getFacilityList(1, 0))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_INPUT);
    }

}
