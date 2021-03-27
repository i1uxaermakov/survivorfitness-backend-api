package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.entity.Participant;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipantManagementService {

    @Autowired
    private ParticipantRepository participantRepository;

    public Participant getParticipantInfoById(Integer participantId) {
        return participantRepository.findParticipantById(participantId);
    }


}
