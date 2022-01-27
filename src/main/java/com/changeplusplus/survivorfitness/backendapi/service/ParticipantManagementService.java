package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.MeasurementDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.LocationRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ParticipantManagementService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private SessionManagementService sessionManagementService;

    @Autowired
    private LocationRepository locationRepository;

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

        // Check if the trainer location is indeed a trainer location
        Location trainerGym = locationRepository.findLocationById(newParticipantData.getTrainerLocation().getId());
        if(Objects.isNull(trainerGym)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The gym specified does not exist.");
        }
        else if(trainerGym.getType() != LocationType.TRAINER_GYM) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The gym specified is not a gym. It has the type of " + trainerGym.getType());
        }

        // Check if the dietitian location is indeed a dietitian location
        Location dietitianOffice = locationRepository.findLocationById(newParticipantData.getDietitianLocation().getId());
        if(Objects.isNull(dietitianOffice)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The gym specified does not exist.");
        }
        else if(dietitianOffice.getType() != LocationType.DIETICIAN_OFFICE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The gym specified is not a gym. It has the type of " + trainerGym.getType());
        }

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
                .setParticipant(participantEntity)
                .setTrainerGym(trainerGym)
                .setDietitianOffice(dietitianOffice);

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
