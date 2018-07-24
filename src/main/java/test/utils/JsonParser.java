package test.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public final class JsonParser {

    private static ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());


    public static String prepareObject(Object o) throws IOException {
        return mapper.writeValueAsString(o);
    }

    public static <T> T parseObject(String obj, Class<T> c) throws IOException {
        return mapper.readValue(obj, c);
    }

    private JsonParser() {
    }
}
