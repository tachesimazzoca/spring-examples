package com.github.tachesimazzoca.spring.examples.forum.config;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

import java.io.File;

public class TypesafeConfig implements Config {
    private static final ConfigRenderOptions RENDER_OPTIONS =
            ConfigRenderOptions.defaults()
                    .setOriginComments(false)
                    .setComments(false);

    private final com.typesafe.config.Config typesafeConfig;

    private TypesafeConfig(com.typesafe.config.Config typesafeConfig) {
        this.typesafeConfig = typesafeConfig;
    }

    private TypesafeConfig(String name) {
        File f = new File(getClass().getResource(name).getPath());
        this.typesafeConfig = ConfigFactory.parseFile(f).resolve();
    }

    public static TypesafeConfig load() {
        return new TypesafeConfig(ConfigFactory.load().resolve());
    }

    public static TypesafeConfig load(String name) {
        return new TypesafeConfig(name);
    }

    public static TypesafeConfig parseFile(File path) {
        return new TypesafeConfig(ConfigFactory.parseFile(path));
    }

    public static TypesafeConfig parseString(String conf) {
        return new TypesafeConfig(ConfigFactory.parseString(conf));
    }

    @Override
    public Object get(String key) {
        return typesafeConfig.getAnyRef(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        return (T) typesafeConfig.getAnyRef(key);
    }
}
