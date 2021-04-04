package com.changeplusplus.survivorfitness.backendapi.controller.payload;

import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;

import java.util.List;

public class InfoAboutAllDietitiansResponse {
    private List<UserDTO> dietitians;

    public InfoAboutAllDietitiansResponse(List<UserDTO> dietitians) {
        this.dietitians = dietitians;
    }

    public List<UserDTO> getDietitians() {
        return dietitians;
    }

    public void setDietitians(List<UserDTO> dietitians) {
        this.dietitians = dietitians;
    }
}
