package com.github.tachesimazzoca.spring.examples.overview;

import com.github.tachesimazzoca.spring.examples.overview.database.JDBCConnectionPool;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class PropertyResourceConfigurerTest {
    private Properties createPropertiesByClassPath(String path) throws IOException {
        Properties props = new Properties();
        props.load(this.getClass().getResourceAsStream(path));
        return props;
    }

    @Test
    public void testPlaceholderTest() throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/database.xml");
        JDBCConnectionPool mcf = context.getBean("connectionFactory", JDBCConnectionPool.class);
        Properties props = createPropertiesByClassPath("/jdbc.properties");
        assertEquals(props.get("default.driver"), mcf.getDriver());
        assertEquals(props.get("default.url"), mcf.getUrl());
        assertEquals(props.get("default.user"), mcf.getUser());
        assertEquals(props.get("default.password"), mcf.getPassword());
    }
}
