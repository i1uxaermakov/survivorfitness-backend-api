package com.changeplusplus.survivorfitness.backendapi.repository;

import com.changeplusplus.survivorfitness.backendapi.entity.Participant;
import com.changeplusplus.survivorfitness.backendapi.entity.Session;
import com.changeplusplus.survivorfitness.backendapi.entity.SpecialistType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends CrudRepository<Session, Integer> {
    List<Session> findAll();
    List<Session> findSessionsByParticipantIdAndWhoseNotes(Integer participantId, SpecialistType type);
}
