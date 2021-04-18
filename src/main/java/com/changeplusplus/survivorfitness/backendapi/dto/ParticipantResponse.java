package com.changeplusplus.survivorfitness.backendapi.dto;

import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantDTO;

public class ParticipantResponse {
    private ParticipantDTO participant;

    public ParticipantResponse(ParticipantDTO participant) {
        this.participant = participant;
    }

    public ParticipantDTO getParticipant() {
        return participant;
    }

    public void setParticipant(ParticipantDTO participant) {
        this.participant = participant;
    }
}
