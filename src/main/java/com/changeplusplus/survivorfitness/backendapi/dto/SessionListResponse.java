package com.changeplusplus.survivorfitness.backendapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionListResponse {
    private List<SessionDTO> trainerSessions;
    private List<SessionDTO> dietitianSessions;

    public SessionListResponse(List<SessionDTO> trainerSessions, List<SessionDTO> dietitianSessions) {
        this.trainerSessions = trainerSessions;
        this.dietitianSessions = dietitianSessions;
    }

    public List<SessionDTO> getTrainerSessions() {
        return trainerSessions;
    }

    public void setTrainerSessions(List<SessionDTO> trainerSessions) {
        this.trainerSessions = trainerSessions;
    }

    public List<SessionDTO> getDietitianSessions() {
        return dietitianSessions;
    }

    public void setDietitianSessions(List<SessionDTO> dietitianSessions) {
        this.dietitianSessions = dietitianSessions;
    }
}
