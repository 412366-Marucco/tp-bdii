package utn.frc.tp_bdii.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import utn.frc.tp_bdii.filters.JwtFilter;
import utn.frc.tp_bdii.services.JwtService;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilter(JwtService jwtService) {
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtFilter(jwtService));
        registration.addUrlPatterns("/api/*");
        return registration;
    }
}
