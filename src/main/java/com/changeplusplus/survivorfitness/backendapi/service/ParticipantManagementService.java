package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.entity.projection.ParticipantGeneralInfoProjection;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParticipantManagementService {

    @Autowired
    private ParticipantRepository participantRepository;

    public ParticipantDTO getParticipantInfoById(Integer participantId) {
        Participant participantEntity = participantRepository.findParticipantById(participantId);
        if(participantEntity == null) {
            return null;
        }
        return getParticipantDtoFromParticipantEntity(participantEntity);
    }

    public List<ParticipantDTO> getGeneralInfoAboutAllParticipants() {
        //Get general info (name, id, specialists they are assigned to) about all participants
        List<ParticipantGeneralInfoProjection> participantsProjections = participantRepository.findAllProjectedBy();
        return getListOfParticipantDtosFromListOfParticipantProjections(participantsProjections);
    }


    private void assignLocationsAndSpecialistsOfUserToDTO(User dietitian, User trainer,
                                                          Location dietitianOffice, Location trainerGym,
                                                          ParticipantDTO participantDTO) {
        participantDTO.setDietitianLocation(LocationManagementService.getConciseLocationDTOBasedOnLocationEntity(dietitianOffice));
        participantDTO.setTrainerLocation(LocationManagementService.getConciseLocationDTOBasedOnLocationEntity(trainerGym));

        participantDTO.setTrainer(UserManagementService.getConciseUserDTOBasedOnUserEntity(trainer));
        participantDTO.setDietitian(UserManagementService.getConciseUserDTOBasedOnUserEntity(dietitian));
    }


    public List<ParticipantDTO> getParticipantsAtSpecificDietitianOffice(Integer dietitianOfficeId) {
        List<ParticipantGeneralInfoProjection> participantProjectionsList = participantRepository.findParticipantsByDietitianOfficeId(dietitianOfficeId);
        return getListOfParticipantDtosFromListOfParticipantProjections(participantProjectionsList);
    }


    public List<ParticipantDTO> getParticipantsAtSpecificTrainerGym(Integer gymId) {
        List<ParticipantGeneralInfoProjection> participantProjectionsList = participantRepository.findParticipantsByTrainerGymId(gymId);
        return getListOfParticipantDtosFromListOfParticipantProjections(participantProjectionsList);
    }

    public List<ParticipantDTO> getParticipantsAssignedToSpecificTrainer(Integer trainerUserId) {
        List<ParticipantGeneralInfoProjection> participantProjectionsList = participantRepository.findParticipantsByTrainerId(trainerUserId);
        return getListOfParticipantDtosFromListOfParticipantProjections(participantProjectionsList);
    }

    public List<ParticipantDTO> getParticipantsAssignedToSpecificDietitian(Integer dietitianUserId) {
        List<ParticipantGeneralInfoProjection> participantProjectionsList = participantRepository.findParticipantsByDietitianId(dietitianUserId);
        return getListOfParticipantDtosFromListOfParticipantProjections(participantProjectionsList);
    }


    private List<ParticipantDTO> getListOfParticipantDtosFromListOfParticipantEntities(List<Participant> participantEntitiesList) {
        List<ParticipantDTO> participantDtoList = new ArrayList<>();
        for(Participant participantEntity: participantEntitiesList) {
            participantDtoList.add(getParticipantDtoFromParticipantEntity(participantEntity));
        }
        return participantDtoList;
    }

    private ParticipantDTO getParticipantDtoFromParticipantEntity(Participant participantEntity) {
        ParticipantDTO participantDTO = new ParticipantDTO(participantEntity);
        assignLocationsAndSpecialistsOfUserToDTO(
                participantEntity.getDietitian(), participantEntity.getTrainer(),
                participantEntity.getDietitianOffice(), participantEntity.getTrainerGym(),
                participantDTO);
        return participantDTO;
    }

    private ParticipantDTO getParticipantDtoFromParticipantProjection(ParticipantGeneralInfoProjection projection) {
        ParticipantDTO participantDTO = new ParticipantDTO();
        participantDTO.setId(projection.getId());
        participantDTO.setFirstName(projection.getFirstName());
        participantDTO.setLastName(projection.getLastName());

        assignLocationsAndSpecialistsOfUserToDTO(
                projection.getDietitian(), projection.getTrainer(),
                projection.getDietitianOffice(), projection.getTrainerGym(),
                participantDTO);

        return participantDTO;
    }

    private List<ParticipantDTO> getListOfParticipantDtosFromListOfParticipantProjections(List<ParticipantGeneralInfoProjection> participantsProjections) {
        //Convert data received from the database into the format expected by the client (list of ParticipantDTOs)
        List<ParticipantDTO> participantsPreparedInfo = new ArrayList<>();
        for(ParticipantGeneralInfoProjection projection: participantsProjections) {
            participantsPreparedInfo.add(getParticipantDtoFromParticipantProjection(projection));
        }
        return participantsPreparedInfo;
    }
}
