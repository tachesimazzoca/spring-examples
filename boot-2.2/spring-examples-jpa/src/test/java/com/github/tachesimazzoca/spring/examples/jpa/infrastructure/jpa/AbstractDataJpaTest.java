package com.github.tachesimazzoca.spring.examples.jpa.infrastructure.jpa;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.github.tachesimazzoca.spring.examples.jpa.config.TestApplicationConfig;

@ExtendWith(SpringExtension.class)
@Import(TestApplicationConfig.class)
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public abstract class AbstractDataJpaTest {
}
