package com.application.blank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${media.location.base}")
    private String mediaBase;  // p.ej. "mediafiles"

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cualquier petición a /mediafiles/** se sirve desde el disco en mediaBase/
        registry.addResourceHandler("/mediafiles/**")
                .addResourceLocations("file:" + mediaBase + "/");
    }
}

