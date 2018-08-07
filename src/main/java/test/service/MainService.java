package test.service;

import org.springframework.stereotype.Service;
import test.domain.MyRequestEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MainService {

    private LinkedList<MyRequestEntity> requests = new LinkedList<>();


    public void add(String method, String body, Map<String, String> headers, Map<String, String> params) {
        requests.addFirst(new MyRequestEntity(method, body, headers, params));
    }

    public List<MyRequestEntity> getAllRequests() {
        return requests;
    }

    public MyRequestEntity getRequest(String id) {
        Optional<MyRequestEntity> first = requests.stream().filter(r -> r.getId().equals(id)).findFirst();
        return first.orElse(null);
    }

    public void removeAll() {
        requests.clear();
    }
}
