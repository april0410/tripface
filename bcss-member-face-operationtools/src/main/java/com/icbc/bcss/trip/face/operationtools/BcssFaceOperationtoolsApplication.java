package com.icbc.bcss.trip.face.operationtools;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableAsync  //
@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages = "com.icbc.bcss.trip.face.operationtools.dao")
public class BcssFaceOperationtoolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BcssFaceOperationtoolsApplication.class, args);
    }

}
