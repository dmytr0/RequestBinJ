package com.dmytr0.requestbin.service;

import com.dmytr0.requestbin.domain.MyResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Service
public class ResponseService {

    public static final String ALL = "ALL";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final MyResponse DEFAULT_RESP = new MyResponse("{\"answer\":\"OK\"}");
    private Map<String, MyResponse> responses;

    public ResponseService() {
        responses = new HashMap<>();
        responses.put(ALL, DEFAULT_RESP);
    }

    public void addResponse(String method, MyResponse response) {
        responses.put(method, response);
    }

    public void deleteResponse(String method) {
        responses.remove(method);
    }

    public MyResponse get(String method) {
        MyResponse resp = responses.get(method);
        if (resp == null) {
            resp = responses.get(ALL);
        }
        if (resp == null) {
            resp = DEFAULT_RESP;
        }
        return resp;
    }

    public Map<String, MyResponse> getAll() {
        return responses;

    }
}
