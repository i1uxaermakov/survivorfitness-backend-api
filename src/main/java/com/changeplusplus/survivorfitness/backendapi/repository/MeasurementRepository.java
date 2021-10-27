package com.changeplusplus.survivorfitness.backendapi.repository;

import com.changeplusplus.survivorfitness.backendapi.entity.Measurement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends CrudRepository<Measurement, Integer> {
}
