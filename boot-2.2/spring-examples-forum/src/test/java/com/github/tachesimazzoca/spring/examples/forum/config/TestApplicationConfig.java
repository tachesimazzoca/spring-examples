package com.github.tachesimazzoca.spring.examples.forum.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = {"com.github.tachesimazzoca.spring.examples.forum"})
public class TestApplicationConfig {
}
