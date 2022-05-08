package com.ttnm.chillchatting.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/30/22
 * Time      : 10:05
 * Filename  : CorsConfig
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .exposedHeaders("Content-Disposition", "filename")
                .allowedMethods("PUT", "DELETE", "POST", "GET", "OPTIONS");
    }
}
