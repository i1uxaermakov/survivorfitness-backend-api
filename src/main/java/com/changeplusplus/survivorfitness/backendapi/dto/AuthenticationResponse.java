package com.changeplusplus.survivorfitness.backendapi.dto;

public class AuthenticationResponse {
    private String jwt;
    private UserDTO user;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String jwt, UserDTO user) {
        this.jwt = jwt;
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
