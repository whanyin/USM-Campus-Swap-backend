package com.cmt322.usmsecondhand.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 1. 允许本地开发环境
        config.addAllowedOriginPattern("http://localhost:5173");

        // 2. 允许你现在的 AWS 生产域名 (必须添加)
        config.addAllowedOriginPattern("https://usm-compus-swap.duckdns.org");

        // 3. 配置安全凭证与头信息
        config.setAllowCredentials(true); // 必须为 true 以配合 Redis 存储 Session/Token
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}