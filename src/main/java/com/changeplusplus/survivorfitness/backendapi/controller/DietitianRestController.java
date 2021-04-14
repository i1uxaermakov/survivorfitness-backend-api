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
@RequestMapping("/dietitians")
public class DietitianRestController {

    @Autowired
    UserManagementService userManagementService;

    @GetMapping("")
    public SpecialistListResponse getGeneralInfoAboutAllDieticians() {
        List<UserDTO> dietitiansList = userManagementService.getGeneralInfoABoutAllDietitians();
        return new SpecialistListResponse(dietitiansList);
    }
}
