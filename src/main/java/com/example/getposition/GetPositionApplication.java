package com.example.getposition;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.getposition.mapper")
public class GetPositionApplication {

    public static void main(String[] args) {
        SpringApplication.run(GetPositionApplication.class, args);
    }

}
