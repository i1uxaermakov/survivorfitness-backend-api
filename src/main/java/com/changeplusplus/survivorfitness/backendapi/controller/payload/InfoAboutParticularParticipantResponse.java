package com.changeplusplus.survivorfitness.backendapi.controller.payload;

import com.changeplusplus.survivorfitness.backendapi.entity.Participant;
import org.springframework.http.ResponseEntity;

public class InfoAboutParticularParticipantResponse {
    private Participant participant;

    public InfoAboutParticularParticipantResponse(Participant participant) {
        this.participant = participant;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
}
