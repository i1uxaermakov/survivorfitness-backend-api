package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantListResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantDTO;
import com.changeplusplus.survivorfitness.backendapi.service.ParticipantManagementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/participants")
public class ParticipantRestController {

    @Autowired
    private ParticipantManagementService participantManagementService;

    @GetMapping("")
    @ApiOperation(value = "Finds general info about all participants", response = ParticipantListResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ParticipantListResponse getGeneralInfoAboutAllParticipants() {
        List<ParticipantDTO> participantsInfo = participantManagementService.getGeneralInfoAboutAllParticipants();
        return new ParticipantListResponse(participantsInfo);
    }

//    @PostMapping("/")
//    public String addNewParticipant() {
//        return "confirmation of participant added";
//    }

    @GetMapping("/{participantId}")
    @ApiOperation(value = "Finds detailed info about a specific participant",
            notes = "Provide an ID to look up a specific participant. If a participant with a specified ID doesn't exist, the endpoint returns paticipant = null",
            response = ParticipantResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR', 'TRAINER', 'DIETITIAN')")
    public ParticipantResponse getInfoAboutSpecificParticipant(
            @ApiParam(value = "ID value for the participant you need to retrieve", required = true)
            @PathVariable("participantId") Integer participantId) {
        ParticipantDTO participantDTO = participantManagementService.getParticipantInfoById(participantId);
        return new ParticipantResponse(participantDTO);
    }

//    @PutMapping("/{participantId}")
//    public String updateInfoAboutSpecificParticipant(@PathVariable("participantId") Integer participantId) {
//        return "confirmation of participant being update";
//    }
//
//    @DeleteMapping("/{participantId}")
//    public String deactivateSpecificParticipant(@PathVariable("participantId") Integer participantId) {
//        return "deactivation of specific participant";
//    }
}
