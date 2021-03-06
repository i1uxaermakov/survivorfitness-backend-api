package com.changeplusplus.survivorfitness.backendapi.repository;

import com.changeplusplus.survivorfitness.backendapi.entity.UserRole;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRoleType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {
    UserRole findUserRoleByName(UserRoleType name);
}
