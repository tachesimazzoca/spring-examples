package com.github.tachesimazzoca.spring.examples.forum;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Setup {
    public static void main(String[] args) {
        // Just execute SQLs declared in jdbc:initialize-database.
        new ClassPathXmlApplicationContext("spring/setup.xml");
    }
}
