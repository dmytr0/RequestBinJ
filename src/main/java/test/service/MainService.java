package test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.domain.MyRequestEntity;
import test.repository.RequestsRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MainService {

    @Autowired
    private RequestsRepository repository;

    private LinkedList<MyRequestEntity> requests = new LinkedList<>();


    public void add(String method, String body, Map<String, String> headers, Map<String, String> params) {
        MyRequestEntity request = new MyRequestEntity(method, body, headers, params);
        repository.save(request);
        requests.addFirst(request);
        removeOld();
    }

    private void removeOld() {
        repository.removeAllByTimeBefore(LocalDateTime.now().minusDays(1));
    }

    public List<MyRequestEntity> getAllRequests() {
        return repository.findAll();
//        return requests;
    }

    public MyRequestEntity getRequest(String id) {
        Optional<MyRequestEntity> first = requests.stream().filter(r -> r.get_id().equals(id)).findFirst();
//        return first.orElse(null);
        return repository.findById(id).orElse(null);
    }

    public void removeAll() {
        requests.clear();
        repository.deleteAll();
    }
}
