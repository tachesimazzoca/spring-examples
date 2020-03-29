package com.github.tachesimazzoca.spring.examples.forum.infrastructure.messaging.amqp;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.qpid.server.SystemLauncher;
import org.apache.qpid.server.model.SystemConfig;

public class EmbeddedQpidBroker {

    private static final String DEFAULT_CONFIG_NAME = "embedded-qpid-broker.json";
    
    private final Map<String, Object> attributes;

    private final SystemLauncher launcher;

    private boolean started = false;

    public EmbeddedQpidBroker() {
        this(EmbeddedQpidBroker.class.getClassLoader().getResource(DEFAULT_CONFIG_NAME));
    }

    private EmbeddedQpidBroker(URL configURL) {
        attributes = new HashMap<>();
        attributes.put(SystemConfig.TYPE, "Memory");
        attributes.put(SystemConfig.INITIAL_CONFIGURATION_LOCATION, configURL.toExternalForm());
        attributes.put(SystemConfig.STARTUP_LOGGED_TO_SYSTEM_OUT, true);
        launcher = new SystemLauncher();
    }

    public synchronized void startup() throws Exception {
        if (started) {
            throw new IllegalStateException("SystemLauncher already started");
        }
        started = true;
        launcher.startup(attributes);
    }

    public synchronized void shutdown() {
        launcher.shutdown();
        started = false;
    }
}
