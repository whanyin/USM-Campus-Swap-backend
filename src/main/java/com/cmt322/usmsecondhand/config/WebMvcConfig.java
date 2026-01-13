package com.cmt322.usmsecondhand.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射路径：/images/** -> 磁盘路径
        // file: 是必须的，表示文件系统路径
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadDir);
    }
}