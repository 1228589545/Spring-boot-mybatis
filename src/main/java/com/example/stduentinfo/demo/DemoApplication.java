package com.example.stduentinfo.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan ("com.example.stduentinfo.demo.mapper.*")
public class DemoApplication {

    public static void main( String[] args ) {
        SpringApplication.run( DemoApplication.class , args );
    }
}
