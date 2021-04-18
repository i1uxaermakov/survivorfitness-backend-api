package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.controller.payload.SpecialistListResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.service.UserManagementService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dietitians")
public class DietitianRestController {

    @Autowired
    UserManagementService userManagementService;

    @GetMapping("")
    @ApiOperation(value = "Finds general info about all dietitians", response = SpecialistListResponse.class)
    public SpecialistListResponse getGeneralInfoAboutAllDietitians() {
        List<UserDTO> dietitiansList = userManagementService.getGeneralInfoABoutAllDietitians();
        return new SpecialistListResponse(dietitiansList);
    }
}
