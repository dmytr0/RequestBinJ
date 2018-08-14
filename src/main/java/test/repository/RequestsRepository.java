package test.repository;

import org.springframework.data.repository.CrudRepository;
import test.domain.MyRequestEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestsRepository extends CrudRepository<MyRequestEntity, String> {

    List<MyRequestEntity> findAll();

    void removeAllByTimeBefore(LocalDateTime beforeTome);

}
