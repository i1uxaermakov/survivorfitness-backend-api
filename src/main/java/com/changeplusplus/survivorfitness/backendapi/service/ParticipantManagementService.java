package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        List<Participant> participants = participantRepository.findAll();
        return getListOfParticipantDtosFromListOfParticipantEntities(participants);
    }


    private void assignStatusAndLocationsAndSpecialistsOfUserToDTO(Participant participantEntity,
                                                          ParticipantDTO participantDTO) {
        Program treatmentProgram = participantEntity.getTreatmentProgram();

        LocationDTO dietitianOfficeBriefInfo = LocationManagementService.getConciseLocationDTOBasedOnLocationEntity(treatmentProgram.getDietitianOffice());
        participantDTO.setDietitianLocation(dietitianOfficeBriefInfo);

        LocationDTO trainerGymBriefInfo = LocationManagementService.getConciseLocationDTOBasedOnLocationEntity(treatmentProgram.getTrainerGym());
        participantDTO.setTrainerLocation(trainerGymBriefInfo);

        UserDTO trainerBriefInfo = UserManagementService.getConciseUserDTOBasedOnUserEntity(treatmentProgram.getTrainer());
        participantDTO.setTrainer(trainerBriefInfo);

        UserDTO dietitianBriefInfo = UserManagementService.getConciseUserDTOBasedOnUserEntity(treatmentProgram.getDietitian());
        participantDTO.setDietitian(dietitianBriefInfo);

        participantDTO.setTreatmentProgramStatus(treatmentProgram.getProgramProgressStatus().toString());
    }


    public List<ParticipantDTO> getParticipantsAtSpecificDietitianOffice(Integer dietitianOfficeId) {
        List<Participant> participants = participantRepository.findParticipantsByTreatmentProgramDietitianOfficeId(dietitianOfficeId);
        return getListOfParticipantDtosFromListOfParticipantEntities(participants);
    }


    public List<ParticipantDTO> getParticipantsAtSpecificTrainerGym(Integer gymId) {
        List<Participant> participants = participantRepository.findParticipantsByTreatmentProgramTrainerGymId(gymId);
        return getListOfParticipantDtosFromListOfParticipantEntities(participants);
    }

    public List<ParticipantDTO> getParticipantsAssignedToSpecificTrainer(Integer trainerUserId) {
        List<Participant> participants = participantRepository.findParticipantsByTreatmentProgramTrainerId(trainerUserId);
        return getListOfParticipantDtosFromListOfParticipantEntities(participants);
    }

    public List<ParticipantDTO> getParticipantsAssignedToSpecificDietitian(Integer dietitianUserId) {
        List<Participant> participants = participantRepository.findParticipantsByTreatmentProgramDietitianId(dietitianUserId);
        return getListOfParticipantDtosFromListOfParticipantEntities(participants);
    }


    private List<ParticipantDTO> getListOfParticipantDtosFromListOfParticipantEntities(List<Participant> participantEntitiesList) {
//        List<ParticipantDTO> participantDtoList = new ArrayList<>();
//        for(Participant participantEntity: participantEntitiesList) {
//            ParticipantDTO dto = getParticipantDtoFromParticipantEntity(participantEntity);
//            participantDtoList.add(dto);
//        }
//        return participantDtoList;

        return participantEntitiesList.stream()
                .map(this::getParticipantDtoFromParticipantEntity)
                .collect(Collectors.toList());
    }

    private ParticipantDTO getParticipantDtoFromParticipantEntity(Participant participantEntity) {
        ParticipantDTO participantDTO = new ParticipantDTO(participantEntity);
        assignStatusAndLocationsAndSpecialistsOfUserToDTO(participantEntity, participantDTO);
        return participantDTO;
    }
}
