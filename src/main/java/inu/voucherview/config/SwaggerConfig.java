package inu.voucherview.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Voucherview API")
                        .description("스포츠 바우처 시설/강좌 정보 조회 API 명세서")
                        .version("v1.0.0"));
    }
}
