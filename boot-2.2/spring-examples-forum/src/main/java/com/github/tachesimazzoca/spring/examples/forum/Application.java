package com.github.tachesimazzoca.spring.examples.forum;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.github.tachesimazzoca.spring.examples.forum.application.AccountService;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    @Bean
//    public CommandLineRunner command(AccountService accountService) {
//        return (args) -> {
//            accountService.attempt("foo@example.net", "changeme");
//        };
//    }
}
