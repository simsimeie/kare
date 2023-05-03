package com.example.kare;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ServletComponentScan
public class KareApplication {

    public static void main(String[] args) {
        SpringApplication.run(KareApplication.class, args);
    }

    @Component
    static class runner implements ApplicationRunner{
        private final Environment env;

        runner(Environment env) {
            this.env = env;
        }

        @Override
        public void run(ApplicationArguments args) throws Exception {
            env.getProperty("my.name");
        }
    }

}
