package com.dmytr0.requestbin.http.receiver;

import com.dmytr0.requestbin.domain.MyRequestEntity;
import com.dmytr0.requestbin.domain.StatsMetric;
import com.dmytr0.requestbin.service.MainService;
import com.dmytr0.requestbin.service.ResponseService;
import com.dmytr0.requestbin.utils.Metrics;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@Log4j2
public class MainController {

    private static final String RANDOMDELAY = "randomdelay";
    private static final String DELAY = "delay";

    @Autowired
    private MainService service;

    @Autowired
    private ResponseService responseService;

    private Metrics metrics;
    private Metrics getMetrics;
    private Metrics postMetrics;
    private Metrics putMetrics;
    private Metrics deleteMetrics;


    @Autowired
    public MainController(Jedis jedis) {
        metrics = new Metrics(jedis, "COMMON_METRIC");
        getMetrics = new Metrics(jedis, "GET_METRIC");
        postMetrics = new Metrics(jedis, "POST_METRIC");
        putMetrics = new Metrics(jedis, "PUT_METRIC");
        deleteMetrics = new Metrics(jedis, "DELETE_METRIC");
    }

    @GetMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity testGet(@RequestBody(required = false) String request,
                                  @RequestHeader(required = false) Map<String, String> headers,
                                  @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        metrics.add();
        getMetrics.add();
        String method = ResponseService.GET;
        service.add(method, request, headers, params);

        log.info("Ok " + method);

        processDelay(params);
        return responseService.get(method).entity();
    }

    @PostMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity testPost(@RequestBody(required = false) String request,
                                   @RequestHeader(required = false) Map<String, String> headers,
                                   @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        metrics.add();
        postMetrics.add();
        String method = ResponseService.POST;
        service.add(method, request, headers, params);

        log.info("Ok " + method);
        processDelay(params);
        return responseService.get(method).entity();
    }


    @PutMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity testPut(@RequestBody(required = false) String request,
                                  @RequestHeader(required = false) Map<String, String> headers,
                                  @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        metrics.add();
        putMetrics.add();
        String method = ResponseService.PUT;
        service.add(method, request, headers, params);

        log.info("Ok " + method);
        processDelay(params);
        return responseService.get(method).entity();
    }


    @DeleteMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity testDelete(@RequestBody(required = false) String request,
                                     @RequestHeader(required = false) Map<String, String> headers,
                                     @RequestParam(required = false) Map<String, String> params) throws InterruptedException {


        metrics.add();
        deleteMetrics.add();
        String method = ResponseService.DELETE;
        service.add(method, request, headers, params);
        log.info("Ok " + method);
        processDelay(params);
        return responseService.get(method).entity();
    }


    @GetMapping(value = "/api/listrequests")
    @ResponseStatus(HttpStatus.OK)
    public List<MyRequestEntity> getListRequests() {
        List<MyRequestEntity> allRequests = service.getAllRequests();
        log.info("Request all requests. Found " + allRequests.size());
        return allRequests;
    }

    @GetMapping(value = "/api/request/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MyRequestEntity getRequest(@PathVariable("id") String id) {
        return service.getRequest(id);
    }

    @GetMapping(value = "/reset")
    public void removeAll(HttpServletResponse response) throws IOException {
        service.removeAll();
        metrics.clear();
        response.sendRedirect("/");
    }

    @GetMapping(value = "/metrics")
    public List<StatsMetric> getMetrics(@RequestParam(required = false, value = "start") String start,
                                        @RequestParam(required = false, value = "finish") String finish,
                                        @RequestParam(required = false, value = "ignoreZero") String ignoreZero,
                                        @RequestParam(required = false, value = "type") String type) {

        return Arrays.asList(metrics.handleRateRequest(start, finish, ignoreZero, type),
                getMetrics.handleRateRequest(start, finish, ignoreZero, type),
                postMetrics.handleRateRequest(start, finish, ignoreZero, type),
                putMetrics.handleRateRequest(start, finish, ignoreZero, type),
                deleteMetrics.handleRateRequest(start, finish, ignoreZero, type));
    }

    private void processDelay(Map<String, String> params) throws InterruptedException {
        long delayMillis = 1;
        if (params.get(DELAY) != null && !params.get(DELAY).isEmpty()) {
            delayMillis = Long.valueOf(params.get(DELAY));
            log.info("delay: " + delayMillis);
            Thread.sleep(delayMillis);
        }
        if (params.get(RANDOMDELAY) != null && !params.get(RANDOMDELAY).isEmpty()) {
            Integer randomDelay = Integer.valueOf(params.get(RANDOMDELAY));
            Random random = new Random();
            delayMillis = random.nextInt(randomDelay + 1);
            log.info("delay random: " + delayMillis);
            Thread.sleep(delayMillis);
        }
    }

    @ExceptionHandler(Exception.class)
    public String error(Exception e) {
        log.error(e.getMessage(), e);
        return "Error!";
    }
}
