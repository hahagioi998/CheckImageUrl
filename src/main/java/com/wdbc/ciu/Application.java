package com.wdbc.ciu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Component
@SpringBootApplication
@EnableTransactionManagement//开启springboot事务的支持
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
