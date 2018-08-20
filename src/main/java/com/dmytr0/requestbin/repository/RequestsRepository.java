package com.dmytr0.requestbin.repository;

import com.dmytr0.requestbin.domain.MyRequestEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestsRepository extends CrudRepository<MyRequestEntity, String> {

    List<MyRequestEntity> findAll(Sort sort);

    void removeAllByTimeBefore(LocalDateTime beforeTome);

    void removeAllBy_idIsNotIn(List<String> ids);

}
