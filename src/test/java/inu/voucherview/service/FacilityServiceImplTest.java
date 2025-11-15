package inu.voucherview.service;

import inu.voucherview.domain.Facility;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacilityServiceImplTest {
    @Mock
    private FacilityMapper facilityMapper;

    @InjectMocks
    private FacilityServiceImpl facilityService;

    @Test
    @DisplayName("시설 목록 조회(정상, 페이지네이션 포함")
    void getFacilityList_Success(){
        int page = 1;
        int limit = 10;
        int fakeTotalCount = 50;

        List<Facility> fakeList = new ArrayList<>();
        Facility f = Facility.of(1L, "테스트시설");
        fakeList.add(f);

        // if facilityMapper.countAll() 호출 시 fackTotalCount(50) 반환
        when(facilityMapper.countAll()).thenReturn(fakeTotalCount);

        // if facilityMapper.findAll(Pagination 객체 아무거나)가 호출되면 fackList를 반환
        when(facilityMapper.findAll(any(Pagination.class))).thenReturn(fakeList);

        // [When]- 실행 (진짜 서비스 로직 호출)
        FacilityListResponse result = facilityService.getFacilityList(page, limit);

        assertThat(result).isNotNull();
        assertThat(result.getFacilityList()).isEqualTo(fakeList);
        assertThat(result.getPagination()).isInstanceOf(Pagination.class);

        Pagination resultPagination = (Pagination) result.getPagination();
        assertThat(resultPagination.getTotalCount()).isEqualTo(fakeTotalCount);
        assertThat(resultPagination.getTotalPages()).isEqualTo(5);

        verify(facilityMapper, times(1)).countAll();
        verify(facilityMapper, times(1)).findAll(any(Pagination.class));

    }
}
