package com.changeplusplus.survivorfitness.backendapi.repository;

import com.changeplusplus.survivorfitness.backendapi.entity.User;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRoleType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserByEmail(String email);
    List<User> findUsersByRolesNameAndLocationsAssignedToId(UserRoleType roleName, Integer locationId);//todo change the search by role and location within location assignment
    List<User> findUsersByRolesName(UserRoleType roleName);// change the search by role within location assignment
    User findUserById(Integer userId);
}
