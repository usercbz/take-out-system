package com.cbz.takeoutsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.cbz.takeoutsystem.mapper")
@EnableTransactionManagement
public class TakeOutSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TakeOutSystemApplication.class, args);
    }

}
