package utn.frc.tp_bdii.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // permite todos los endpoints
                .allowedOrigins("*") // ⬅️ PERMITÍ TODOS LOS ORÍGENES
                .allowedMethods("*") // permite todos los métodos HTTP
                .allowedHeaders("*"); // permite todos los headers
    }
}
