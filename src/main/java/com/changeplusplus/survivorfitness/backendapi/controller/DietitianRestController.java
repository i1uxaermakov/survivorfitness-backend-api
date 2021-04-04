package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.controller.payload.InfoAboutAllDietitiansResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.service.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dieticians")
public class DietitianRestController {

    @Autowired
    UserManagementService userManagementService;

    @GetMapping("")
    public InfoAboutAllDietitiansResponse getGeneralInfoAboutAllDieticians() {
        List<UserDTO> dietitiansList = userManagementService.getGeneralInfoABoutAllDietitians();
        return new InfoAboutAllDietitiansResponse(dietitiansList);
    }
}
