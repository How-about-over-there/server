package com.haot.lodge.common.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        servers = {
                @Server(url = "http://localhost:19091", description = "Local Gateway URL")
        }
)
@Configuration
public class SwaggerConfig {

    @Value("${gateway.url}")
    private String gatewayUrl;

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .info(new Info()
                        .title("Haot-Lodge-Service")
                        .description("HAOT 숙소 서비스")
                        .version("v1"))
                .addServersItem(new io.swagger.v3.oas.models.servers.Server().url(gatewayUrl).description("Gateway URL"))
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement))
                .addServersItem(new io.swagger.v3.oas.models.servers.Server().url("/"));
    }

//    @Bean
//    public OpenAPI openAPI() {
//
//        return new OpenAPI()
//                .components(apiComponents())
//                .security(apiSecurity())
//                .info(apiInfo())
//                .addServersItem(new io.swagger.v3.oas.models.servers.Server().url("/"));
//    }
//
//    private Info apiInfo() {
//        return new Info()
//                .title("Haot-Lodge-Service")
//                .description("HAOT 숙소 서비스")
//                .version("v1");
//    }
//
//    private Components apiComponents() {
//        SecurityScheme securityScheme = new SecurityScheme()
//                .type(SecurityScheme.Type.HTTP)
//                .scheme("bearer")
//                .bearerFormat("JWT")
//                .in(SecurityScheme.In.HEADER)
//                .name("Authorization");
//        return new Components().addSecuritySchemes("bearerAuth", securityScheme);
//    }
//
//    private List<SecurityRequirement> apiSecurity() {
//        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");
//        return Arrays.asList(securityRequirement);
//    }

}
