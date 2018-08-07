package test.service;

import org.springframework.stereotype.Service;
import test.domain.MyResponse;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseService {

    public static final String ALL = "all";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    private Map<String, MyResponse> responses = new HashMap<>();

    public void addResponse(String method, MyResponse response){
        responses.put(method, response);
    }

    public void addResponse(MyResponse response){
        responses.put(ALL, response);
    }

    public void deleteResponse(String method){
        responses.remove(method);
    }

    public MyResponse get(String method) {
        MyResponse resp = responses.get(method);
        if(resp == null) {
            resp = responses.get(ALL);
        }

        if (responses ==null) {
            resp = new MyResponse("{\"answer\":\"OK\"}");
        }

        return resp;
    }

    public Map<String, MyResponse> getAll() {
        return responses;

    }
}
