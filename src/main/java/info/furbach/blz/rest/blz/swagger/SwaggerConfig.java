package info.furbach.blz.rest.blz.swagger;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "swagger.project.info", ignoreUnknownFields = false)
public class SwaggerConfig {

    @Getter
    @Setter
    private String title, version, description, environment;

    @Autowired
    private Environment env;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
//                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/api.*"))
                .paths(PathSelectors.any()) // for all
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                title,
                description
                        + "\n\n"
                        + getConfiguration(),
                version + " <" + environment + ">",
                "",
                new Contact("Referat", "", "andreas@furbach.info"),
                "© Andreas Furbach 2019",
                "",
                new ArrayList<VendorExtension>());
        return apiInfo;
    }

    private String getConfiguration() {
        return String.format("| key | value |\n" +
                        "| -------- | -------- |\n" +
                        "| Version | ```%s``` |\n" +
                        "| Profiles | ```%s``` |\n"
                , env.getProperty("swagger.project.info.version")
                , profiles()
        );
    }

    private String profiles() {
        return String.join(", ", env.getActiveProfiles());
    }
}
