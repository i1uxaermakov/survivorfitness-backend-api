package com.changeplusplus.survivorfitness.backendapi.controller.payload;

import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantDTO;

import java.util.List;

public class InfoAboutAllParticipantsResponse {
    List<ParticipantDTO> participants;

    public InfoAboutAllParticipantsResponse(List<ParticipantDTO> participants) {
        this.participants = participants;
    }

    public List<ParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantDTO> participants) {
        this.participants = participants;
    }
}
