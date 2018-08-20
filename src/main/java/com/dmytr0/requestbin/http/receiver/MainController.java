package com.dmytr0.requestbin.http.receiver;

import com.dmytr0.requestbin.domain.MyRequestEntity;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@Log4j2
public class MainController {

    @Autowired
    private MainService service;

    @Autowired
    private ResponseService responseService;

    private Metrics metrics;


    @Autowired
    public MainController(Jedis jedis) {
        metrics = new Metrics(jedis, "MAIN METRIC");
    }

    @GetMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity testGet(@RequestBody(required = false) String request,
                                  @RequestHeader(required = false) Map<String, String> headers,
                                  @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        metrics.add();
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
        String method = ResponseService.DELETE;
        service.add(method, request, headers, params);
        log.info("Ok " + method);
        processDelay(params);
        return responseService.get(method).entity();
    }


    @GetMapping(value = "/api/listrequests")
    @ResponseStatus(HttpStatus.OK)
    public List<MyRequestEntity> getListRequests() {
        return service.getAllRequests();
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

    @GetMapping(value = "/metrics", produces = "text/html; charset=utf-8")
    public String getMetrics() {
        LocalDateTime now = LocalDateTime.now();
        return metrics.getRatePerSecond(now.minusHours(1), now);
    }

    private void processDelay(Map<String, String> params) throws InterruptedException {
        long delayMillis = 1;
        if (params.get("delay") != null && !params.get("delay").isEmpty()) {
            delayMillis = Long.valueOf(params.get("delay"));
        }
        if (params.get("randomdelay") != null && !params.get("randomdelay").isEmpty()) {
            Integer randomDelay = Integer.valueOf(params.get("randomdelay"));
            Random random = new Random();
            delayMillis = random.nextInt(randomDelay + 1);
        }

        log.info("delay: " + delayMillis);
        Thread.sleep(delayMillis);
    }

    @ExceptionHandler(Exception.class)
    public String error(Exception e) {
        log.error(e.getMessage(), e);
        return Arrays.toString(e.getStackTrace());
    }
}
