package com.changeplusplus.survivorfitness.backendapi.dto;

public class UserResponse {
    private UserDTO user;

    public UserResponse(UserDTO user) {
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
