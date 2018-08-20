package com.dmytr0.requestbin.service;

import com.dmytr0.requestbin.domain.MyRequestEntity;
import com.dmytr0.requestbin.repository.RequestsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.dmytr0.requestbin.utils.CustomCollector.lastN;

@Log4j2
@Service
public class MainService {

    @Autowired
    private RequestsRepository repository;

    @Value("${app.number.stored.result}")
    private int numberOfResults;

    @Async
    public void add(String method, String body, Map<String, String> headers, Map<String, String> params) {
        MyRequestEntity request = new MyRequestEntity(method, body, headers, params);
        log.info("Request will be saved: " + request);
        removeOld();
        repository.save(request);
    }

    private void removeOld() {
        repository.removeAllByTimeBefore(LocalDateTime.now().minusDays(1));
        long count = repository.count();
        if (count > numberOfResults) {
            log.warn(String.format("Number of stored requests more than %d. Will be cleaned...", numberOfResults));
            List<String> ids = repository.findAll(new Sort(Sort.Direction.ASC, "time"))
                    .stream().map(MyRequestEntity::get_id)
                    .collect(lastN(numberOfResults));
            repository.removeAllBy_idIsNotIn(ids);
        }
    }

    public List<MyRequestEntity> getAllRequests() {
        return repository.findAll(new Sort(Sort.Direction.DESC, "time"));
    }

    public MyRequestEntity getRequest(String id) {
        return repository.findById(id).orElse(null);
    }

    public void removeAll() {
        repository.deleteAll();
    }
}
