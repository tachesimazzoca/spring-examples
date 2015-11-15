package com.github.tachesimazzoca.spring.examples.forum.storage;

import org.apache.commons.io.IOUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MultiValueMapStorage implements Storage<MultiValueMap<String, String>> {
    private static final String ENCODING = "UTF-8";
    private final StorageEngine engine;
    private final String prefix;

    public MultiValueMapStorage(StorageEngine engine) {
        this.engine = engine;
        prefix = "";
    }

    public MultiValueMapStorage(StorageEngine engine, String prefix) {
        this.engine = engine;
        this.prefix = prefix;
    }

    @Override
    public String create(MultiValueMap<String, String> value) {
        String key = prefix + UUID.randomUUID().toString();
        write(key, value);
        return key;
    }

    @Override
    public Optional<MultiValueMap<String, String>> read(String key) {
        if (null == key)
            throw new NullPointerException("The parameter key must be not null.");

        return engine.read(key).map(MultiValueMapStorage::decode);
    }

    @Override
    public void write(String key, MultiValueMap<String, String> valueMap) {
        if (null == key)
            throw new NullPointerException("The parameter key must be not null.");

        engine.write(key, encode(valueMap));
    }

    @Override
    public void delete(String key) {
        if (null == key)
            throw new NullPointerException("The parameter key must be not null.");

        engine.delete(key);
    }

    private static InputStream encode(MultiValueMap<String, String> valueMap) {
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, List<String>> entry : valueMap.entrySet()) {
                for (String v : entry.getValue()) {
                    if (sb.length() > 0)
                        sb.append("&");
                    sb.append(URLEncoder.encode(entry.getKey(), ENCODING));
                    sb.append("=");
                    sb.append(URLEncoder.encode(v, ENCODING));
                }
            }
            return IOUtils.toInputStream(sb.toString(), ENCODING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static MultiValueMap<String, String> decode(InputStream input) {
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
        try {
            String str = IOUtils.toString(input, ENCODING);
            String[] pairs = str.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (2 != kv.length)
                    continue;
                valueMap.add(URLDecoder.decode(kv[0], ENCODING),
                        URLDecoder.decode(kv[1], ENCODING));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return valueMap;
    }
}
