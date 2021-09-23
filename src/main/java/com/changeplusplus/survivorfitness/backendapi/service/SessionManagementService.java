package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.SessionDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.Session;
import com.changeplusplus.survivorfitness.backendapi.entity.SpecialistType;
import com.changeplusplus.survivorfitness.backendapi.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionManagementService {

    @Autowired
    private SessionRepository sessionRepository;

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

    private List<SessionDTO> getListOfSessionDtosFromListOfSessionEntities(List<Session> sessionEntities) {
        return sessionEntities.stream()
                .map(this::getSessionDtoFromSessionEntity)
                .collect(Collectors.toList());
    }

    private SessionDTO getSessionDtoFromSessionEntity(Session sessionEntity) {
        return new SessionDTO(sessionEntity);
    }
}
