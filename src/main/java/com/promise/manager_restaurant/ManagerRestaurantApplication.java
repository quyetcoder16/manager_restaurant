package com.promise.manager_restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ManagerRestaurantApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerRestaurantApplication.class, args);
    }

}
