package com.wanted.teamr.snsfeedintegration.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SpringDocConfig {

    private final SpringDocConfigProperties springDocConfigProperties;
    private final SwaggerUiConfigProperties swaggerUiConfigProperties;

    /**
     * swagger ui 웹 페이지 주소: /swagger-ui/index.html
     * <p>
     * swagger ui 웹 페이지 리소스(js, css 등) 주소: /swagger-ui/**
     *
     * @return swagger-ui 웹 페이지, 웹 페이지를 위한 리소스 모두를 포함하는 주소 반환
     */
    @Bean
    public String swaggerUiPath() {
        return swaggerUiConfigProperties.getPath() + "/**";
    }

    /**
     * api doc 주소: configuraiton file의 {springdoc.api-docs.path}
     * <p>
     * swagger-ui에서 요청하는 리소스(swagger-config 등) 주소: {springdoc.api-docs.path}/**
     *
     * @return api doc 주소와 그 외 swagger-ui에서 요청하는 리소스 모두를 포함하는 주소 반환
     */
    @Bean
    public String apiDocsPath() {
        return springDocConfigProperties.getApiDocs().getPath() + "/**";
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("소셜 미디어 통합 Feed 서비스 API")
                .description("본 서비스는 여러 소셜 미디어의 Feed를 통합 제공하는 웹 서비스입니다.</br>" +
                        "사용자는 SNS API(가상)을 통해 해시태그를 기반으로 Feed 조회할 수 있습니다.")
                .version("1.0.0");
    }

}
