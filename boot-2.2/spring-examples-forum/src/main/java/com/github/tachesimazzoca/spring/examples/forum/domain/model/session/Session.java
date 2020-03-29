package com.github.tachesimazzoca.spring.examples.forum.domain.model.session;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

@Data
public class Session {

    private static final String ENCODING = "UTF-8";

    private final String sessionId;

    private final Map<String, String> valueMap;

    private Session(String sessionId, Map<String, String> valueMap) {
        this.sessionId = sessionId;
        this.valueMap = valueMap;
    }

    public static Session create() {
        return new Session(UUID.randomUUID().toString(), new LinkedHashMap<String, String>());
    }

    public static Session create(String sessionId) {
        return new Session(sessionId, new LinkedHashMap<String, String>());
    }

    public String encodeValueMap() {
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : valueMap.entrySet()) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(URLEncoder.encode(entry.getKey(), ENCODING));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(), ENCODING));
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void decodeValueMap(String str) {
        try {
            String[] pairs = str.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (2 != kv.length)
                    continue;
                valueMap.put(URLDecoder.decode(kv[0], ENCODING),
                        URLDecoder.decode(kv[1], ENCODING));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
