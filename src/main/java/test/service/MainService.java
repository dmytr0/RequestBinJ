package test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import test.domain.MyRequestEntity;
import test.repository.RequestsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class MainService {

    @Autowired
    private RequestsRepository repository;

    public void add(String method, String body, Map<String, String> headers, Map<String, String> params) {
        MyRequestEntity request = new MyRequestEntity(method, body, headers, params);
        repository.save(request);
        removeOld();
    }

    private void removeOld() {
        repository.removeAllByTimeBefore(LocalDateTime.now().minusDays(1));
        long count = repository.count();
        if (count > 20) {
            repository.removeAllByTimeBefore(LocalDateTime.now().minusMinutes(5));
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
