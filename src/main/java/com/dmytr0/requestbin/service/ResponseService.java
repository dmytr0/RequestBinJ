package com.dmytr0.requestbin.service;

import com.dmytr0.requestbin.domain.MyResponse;
import com.dmytr0.requestbin.enums.HttpMethod;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.dmytr0.requestbin.enums.HttpMethod.ALL;

@Log4j2
@Service
public class ResponseService {


    private static final MyResponse DEFAULT_RESP = new MyResponse("{\"answer\":\"OK\"}");
    private Map<HttpMethod, MyResponse> responses;

    public ResponseService() {
        responses = new HashMap<>();
        responses.put(ALL, DEFAULT_RESP);
    }

    public void addResponse(HttpMethod method, MyResponse response) {
        responses.put(method, response);
    }

    public void deleteResponse(HttpMethod method) {
        responses.remove(method);
    }

    public MyResponse get(HttpMethod method) {
        MyResponse resp = responses.get(method);
        if (resp == null) {
            resp = responses.get(ALL);
        }
        if (resp == null) {
            resp = DEFAULT_RESP;
        }
        return resp;
    }

    public Map<HttpMethod, MyResponse> getAll() {
        return responses;

    }
}
