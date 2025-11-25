package inu.voucherview;

import inu.voucherview.mapper.CourseMapper;
import inu.voucherview.mapper.FacilityMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.mockito.Mockito;

@SpringBootTest(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")
class VoucherViewApplicationTests {

    @Test
    void contextLoads() {
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        FacilityMapper facilityMapper() {
            return Mockito.mock(FacilityMapper.class);
        }

        @Bean
        CourseMapper courseMapper() {
            return Mockito.mock(CourseMapper.class);
        }
    }
}
