package com.changeplusplus.survivorfitness.backendapi.dto;

import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;

import java.util.List;

public class UserListResponse {
    private List<UserDTO> specialists;

    public UserListResponse(List<UserDTO> specialists) {
        this.specialists = specialists;
    }

    public List<UserDTO> getSpecialists() {
        return specialists;
    }

    public void setSpecialists(List<UserDTO> specialists) {
        this.specialists = specialists;
    }
}
