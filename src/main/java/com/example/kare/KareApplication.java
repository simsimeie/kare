package com.example.kare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ServletComponentScan
public class KareApplication {

    public static void main(String[] args) {
        SpringApplication.run(KareApplication.class, args);
    }

}
