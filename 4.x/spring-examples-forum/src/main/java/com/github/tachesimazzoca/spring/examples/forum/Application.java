package com.github.tachesimazzoca.spring.examples.forum;

import com.github.tachesimazzoca.spring.examples.forum.config.Config;
import com.github.tachesimazzoca.spring.examples.forum.config.TypesafeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Application {
    @Bean
    public Config config() {
        return TypesafeConfig.load();
    }
}
