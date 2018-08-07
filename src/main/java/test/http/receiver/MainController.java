package test.http.receiver;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.domain.MyRequestEntity;
import test.service.MainService;
import test.service.ResponseService;

import java.util.List;
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
    public ResponseEntity testGet(@RequestBody(required = false) String request,
                                  @RequestHeader(required = false) Map<String, String> headers,
                                  @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        String method = ResponseService.GET;
        service.add(method, request, headers, params);

        log.info("Ok " + method);
        return responseService.get(method).entity();
    }

    @PostMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity testPost(@RequestBody(required = false) String request,
                                   @RequestHeader(required = false) Map<String, String> headers,
                                   @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        String method = ResponseService.POST;
        service.add(method, request, headers, params);

        log.info("Ok " + method);
        return responseService.get(method).entity();
    }


    @PutMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity testPut(@RequestBody(required = false) String request,
                                  @RequestHeader(required = false) Map<String, String> headers,
                                  @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        String method = ResponseService.PUT;
        service.add(method, request, headers, params);

        log.info("Ok " + method);
        return responseService.get(method).entity();
    }


    @DeleteMapping(value = "/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity testDelete(@RequestBody(required = false) String request,
                                     @RequestHeader(required = false) Map<String, String> headers,
                                     @RequestParam(required = false) Map<String, String> params) throws InterruptedException {

        String method = ResponseService.DELETE;
        service.add(method, request, headers, params);
        log.info("Ok " + method);
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
    @ResponseStatus(HttpStatus.OK)
    public String removeAll() {
        service.removeAll();
        return "OK";
    }
}
