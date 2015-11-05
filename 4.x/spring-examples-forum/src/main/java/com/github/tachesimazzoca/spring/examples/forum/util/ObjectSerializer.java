package com.github.tachesimazzoca.spring.examples.forum.util;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectSerializer {
    public interface Serializer {
        String serialize(Object object);

        <T> T deserialize(String ser, Class<T> type);
    }

    public static final Serializer BASE64 = new Serializer() {
        @Override
        public String serialize(Object obj) {
            String ser = null;
            ByteArrayOutputStream os = null;
            ObjectOutputStream oos = null;
            try {
                os = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(os);
                oos.writeObject(obj);
                ser = Base64.encodeBase64String(os.toByteArray());
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            } finally {
                closeQuietly(oos);
                closeQuietly(os);
            }
            return ser;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T deserialize(String ser, Class<T> type) {
            T obj = null;
            ByteArrayInputStream is = null;
            ObjectInputStream ois = null;
            try {
                is = new ByteArrayInputStream(Base64.decodeBase64(ser));
                ois = new ObjectInputStream(is);
                obj = (T) ois.readObject();
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            } finally {
                closeQuietly(ois);
                closeQuietly(is);
            }
            return obj;
        }
    };

    private static void closeQuietly(Closeable o) {
        try {
            if (o != null) o.close();
        } catch (Exception e) {
        }
    }
}
