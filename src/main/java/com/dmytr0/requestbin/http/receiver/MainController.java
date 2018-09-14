package com.dmytr0.requestbin.http.receiver;

import com.dmytr0.requestbin.domain.MyRequestEntity;
import com.dmytr0.requestbin.domain.StatsMetric;
import com.dmytr0.requestbin.enums.HttpMethod;
import com.dmytr0.requestbin.service.MainService;
import com.dmytr0.requestbin.service.MetricsService;
import com.dmytr0.requestbin.service.ResponseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.dmytr0.requestbin.enums.HttpMethod.*;

@RestController
@Log4j2
public class MainController {

    private static final String RANDOMDELAY = "randomdelay";
    private static final String DELAY = "delay";

    @Autowired
    private MainService service;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private MetricsService metricsService;


    @GetMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity testGet(@RequestBody(required = false) String request,
                                  @RequestHeader(required = false) Map<String, String> headers,
                                  @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        return processRequest(request, headers, params, GET);
    }

    @PostMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity testPost(@RequestBody(required = false) String request,
                                   @RequestHeader(required = false) Map<String, String> headers,
                                   @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        return processRequest(request, headers, params, POST);
    }


    @PutMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity testPut(@RequestBody(required = false) String request,
                                  @RequestHeader(required = false) Map<String, String> headers,
                                  @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        return processRequest(request, headers, params, PUT);
    }


    @DeleteMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity testDelete(@RequestBody(required = false) String request,
                                     @RequestHeader(required = false) Map<String, String> headers,
                                     @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        return processRequest(request, headers, params, DELETE);
    }

    private ResponseEntity processRequest(String request,
                                          Map<String, String> headers,
                                          Map<String, String> params,
                                          HttpMethod method) throws InterruptedException {


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

        response.sendRedirect("/");
    }

    @GetMapping(value = "/metrics")
    public List<StatsMetric> getMetrics(@RequestParam(required = false, value = "start") String start,
                                        @RequestParam(required = false, value = "finish") String finish,
                                        @RequestParam(required = false, value = "ignoreZero") String ignoreZero,
                                        @RequestParam(required = false, value = "type") String type) {

        log.info(String.format("Get metric request start: %s\tfinish: %s\tignoreZero:%s\ttype:%s", start, finish, ignoreZero, type));
        return metricsService.handleRateRequest(start, finish, ignoreZero, type);
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
