package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.MeasurementDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.LocationRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private UserRepository userRepository;

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
    public ParticipantDTO editParticipant(
            ParticipantDTO participantDTO
    ){

        //verifies the trainer gym and dietitian locations
        Location trainerGym =
                verifyLocation(participantDTO.getTrainerLocation().getId(), LocationType.TRAINER_GYM);
        Location dietitianOffice =
                verifyLocation(participantDTO.getDietitianLocation().getId(), LocationType.DIETICIAN_OFFICE);

        //gets the entity of the program and participant
        Participant participantEntity = participantRepository.findParticipantById(
                participantDTO.getId());
        if (Objects.isNull(participantEntity)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }
        Program programEntity = participantEntity.getTreatmentProgram();

        // Get information about the current user (the one who is adding a new user)
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = (String) authentication.getPrincipal();
        User currentUser = userRepository.findUserByEmail(currentUserEmail);

        //Only super admins and location admins can change info about the program
        if(
            !currentUser.hasRole(UserRoleType.SUPER_ADMIN) &&
            !currentUser.hasRole(UserRoleType.LOCATION_ADMINISTRATOR)
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The current user is not allowed to create Super Admins.");
        }

        Participant updatedParticipantEntity =
        participantEntity.setFirstName(participantDTO.getFirstName())
                .setLastName(participantDTO.getLastName())
                .setAge(participantDTO.getAge())
                .setEmail(participantDTO.getEmail())
                .setPhoneNumber(participantDTO.getPhoneNumber())
                .setStartDate(new Date(participantDTO.getStartDate()))
                .setGoals(participantDTO.getGoals())
                .setTypeOfCancer(participantDTO.getTypeOfCancer())
                .setFormsOfTreatment(participantDTO.getFormsOfTreatment())
                .setSurgeries(participantDTO.getSurgeries())
                .setPhysicianNotes(participantDTO.getPhysicianNotes());
        //check trainer and dietitian both exist & retrieve assignment
        User trainer = userRepository.findUserById(participantDTO.getTrainer().getId());
        User dietitian = userRepository.findUserById(participantDTO.getDietitian().getId());

        if (Objects.isNull(trainer)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Trainer with specified ID not found.");
        }
        if (Objects.isNull(dietitian)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Dietitian with specified ID not found.");
        }

        //create an updated program and participant entity
        Program updatedProgramEntity = programEntity
                .setProgramProgressStatus(
                        ProgramProgressStatus.valueOf(participantDTO.getTreatmentProgramStatus()))
                .setParticipant(updatedParticipantEntity)
                .setTrainerGym(trainerGym)
                .setDietitianOffice(dietitianOffice)
                .setTrainer(trainer)
                .setDietitian(dietitian);

        updatedParticipantEntity.setTreatmentProgram(updatedProgramEntity);

        //save to DB
        updatedParticipantEntity = participantRepository.save(updatedParticipantEntity);

        return getParticipantDtoFromParticipantEntity(updatedParticipantEntity);
    }

    @Transactional
    public ParticipantDTO createNewParticipant(
            ParticipantDTO newParticipantData, Integer numberOfTrainerSessions,
            List<Integer> whenToTakeMeasurements, List<MeasurementDTO> measurementsToTake,
            Integer numberOfDietitianSessions) {

        Location trainerGym =
                verifyLocation(newParticipantData.getTrainerLocation().getId(), LocationType.TRAINER_GYM);
        Location dietitianOffice =
                verifyLocation(newParticipantData.getDietitianLocation().getId(), LocationType.DIETICIAN_OFFICE);

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

    // Helper method for create/edit participants: ensures that location w/ locationId exists and is of type locationType.
    private Location verifyLocation(Integer locationId, LocationType locationType){
        // Check if the trainer location is indeed a trainer location
        Location location = locationRepository.findLocationById(locationId);
        if(Objects.isNull(location)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location specified does not exist.");
        }
        else if(location.getType() != locationType) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location specified is of type " +
                    location.getType() +
                    ".  It should be of type " + locationType + ".");
        }
        return location;
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
