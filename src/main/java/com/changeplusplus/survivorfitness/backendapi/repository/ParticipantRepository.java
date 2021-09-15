package com.changeplusplus.survivorfitness.backendapi.repository;

import com.changeplusplus.survivorfitness.backendapi.entity.Participant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends CrudRepository<Participant, Integer> {
    Participant findParticipantById(Integer id);
    List<Participant> findAll();
    List<Participant> findParticipantsByTreatmentProgramDietitianOfficeId(Integer dietitianOfficeId);
    List<Participant> findParticipantsByTreatmentProgramTrainerGymId(Integer trainerGymId);
    List<Participant> findParticipantsByTreatmentProgramTrainerId(Integer trainerUserId);
    List<Participant> findParticipantsByTreatmentProgramDietitianId(Integer dietitianUserId);
}
