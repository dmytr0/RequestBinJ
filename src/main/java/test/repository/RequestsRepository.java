package test.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import test.domain.MyRequestEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestsRepository extends CrudRepository<MyRequestEntity, String> {

    List<MyRequestEntity> findAll(Sort sort);

    void removeAllByTimeBefore(LocalDateTime beforeTome);

}
