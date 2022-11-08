package top.iseason.cmsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("大赛管理系统 - 在线API接口文档")
                        .description("by Iseason")
                        .version("1.0.0")
                        .build())
                .groupName("all")
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.iseason.cmsystem.controller"))
                .paths(PathSelectors.any())
                .build();
    }

}