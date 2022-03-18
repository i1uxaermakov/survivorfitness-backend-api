package com.changeplusplus.survivorfitness.backendapi.repository;

import com.changeplusplus.survivorfitness.backendapi.entity.LocationAssignment;
import org.springframework.data.repository.CrudRepository;

public interface LocationAssignmentRepository extends CrudRepository<LocationAssignment, Integer> {
    void delete(LocationAssignment entity);
}
