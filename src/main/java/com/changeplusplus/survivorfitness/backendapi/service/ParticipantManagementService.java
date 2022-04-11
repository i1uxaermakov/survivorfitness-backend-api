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

/**
 * The class that encapsulates all functionality related to the management of Participant
 * entities (retrieval, creation, etc.) @Service annotation indicates to the Spring
 * framework that it should create a singleton of this class and maintain its state.
 * Users of this class can get a reference to it by creating a field of this class in
 * another class and annotating it with @Autowired (see below for examples of
 * autowiring other services).
 */
@Service
public class ParticipantManagementService {

    /**
     * A reference to the interface that manages interaction with the database that
     * pertains to Participant entity (all SQL operations with Participants table)
     */
    @Autowired
    private ParticipantRepository participantRepository;

    /**
     * A reference to a class that encapsulates all functionality related to
     * management of Session entities
     */
    @Autowired
    private SessionManagementService sessionManagementService;

    /**
     * A reference to the interface that manages interaction with the database that
     * pertains to Location entity (all SQL operations with Locations table)
     */
    @Autowired
    private LocationRepository locationRepository;

    /**
     * A reference to the interface that manages interaction with the database that
     * pertains to User entity (all SQL operations with Users table)
     */
    @Autowired
    private UserRepository userRepository;


    /**
     * Retrieves a participant by their ID.
     * @param participantId ID of the participant
     * @return ParticipantDTO with the same fields as in the Participant
     * entity. If no user was found, returns null
     */
    public ParticipantDTO getParticipantInfoById(Integer participantId) {
        Participant participantEntity = participantRepository.findParticipantById(participantId);
        if(participantEntity == null) {
            return null;
        }
        return getParticipantDtoFromParticipantEntity(participantEntity);
    }


    /**
     * Retrieves information about all participants in the system. Each ParticipantDTO object
     * will have the name, id, and specialists they are assigned to set.
     * @return A list of ParticipantDTOs with the fields equal to the ones of the corresponding
     * Participant entity
     */
    public List<ParticipantDTO> getGeneralInfoAboutAllParticipants() {
        //Get general info (name, id, specialists they are assigned to) about all participants
        List<Participant> participants = participantRepository.findAll();
        return getListOfParticipantDtosFromListOfParticipantEntities(participants);
    }


    /**
     * A helper method for getParticipantDtoFromParticipantEntity(). Given a Participant entity, retrieves
     * information about its gym, dietitian office, trainer, and dietitian and sets the fields of ParticipantDTO to it.
     * Updates the passed @param participantDTO. For the locations, only ID, name, and type will be set. For Users,
     * only ID and name will be set.
     * @param participantEntity the Participant Entity in the database
     * @param participantDTO the participantDTO whose gym, dietitian office, trainer, and dietitian fields have
     *                       to be set to the ones in Participant entity and its associated Program
     */
    private void assignStatusAndLocationsAndSpecialistsOfUserToDTO(Participant participantEntity,
                                                          ParticipantDTO participantDTO) {
        // Retrieve the associated Program object (since info about specialists and
        // locations of the participant is saved here).
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


    /**
     * Finds participants assigned to the Dietitian Office with the location ID equal to @param dietitianOfficeId.
     * @param dietitianOfficeId The ID of the dietitian office Participants should be assigned to
     * @return A list of Participants assigned to the Dietitian Office. Returns an empty list if such location
     * doesn't exist or there are no participants assigned to it.
     */
    public List<ParticipantDTO> getParticipantsAtSpecificDietitianOffice(Integer dietitianOfficeId) {
        List<Participant> participants = participantRepository.findParticipantsByTreatmentProgramDietitianOfficeId(dietitianOfficeId);
        return getListOfParticipantDtosFromListOfParticipantEntities(participants);
    }


    /**
     * Finds participants assigned to the Gym with the location ID equal to @param gymId.
     * @param gymId The ID of the gym Participants should be assigned to
     * @return A list of Participants assigned to the Gym. Returns an empty list if such location
     * doesn't exist or there are no participants assigned to it.
     */
    public List<ParticipantDTO> getParticipantsAtSpecificTrainerGym(Integer gymId) {
        List<Participant> participants = participantRepository.findParticipantsByTreatmentProgramTrainerGymId(gymId);
        return getListOfParticipantDtosFromListOfParticipantEntities(participants);
    }


    /**
     * Finds participants assigned to the Trainer with the user ID equal to @param trainerUserId.
     * @param trainerUserId The ID of the trainer Participants should be assigned to
     * @return A list of Participants assigned to the Trainer. Returns an empty list if such user
     * doesn't exist or there are no participants assigned to them.
     */
    public List<ParticipantDTO> getParticipantsAssignedToSpecificTrainer(Integer trainerUserId) {
        List<Participant> participants = participantRepository.findParticipantsByTreatmentProgramTrainerId(trainerUserId);
        return getListOfParticipantDtosFromListOfParticipantEntities(participants);
    }


    /**
     * Finds participants assigned to the Dietitian with the user ID equal to @param dietitianUserId.
     * @param dietitianUserId The ID of the dietitian Participants should be assigned to
     * @return A list of Participants assigned to the Dietitian. Returns an empty list if such user
     * doesn't exist or there are no participants assigned to them.
     */
    public List<ParticipantDTO> getParticipantsAssignedToSpecificDietitian(Integer dietitianUserId) {
        List<Participant> participants = participantRepository.findParticipantsByTreatmentProgramDietitianId(dietitianUserId);
        return getListOfParticipantDtosFromListOfParticipantEntities(participants);
    }


    /**
     * editParticipant - method that updates the participant object
     * @param participantDTO - new DTO object of the participant
     * @return - updated DTO of the participant retrieved from the database
     */
    @Transactional
    public ParticipantDTO editParticipant(ParticipantDTO participantDTO) {

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

        //whether the new participant object has modified the treatment program at all
        boolean programHasChanged = ProgramProgressStatus.valueOf(participantDTO.getTreatmentProgramStatus()) !=
                programEntity.getProgramProgressStatus() ||
                !Objects.equals(programEntity.getTrainerGym().getId(), trainerGym.getId()) ||
                !Objects.equals(programEntity.getDietitianOffice().getId(), dietitianOffice.getId()) ||
                !Objects.equals(programEntity.getTrainer().getId(), participantDTO.getTrainer().getId()) ||
                !Objects.equals(programEntity.getDietitian().getId(), participantDTO.getDietitian().getId());

        //Only super admins and location admins can change info about the program
        if(programHasChanged &&
            !currentUser.hasRole(UserRoleType.SUPER_ADMIN) &&
            !currentUser.hasRole(UserRoleType.LOCATION_ADMINISTRATOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The current user is not allowed to change information about the " +
                            "program.");
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

        // If the trainer ID is provided, retrieve that trainer from the database and check if they exist
        // If trainer is not provided, set it to null
        User trainer = null;
        if(!Objects.isNull(participantDTO.getTrainer()) && !Objects.isNull(participantDTO.getTrainer().getId())) {
            trainer = userRepository.findUserById(participantDTO.getTrainer().getId());
            if (Objects.isNull(trainer)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Trainer with specified ID not found.");
            }
        }

        // If the dietitian ID is provided, retrieve that dietitian from the database and check if they exist
        // If dietitian is not provided, set it to null
        User dietitian = null;
        if(!Objects.isNull(participantDTO.getDietitian()) && !Objects.isNull(participantDTO.getDietitian().getId())) {
            dietitian = userRepository.findUserById(participantDTO.getDietitian().getId());
            if (Objects.isNull(dietitian)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Dietitian with specified ID not found.");
            }
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



    /**
     * Creates a new Participant and a Program associated with them. Crates all Sessions and Measurements for the
     * Participant. Later, when the data is entered for measurements or sessions, the existing entities are
     * updated – no new Sessions or Measurements are created.
     *
     * @param newParticipantData Personal info about the new participant and trainers and specialists
     *                           the user is assigned to.
     * @param numberOfTrainerSessions Number of trainer Sessions that will happen for this participant. This method
     *                                will pre-create this many Sessions for the trainer
     * @param whenToTakeMeasurements An array of integers that specifies the 1-based indices of sessions when
     *                               measurements should be taken for the participant. This method will create
     *                               measurements specified in @param measurementsToTake in each session specified here
     * @param measurementsToTake A list of measurements that will be taken for the participant on Sessions with indices
     *                           specified in @param whenToTakeMeasurements
     * @param numberOfDietitianSessions Number of trainer Sessions that will happen for this participant. This method
     *                                  will pre-create this many Sessions for the dietitian
     * @return ParticipantDTO with the fields equal to the ones of Participant entity (that has just been saved to the
     * database)
     */
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

        // If the trainer ID is provided, retrieve that trainer from the database and check if they exist
        // If trainer is not provided, set it to null
        User trainer = null;
        if(!Objects.isNull(newParticipantData.getTrainer()) && !Objects.isNull(newParticipantData.getTrainer().getId())) {
            trainer = userRepository.findUserById(newParticipantData.getTrainer().getId());
            if (Objects.isNull(trainer)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Trainer with specified ID not found.");
            }
        }

        // If the dietitian ID is provided, retrieve that dietitian from the database and check if they exist
        // If dietitian is not provided, set it to null
        User dietitian = null;
        if(!Objects.isNull(newParticipantData.getDietitian()) && !Objects.isNull(newParticipantData.getDietitian().getId())) {
            dietitian = userRepository.findUserById(newParticipantData.getDietitian().getId());
            if (Objects.isNull(dietitian)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Dietitian with specified ID not found.");
            }
        }

        // Create Program entity
        Program program = new Program()
                .setProgramProgressStatus(ProgramProgressStatus.SPECIALISTS_NOT_ASSIGNED)
                .setParticipant(participantEntity)
                .setTrainerGym(trainerGym)
                .setDietitianOffice(dietitianOffice)
                .setTrainer(trainer)
                .setDietitian(dietitian);

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


    /**
     * Helper method for create/edit participants: ensures that location w/ locationId exists and is of
     * type locationType.
     * @param locationId ID of the location to look for
     * @param locationType the type of the location (DIETITIAN_OFFICE or GYM) we are looking for
     * @return Location with ID=locationId and type=locationType. If no such location is present, the method throws
     * a ResponseStatusException with HttpStatus.BAD_REQUEST response to the requester
     */
    private Location verifyLocation(Integer locationId, LocationType locationType){
        // Check if the trainer location is indeed a trainer location
        Location location = locationRepository.findLocationById(locationId);
        if(Objects.isNull(location)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location specified does not exist.");
        }
        else if(location.getType() != locationType) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location specified is of type " +
                    location.getType() + ".  It should be of type " + locationType + ".");
        }
        return location;
    }


    /**
     * Converts a list of Participant entities into a list of participant DTOs
     * @param participantEntitiesList entities to convert
     * @return a list of ParticipantDTOs with the same fields as in corresponding entities
     */
    private List<ParticipantDTO> getListOfParticipantDtosFromListOfParticipantEntities(
            List<Participant> participantEntitiesList) {
        return participantEntitiesList.stream()
                .map(this::getParticipantDtoFromParticipantEntity)
                .collect(Collectors.toList());
    }


    /**
     * Convert a Participant entity to a ParticipantDTO. Used every time the data is sent back to the
     * frontend because it expects the data to be in the format of ParticipantDTO. Also sets the dietitian,
     * trainer, dietitian office, and gym of the participant.
     * @param participantEntity entity to convert
     * @return ParticipantDTO with the same fields as in participant Entity
     */
    private ParticipantDTO getParticipantDtoFromParticipantEntity(Participant participantEntity) {
        ParticipantDTO participantDTO = new ParticipantDTO(participantEntity);
        assignStatusAndLocationsAndSpecialistsOfUserToDTO(participantEntity, participantDTO);
        return participantDTO;
    }
}
