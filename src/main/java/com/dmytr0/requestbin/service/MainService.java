package com.dmytr0.requestbin.service;

import com.dmytr0.requestbin.domain.MyRequestEntity;
import com.dmytr0.requestbin.enums.HttpMethod;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class MainService {

    @Autowired
    private StoreService storeService;

    @Autowired
    private MetricsService metricsService;

    @Value("${app.number.stored.result}")
    private int numberOfResults;

    @Async("task-executor")
    public void add(HttpMethod method, String body, Map<String, String> headers, Map<String, String> params) {
        MyRequestEntity request = new MyRequestEntity(method, body, headers, params);
        log.info("Request will be saved: " + request);
        storeService.save(request);
        metricsService.add(method);
        removeOld();
    }

    private void removeOld() {
        storeService.removeOld(numberOfResults);
        metricsService.removeOld(numberOfResults);
    }


    public List<MyRequestEntity> getAllRequests() {
        return storeService.getAllRequests();
    }

    public MyRequestEntity getRequest(String id) {
        return storeService.getRequest(id);
    }

    public void removeAll() {
        metricsService.clear();
        storeService.removeAll();
    }


}
