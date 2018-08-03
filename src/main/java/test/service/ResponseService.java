package test.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseService {

    public static final String ALL = "all";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    private Map<String, String> responses = new HashMap<>();

    public void addResponse(String method, String body){
        responses.put(method, body);
    }

    public void addResponse(String body){
        responses.put(ALL, body);
    }

    public void deleteResponse(String method){
        responses.remove(method);
    }

    public String get(String method) {
        String s = responses.get(method);
        if(s == null) {
            s = responses.get(ALL);
        }

        if (responses ==null) {
            s = "{\"message\":\"HELLO WORLD\"}";
        }

        return s;
    }

}
