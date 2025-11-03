package com.malpishon.gamertrials.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String baseDir = System.getProperty("user.home") + "/gamer-trials/uploads/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + baseDir);
    }
}
