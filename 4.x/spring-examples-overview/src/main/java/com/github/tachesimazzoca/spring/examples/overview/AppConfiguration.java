package com.github.tachesimazzoca.spring.examples.overview;

import com.github.tachesimazzoca.spring.examples.overview.config.Config;
import com.github.tachesimazzoca.spring.examples.overview.config.ConfigFactory;
import com.github.tachesimazzoca.spring.examples.overview.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.overview.models.AccountService;
import com.github.tachesimazzoca.spring.examples.overview.models.MockAccountDao;
import com.github.tachesimazzoca.spring.examples.overview.models.MockAccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class AppConfiguration {
    @Bean
    public Config config() {
        return ConfigFactory.createConfig(new ClassPathResource("/config.properties"));
    }

    @Bean
    public AccountDao accountDao() {
        return new MockAccountDao();
    }

    @Bean
    public AccountService accountService() {
        return new MockAccountService(accountDao());
    }
}
