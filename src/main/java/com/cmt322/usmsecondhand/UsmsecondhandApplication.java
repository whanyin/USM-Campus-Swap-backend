package com.cmt322.usmsecondhand;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.cmt322.usmsecondhand.mapper")
@EnableScheduling
public class UsmsecondhandApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsmsecondhandApplication.class, args);
    }

}
