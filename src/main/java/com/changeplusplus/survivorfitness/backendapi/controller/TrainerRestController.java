package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.controller.payload.SpecialistListResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainers")
public class TrainerRestController {
    @Autowired
    UserManagementService userManagementService;

    @GetMapping("")
    public SpecialistListResponse getGeneralInfoAboutAllTrainers() {
        List<UserDTO> trainersList = userManagementService.getGeneralInfoABoutAllTrainers();
        return new SpecialistListResponse(trainersList);
    }


}
