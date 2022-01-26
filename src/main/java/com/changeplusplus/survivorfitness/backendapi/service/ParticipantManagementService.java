package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.MeasurementDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipantManagementService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private SessionManagementService sessionManagementService;

    @Autowired
    private MeasurementManagementService measurementManagementService;

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

    @Transactional
    public ParticipantDTO createNewParticipant(
            ParticipantDTO newParticipantData, Integer numberOfTrainerSessions,
            List<Integer> whenToTakeMeasurements, List<MeasurementDTO> measurementsToTake,
            Integer numberOfDietitianSessions) {

        // Create Participant entity
        Participant participantEntity = new Participant()
                .setFirstName(newParticipantData.getFirstName())
                .setLastName(newParticipantData.getLastName())
                .setAge(newParticipantData.getAge())
                .setEmail(newParticipantData.getEmail())
                .setPhoneNumber(newParticipantData.getPhoneNumber())
                .setStartDate(new Date(newParticipantData.getStartDate()))
                .setGoals(newParticipantData.getGoals())
                .setTypeOfCancer(newParticipantData.getTypeOfCancer())
                .setFormsOfTreatment(newParticipantData.getFormsOfTreatment())
                .setSurgeries(newParticipantData.getSurgeries())
                .setPhysicianNotes(newParticipantData.getPhysicianNotes());

        // Create Program entity
        Program program = new Program()
                .setProgramProgressStatus(ProgramProgressStatus.SPECIALISTS_NOT_ASSIGNED)
                .setParticipant(participantEntity);

        // Create Session entities
        List<Session> dietitianSessions =
                sessionManagementService.createDietitianSessionEntities(numberOfDietitianSessions, program);
        List<Session> trainerSessions =
                sessionManagementService.createTrainerSessionEntities(numberOfTrainerSessions,
                        program, whenToTakeMeasurements, measurementsToTake);
        program.setTrainerSessions(trainerSessions);
        program.setDietitianSessions(dietitianSessions);

        // Assign the program to the participant
        participantEntity.setTreatmentProgram(program);

        // Save the participant. The other entities will be saved by the cascade
        participantEntity = participantRepository.save(participantEntity);

        return getParticipantDtoFromParticipantEntity(participantEntity);
    }


    private List<ParticipantDTO> getListOfParticipantDtosFromListOfParticipantEntities(List<Participant> participantEntitiesList) {
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
