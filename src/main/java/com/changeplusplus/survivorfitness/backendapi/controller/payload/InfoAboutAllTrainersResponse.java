package com.changeplusplus.survivorfitness.backendapi.controller.payload;

import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;

import java.util.List;

public class InfoAboutAllTrainersResponse {
    private List<UserDTO> trainers;

    public InfoAboutAllTrainersResponse(List<UserDTO> trainers) {
        this.trainers = trainers;
    }

    public List<UserDTO> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<UserDTO> trainers) {
        this.trainers = trainers;
    }
}
