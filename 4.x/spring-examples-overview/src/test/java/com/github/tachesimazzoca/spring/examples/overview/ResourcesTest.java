package com.github.tachesimazzoca.spring.examples.overview;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

public class ResourcesTest {
    @Test
    public void testByteArrayResource() throws IOException {
        byte[] bytes = "Hello ByteArrayResource".getBytes();
        Resource res = new ByteArrayResource(bytes);
        assertEquals(bytes.length, res.contentLength());
    }

    @Test
    public void testClassPathResource() throws IOException {
        ApplicationContext ctx = new StaticApplicationContext();
        Resource res = ctx.getResource("classpath:/config.properties");
        Properties props = new Properties();
        props.load(res.getInputStream());
        assertEquals("http://www.example.net", props.get("url.home"));
    }

    @Test
    public void testFileSystemResource() throws IOException {
        ApplicationContext ctx = new StaticApplicationContext();
        String path = this.getClass().getResource("/config.properties").getPath();
        Resource res = ctx.getResource("file://" + path);
        Properties props = new Properties();
        props.load(res.getInputStream());
        assertEquals("/spring-examples-overview", props.get("url.base"));
    }
}
