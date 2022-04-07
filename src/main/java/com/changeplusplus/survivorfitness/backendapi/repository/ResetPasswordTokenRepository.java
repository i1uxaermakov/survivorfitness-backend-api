package com.changeplusplus.survivorfitness.backendapi.repository;

import com.changeplusplus.survivorfitness.backendapi.entity.ResetPasswordToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, Integer> {
}
