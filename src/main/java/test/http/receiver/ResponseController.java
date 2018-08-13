package test.http.receiver;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import test.domain.MyResponse;
import test.service.ResponseService;

import java.util.Map;

@RestController
@RequestMapping("/response")
@Log4j2
public class ResponseController {

    @Autowired
    private ResponseService responseService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, MyResponse> getAll() {
        return responseService.getAll();
    }

    @PostMapping("/add/{method}")
    @ResponseStatus(HttpStatus.OK)
    public void add(MyResponse response, @PathVariable("method") String method) {
        log.info("adding " + response + "\t\tmethod " + method);
        responseService.addResponse(method, response);
    }

    @DeleteMapping("/delete/{method}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("method") String method) {
        responseService.deleteResponse(method);
    }
}
