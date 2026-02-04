package cc.restfulmc.api.config;

import cc.restfulmc.api.common.RequestTimingFilter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The configuration for the app.
 *
 * @author Braydon
 */
@Configuration @Getter
public class AppConfig {
    public static AppConfig INSTANCE;

    @Value("${server.publicUrl}")
    private String serverPublicUrl;

    /**
     * The build properties of the
     * app, null if the app is not built.
     */
    private final BuildProperties buildProperties;

    @Autowired
    public AppConfig(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @PostConstruct
    public void onInitialize() {
        INSTANCE = this;
    }

    /**
     * Define the OpenAI specification for this app.
     *
     * @return the specification
     */
    @Bean @NonNull
    public OpenAPI defineOpenAPI() {
        Info info = new Info();
        info.setTitle("RESTfulMC");
        info.setVersion(buildProperties == null ? "N/A" : buildProperties.getVersion());
        info.setDescription(buildProperties == null ? "N/A" : buildProperties.get("description"));
        info.setContact(new Contact().name("Braydon (Rainnny)").url("https://rainnny.club").email("braydonrainnny@gmail.com"));
        info.setLicense(new License().name("MIT License").url("https://opensource.org/licenses/MIT"));

        return new OpenAPI()
                .info(info)
                .addServersItem(new Server().url(serverPublicUrl).description("The public server URL"));
    }

    /**
     * Configure CORS for the app.
     *
     * @return the WebMvc config
     */
    @Bean @NonNull
    public WebMvcConfigurer configureCors() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                // Allow all origins to access the API
                registry.addMapping("/**")
                        .allowedOrigins("*") // Allow all origins
                        .allowedMethods("*") // Allow all methods
                        .allowedHeaders("*"); // Allow all headers
            }
        };
    }

    @Bean
    public FilterRegistrationBean<RequestTimingFilter> requestTimingFilter() {
        FilterRegistrationBean<RequestTimingFilter> filterRegistrationBean = new FilterRegistrationBean<>(new RequestTimingFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setName("requestTimingFilter");
        return filterRegistrationBean;
    }
}