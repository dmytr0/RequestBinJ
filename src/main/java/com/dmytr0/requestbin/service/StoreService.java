package com.dmytr0.requestbin.service;

import com.dmytr0.requestbin.domain.MyRequestEntity;
import com.dmytr0.requestbin.repository.RequestsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.dmytr0.requestbin.utils.CustomCollector.lastN;

@Log4j2
@Service
public class StoreService {

    @Autowired
    private RequestsRepository repository;


    void removeOld(int numberOfResults) {
        repository.removeAllByTimeBefore(LocalDateTime.now().minusDays(1));
        long count = repository.count();
        if (count >= numberOfResults) {
            log.warn(String.format("Number of stored requests more than %d. Will be cleaned...", numberOfResults));
            List<String> ids = repository.findAll(new Sort(Sort.Direction.ASC, "time"))
                    .stream().map(MyRequestEntity::get_id)
                    .collect(lastN(numberOfResults));
            repository.removeAllBy_idIsNotIn(ids);
        }
    }

    void save(MyRequestEntity request) {
        repository.save(request);
    }

    List<MyRequestEntity> getAllRequests() {
        return repository.findAll(new Sort(Sort.Direction.DESC, "time"));
    }

    MyRequestEntity getRequest(String id) {
        return repository.findById(id).orElse(null);
    }

    void removeAll() {
        try {
            repository.deleteAll();
        } catch (Exception e) {
            log.error("Requests could not be cleared: " + e.getMessage(), e);
        }
    }
}
