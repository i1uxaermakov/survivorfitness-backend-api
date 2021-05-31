package com.changeplusplus.survivorfitness.backendapi.repository;

import com.changeplusplus.survivorfitness.backendapi.entity.Participant;
import com.changeplusplus.survivorfitness.backendapi.entity.projection.ParticipantGeneralInfoProjection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends CrudRepository<Participant, Integer> {
    Participant findParticipantById(Integer id);
    List<ParticipantGeneralInfoProjection> findAllProjectedBy();
    List<ParticipantGeneralInfoProjection> findParticipantsByTrainerGymId(Integer gymId);
    List<ParticipantGeneralInfoProjection> findParticipantsByDietitianOfficeId(Integer dietitianOfficeId);
    List<ParticipantGeneralInfoProjection> findParticipantsByDietitianId(Integer dietitianUserId);
    List<ParticipantGeneralInfoProjection> findParticipantsByTrainerId(Integer trainerUserId);
}
