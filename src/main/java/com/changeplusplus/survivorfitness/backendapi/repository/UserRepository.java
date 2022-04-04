package com.changeplusplus.survivorfitness.backendapi.repository;

import com.changeplusplus.survivorfitness.backendapi.entity.User;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRoleType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserByEmail(String email);
    List<User> findUsersByLocationAssignmentsUserRoleTypeAndLocationAssignmentsLocationId(UserRoleType roleName, Integer locationId);
    User findUserById(Integer userId);
    List<User> findAll();
    List<User> findDistinctUsersByLocationAssignmentsUserRoleType(UserRoleType roleName);
    List<User> findUsersBySuperAdmin(boolean isSuperAdmin);
}
