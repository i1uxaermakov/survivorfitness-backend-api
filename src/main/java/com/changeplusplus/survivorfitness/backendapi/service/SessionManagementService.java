package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.MeasurementDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.SessionDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.MeasurementRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The class that encapsulates all functionality related to the management of Session
 * entities (retrieval, creation, updating, etc.) @Service annotation indicates to the Spring
 * framework that it should create a singleton of this class and maintain its state.
 * Users of this class can get a reference to it by creating a field of this class in
 * another class and annotating it with @Autowired (see below for examples of
 * autowiring other services).
 */
@Service
public class SessionManagementService {

    /**
     * A reference to the interface that manages interaction with the database that
     * pertains to Session entity (all SQL operations with Sessions table)
     */
    @Autowired
    private SessionRepository sessionRepository;

    /**
     * A reference to the interface that manages interaction with the database that
     * pertains to Measurement entity (all SQL operations with Measurements table)
     */
    @Autowired
    private MeasurementRepository measurementRepository;


    /**
     * Retrieves all (both trainer's and dietitian's) sessions of a participant specified by @param participantId.
     * If such participant doesn't exist or there are no sessions associated with them, the method will return a pair
     * of two empty lists.
     * @param participantId ID of the participants whose sessions have to be retrieved.
     * @return A pair of lists. First element of the pair is the list of trainer sessions; the second – of
     * dietitian sessions.
     */
    public Pair<List<SessionDTO>, List<SessionDTO>> getAllSessionNotesOfParticipant(Integer participantId) {
        List<SessionDTO> dietitianSessionDTOs = getDietitianSessionsOfParticipant(participantId);
        List<SessionDTO> trainerSessionDTOs = getTrainerSessionsOfParticipant(participantId);

        return Pair.of(trainerSessionDTOs, dietitianSessionDTOs);
    }


    /**
     * Retrieves dietitian's session of the participant specified by @param participantId. If a participant with
     * such ID doesn't exit or there are no sessions associated with it, returns ab empty list.
     * @param participantId ID of the participant whose dietitian sessions should be retrieved
     * @return List of Session objects - dietitian sessions
     */
    public List<SessionDTO> getDietitianSessionsOfParticipant(Integer participantId) {
        List<Session> dietitianSessionEntities = sessionRepository.findSessionsByParticipantIdAndWhoseNotes(
                participantId, SpecialistType.DIETITIAN);
        List<SessionDTO> sessionDTOs = getListOfSessionDtosFromListOfSessionEntities(dietitianSessionEntities);
        sessionDTOs.sort(Comparator.comparing(SessionDTO::getSessionIndexNumber));
        return sessionDTOs;
    }


    /**
     * Retrieves trainer's session of the participant specified by @param participantId. If a participant with
     * such ID doesn't exit or there are no sessions associated with it, returns ab empty list.
     * @param participantId ID of the participant whose trainer sessions should be retrieved
     * @return List of Session objects - trainer sessions
     */
    public List<SessionDTO> getTrainerSessionsOfParticipant(Integer participantId) {
        List<Session> dietitianSessionEntities = sessionRepository.findSessionsByParticipantIdAndWhoseNotes(
                participantId, SpecialistType.TRAINER);
        List<SessionDTO> sessionDTOs = getListOfSessionDtosFromListOfSessionEntities(dietitianSessionEntities);
        sessionDTOs.sort(Comparator.comparing(SessionDTO::getSessionIndexNumber));
        return sessionDTOs;
    }


    /**
     * Updates the Session and the associated Measurement objects. Does not update whoseNotes, sessionIndexNumber,
     * and participant because those are immutable.
     * @param sessionDtoToUpdate SessionDTO received from the frontend that contains the new fields of the Session
     * @return SessionDTO with the fields equal to the ones of the updated Session entity
     */
    @Transactional
    public SessionDTO updateSession(SessionDTO sessionDtoToUpdate) {
        Session sessionEntityInDb = sessionRepository.findById(sessionDtoToUpdate.getId()).orElse(null);
        if(Objects.isNull(sessionEntityInDb)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session with specified ID not found.");
        }

        // Update fields of the Session
        sessionEntityInDb.setAdminNotes(sessionDtoToUpdate.getAdminNotes());
        sessionEntityInDb.setSpecialistNotes(sessionDtoToUpdate.getSpecialistNotes());
        sessionEntityInDb.setInitialLogDate(new Date(sessionDtoToUpdate.getInitialLogDate()));
        sessionEntityInDb.setLastUpdatedDate(new Date());

        // Update measurements
        for(MeasurementDTO measurementDTO: sessionDtoToUpdate.getMeasurements()) {
            Integer m_id = measurementDTO.getId();
            Measurement measurementInDb = measurementRepository.findById(m_id).orElse(null);
            if(Objects.isNull(measurementInDb)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Measurement with specified ID (" + m_id + ") not found.");
            }
            measurementInDb.setValue(measurementDTO.getValue());
        }

        return getSessionDtoFromSessionEntity(sessionRepository.save(sessionEntityInDb));
    }


    /**
     * Creates the specified number of Sessions. Sets their type to specialistType and associates the notes with
     * the participant specified in the @param program object.
     * @param specialistType whose session notes these will be
     * @param numberOfSessions how many sessions to create
     * @param program the program to associate the sessions with
     * @return a list of sessions that have NOT been saved in the database
     */
    private List<Session> createSessionEntities(SpecialistType specialistType,
                                                Integer numberOfSessions, Program program) {
        List<Session> list = new ArrayList<>();

        for(int i=0; i<numberOfSessions; i++) {
            Session session = new Session()
                    .setSessionIndexNumber(i+1)
                    .setParticipant(program.getParticipant())
                    .setProgram(program)
                    .setWhoseNotes(specialistType);

            list.add(session);
        }

        return list;
    }


    /**
     * Creates the specified number of trainer sessions and associates them with the Program object. For each
     * session specified in @param whenToTakeMeasurements, also creates Measurement objects specified
     * in @param measurementsToTake.
     * @param numberOfSessions how many trainer sessions to create
     * @param program The program sessions will be associated with
     * @param whenToTakeMeasurements a list of integers specifying on which sessions measurements are taken
     * @param measurementsToTake A list of measurements that should be taken. These will be converted to Measurement
     *                           entities.
     * @return A list of trainer sessions that have just been created but have not been saved
     */
    public List<Session> createTrainerSessionEntities(Integer numberOfSessions, Program program,
                                                      List<Integer> whenToTakeMeasurements,
                                                      List<MeasurementDTO> measurementsToTake) {
        // Create trainer sessions
        List<Session> sessions = createSessionEntities(SpecialistType.TRAINER, numberOfSessions, program);

        // Create measurements for each session specified in @param whenToTakeMeasurements
        for(Integer sessionIndex: whenToTakeMeasurements) {
            if(sessionIndex <= 0 || sessionIndex > numberOfSessions) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "A session index in the whenToTakeMeasurements list is non-positive or more " +
                                "than the number of sessions");
            }

            Session sessionWithMeasurements = sessions.get(sessionIndex - 1);

            // Create Measurement entities
            List<Measurement> measurementEntities =
                    measurementsToTake.stream()
                            .map(mdto -> new Measurement()
                                    .setCategory(mdto.getCategory())
                                    .setName(mdto.getName())
                                    .setValue("")
                                    .setUnit(mdto.getUnit())
                                    .setSession(sessionWithMeasurements))
                            .collect(Collectors.toList());

            sessionWithMeasurements.setMeasurements(measurementEntities);
        }

        return sessions;
    }


    /**
     * Creates the specified number of Dietitian Sessions in the Program object. Used by the
     * ParticipantManagementService.createParticipant() method
     * @param numberOfSessions how many dietitian sessions to create
     * @param program What program the sessions should be assiciated with
     * @return A list of dietitian Session objects that are NOT saved in the database.
     */
    public List<Session> createDietitianSessionEntities(Integer numberOfSessions, Program program) {
        return createSessionEntities(SpecialistType.DIETITIAN, numberOfSessions, program);
    }


    /**
     * Converts a list of Session entities into a list of SessionDTOs.
     * @param sessionEntities a list of entities to convert
     * @return A list of SessionDTOs with the same fields as in the corresponding Session entities
     */
    private List<SessionDTO> getListOfSessionDtosFromListOfSessionEntities(List<Session> sessionEntities) {
        return sessionEntities.stream()
                .map(this::getSessionDtoFromSessionEntity)
                .collect(Collectors.toList());
    }


    /**
     * Converts a Session Entity to a SessionDTO.
     * @param sessionEntity entity to convert
     * @return SessionDTO with the same fields as in Session entity
     */
    private SessionDTO getSessionDtoFromSessionEntity(Session sessionEntity) {
        return new SessionDTO(sessionEntity);
    }
}
