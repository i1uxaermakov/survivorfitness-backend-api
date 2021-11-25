package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.MeasurementDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.SessionDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.Measurement;
import com.changeplusplus.survivorfitness.backendapi.entity.Session;
import com.changeplusplus.survivorfitness.backendapi.entity.SpecialistType;
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

@Service
public class SessionManagementService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    public Pair<List<SessionDTO>, List<SessionDTO>> getAllSessionNotesOfParticipant(Integer participantId) {
        List<SessionDTO> dietitianSessionDTOs = getDietitianSessionsOfParticipant(participantId);
        List<SessionDTO> trainerSessionDTOs = getTrainerSessionsOfParticipant(participantId);

        return Pair.of(trainerSessionDTOs, dietitianSessionDTOs);
    }

    public List<SessionDTO> getDietitianSessionsOfParticipant(Integer participantId) {
        List<Session> dietitianSessionEntities = sessionRepository.findSessionsByParticipantIdAndWhoseNotes(participantId, SpecialistType.DIETITIAN);
        List<SessionDTO> sessionDTOs = getListOfSessionDtosFromListOfSessionEntities(dietitianSessionEntities);
        sessionDTOs.sort(Comparator.comparing(SessionDTO::getSessionIndexNumber));
        return sessionDTOs;
    }

    public List<SessionDTO> getTrainerSessionsOfParticipant(Integer participantId) {
        List<Session> dietitianSessionEntities = sessionRepository.findSessionsByParticipantIdAndWhoseNotes(participantId, SpecialistType.TRAINER);
        List<SessionDTO> sessionDTOs = getListOfSessionDtosFromListOfSessionEntities(dietitianSessionEntities);
        sessionDTOs.sort(Comparator.comparing(SessionDTO::getSessionIndexNumber));
        return sessionDTOs;
    }

    @Transactional
    public SessionDTO updateSession(SessionDTO sessionDtoToUpdate) {
        Session sessionEntityInDb = sessionRepository.findById(sessionDtoToUpdate.getId()).orElse(null);
        if(Objects.isNull(sessionEntityInDb)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Session with specified ID not found.");
        }

        // Update fields of the Session
        sessionEntityInDb.setAdminNotes(sessionDtoToUpdate.getAdminNotes());
        sessionEntityInDb.setSpecialistNotes(sessionDtoToUpdate.getSpecialistNotes());
        if(Objects.isNull(sessionEntityInDb.getInitialLogDate())) {
            sessionEntityInDb.setInitialLogDate(new Date(sessionDtoToUpdate.getInitialLogDate()));
        }
        sessionEntityInDb.setLastUpdatedDate(new Date());

        // Update measurements
        for(MeasurementDTO measurementDTO: sessionDtoToUpdate.getMeasurements()) {
            Integer m_id = measurementDTO.getId();
            Measurement measurementInDb = measurementRepository.findById(m_id).orElse(null);
            if(Objects.isNull(measurementInDb)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Measurement with specified ID (" + m_id + ") not found.");
            }
            measurementInDb.setValue(measurementDTO.getValue());
        }

        return getSessionDtoFromSessionEntity(sessionRepository.save(sessionEntityInDb));
    }

    private List<SessionDTO> getListOfSessionDtosFromListOfSessionEntities(List<Session> sessionEntities) {
        return sessionEntities.stream()
                .map(this::getSessionDtoFromSessionEntity)
                .collect(Collectors.toList());
    }

    private SessionDTO getSessionDtoFromSessionEntity(Session sessionEntity) {
        return new SessionDTO(sessionEntity);
    }
}
