package com.changeplusplus.survivorfitness.backendapi.dto;

public class SessionResponse {
    private SessionDTO session;

    public SessionResponse(SessionDTO session) {
        this.session = session;
    }

    public SessionDTO getSession() {
        return session;
    }

    public void setSession(SessionDTO session) {
        this.session = session;
    }
}
