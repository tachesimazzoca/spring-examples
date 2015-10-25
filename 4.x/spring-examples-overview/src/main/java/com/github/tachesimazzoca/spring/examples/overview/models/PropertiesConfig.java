package com.github.tachesimazzoca.spring.examples.overview.models;

import java.util.Properties;

public class PropertiesConfig implements Config {
    private final Properties props;

    public PropertiesConfig(Properties props) {
        this.props = props;
    }

    public String get(String key) {
        return props.getProperty(key);
    }
}
