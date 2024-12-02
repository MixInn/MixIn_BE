/*
package com.sparta.mixin.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.awt.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.path}")
    private String uploadFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry
                .addResourceHandler("/uploads/images/**") // /upload/** 패턴으로 요청이 오면
                .addResourceLocations("file:///" + uploadFolder + "/") // 파일 시스템 내 실제 경로로 매핑
                .setCachePeriod(3600) // 1시간 캐싱
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}*/
