package test.http.receiver;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import test.service.MainService;
import test.service.ResponseService;

import java.util.Map;


@RestController
@Log4j2
public class MainController {

    @Autowired
    private MainService service;

    @Autowired
    private ResponseService responseService;

    @GetMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public String testGet(@RequestBody(required = false) String request,
                          @RequestHeader(required = false) HttpHeaders headers,
                          @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        service.add(ResponseService.GET, request, headers, params);

        log.info("Ok");
        return responseService.get(ResponseService.GET);
    }

    @PostMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public String testPost(@RequestBody(required = false) String request,
                           @RequestHeader(required = false) HttpHeaders headers,
                           @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        service.add(ResponseService.POST, request, headers, params);

        log.info("Ok");
        return responseService.get(ResponseService.POST);
    }


    @PutMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public String testPut(@RequestBody(required = false) String request,
                          @RequestHeader(required = false) HttpHeaders headers,
                          @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        service.add(ResponseService.PUT, request, headers, params);

        log.info("Ok");
        return responseService.get(ResponseService.PUT);
    }


    @DeleteMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public String testDelete(@RequestBody(required = false) String request,
                             @RequestHeader(required = false) HttpHeaders headers,
                             @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        service.add(ResponseService.DELETE, request, headers, params);

        log.info("Ok");
        return responseService.get(ResponseService.DELETE);
    }


    @GetMapping(value = "/view", produces = "text/html")
    @ResponseStatus(HttpStatus.OK)
    public String getResult() {

        return service.getAll();

    }

    @GetMapping(value = "/reset")
    @ResponseStatus(HttpStatus.OK)
    public String removeAll() {

        service.removeAll();

        return "OK";
    }
}
