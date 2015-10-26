package com.github.tachesimazzoca.spring.examples.overview.config;

import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigFactory {
    public static Config createConfig(Resource file) {
        Properties props = new Properties();
        try {
            props.load(file.getInputStream());
        } catch (FileNotFoundException e) {
            throw new Error(e);
        } catch (IOException e) {
            throw new Error(e);
        }
        return new PropertiesConfig(props);
    }
}
