package com.lms.lms;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class LmsApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();

        SpringApplication app = new SpringApplication(LmsApplication.class);
        app.setDefaultProperties(Map.of(
            "spring.data.mongodb.uri", dotenv.get("MONGO_URI"),
            "jwt.secret", dotenv.get("JWT_SECRET")  // if you need it
        ));
        app.run(args);
    }
}

