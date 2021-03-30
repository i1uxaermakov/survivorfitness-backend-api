package com.changeplusplus.survivorfitness.backendapi.controller.payload;

import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantDTO;

public class InfoAboutParticularParticipantResponse {
    private ParticipantDTO participant;

    public InfoAboutParticularParticipantResponse(ParticipantDTO participant) {
        this.participant = participant;
    }

    public ParticipantDTO getParticipant() {
        return participant;
    }

    public void setParticipant(ParticipantDTO participant) {
        this.participant = participant;
    }
}
