package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantListResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantDTO;
import com.changeplusplus.survivorfitness.backendapi.service.ParticipantManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/participants")
@Api(tags = "Participant Controller", description = "Endpoints for retrieval and management of information about participants.")
public class ParticipantRestController {

    @Autowired
    private ParticipantManagementService participantManagementService;

    @GetMapping("")
    @ApiOperation(value = "Finds general info about participants",
            notes = "This endpoint allows to select participants by dietitian office, gym, trainer, and dietitian. However, the endpoint only supports the selection of participants by one parameter. " +
                    "If multiple parameters are provided in a request, the method will return a Bad Request response. If none of the parameters are provided, the endpoint returns the list of all existing participants.\n\n" +
                    "Different types of users have different privileges on using this endpoint:\n" +
                    "1. Selecting participants by dietitian office and gym is available to users with LOCATION_ADMINISTRATOR, SUPER_ADMIN roles.\n" +
                    "2. Selecting participants by User ID of a dietitian is available to users with DIETITIAN, LOCATION_ADMINISTRATOR, SUPER_ADMIN roles.\n" +
                    "3. Selecting participants by User ID of a trainer is available to users with TRAINER, LOCATION_ADMINISTRATOR, SUPER_ADMIN roles.",
            response = ParticipantListResponse.class)
    public ResponseEntity<?> getGeneralInfoAboutParticipants(
            @ApiParam(value = "Location ID of the Dietitian Office", required = false) @RequestParam(name="dietitianOfficeId", required = false) Integer dietitianOfficeId,
            @ApiParam(value = "Location ID of the Gym Location", required = false) @RequestParam(name="gymId", required = false) Integer gymId,
            @ApiParam(value = "User ID of the Dietitian", required = false) @RequestParam(name="dietitianUserId", required = false) Integer dietitianUserId,
            @ApiParam(value = "User ID of the Trainer", required = false) @RequestParam(name="trainerUserId", required = false) Integer trainerUserId) {
        if(dietitianOfficeId != null && areAllObjectsNull(Arrays.asList(gymId, dietitianUserId, trainerUserId))) {
            return new ResponseEntity<>(getParticipantsAtSpecificDietitianOffice(dietitianOfficeId), HttpStatus.OK);
        }
        else if(gymId != null && areAllObjectsNull(Arrays.asList(dietitianOfficeId, dietitianUserId, trainerUserId))) {
            return new ResponseEntity<>(getParticipantsAtSpecificGym(gymId), HttpStatus.OK);
        }
        else if(dietitianUserId != null && areAllObjectsNull(Arrays.asList(dietitianOfficeId, gymId, trainerUserId))) {
            return new ResponseEntity<>(getParticipantsAssignedToSpecificDietitian(dietitianUserId), HttpStatus.OK);
        }
        else if(trainerUserId != null && areAllObjectsNull(Arrays.asList(dietitianOfficeId, dietitianUserId, gymId))) {
            return new ResponseEntity<>(getParticipantsAssignedToSpecificTrainer(trainerUserId), HttpStatus.OK);
        }
        else if(areAllObjectsNull(Arrays.asList(gymId, dietitianOfficeId, dietitianUserId, trainerUserId))) {
            return new ResponseEntity<>(getGeneralInfoAboutAllParticipants(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity("This endpoint only supports sorting of participants by one parameter", HttpStatus.BAD_REQUEST);
        }
    }

//    @PostMapping("/")
//    public String addNewParticipant() {
//        return "confirmation of participant added";
//    }

    @GetMapping("/{participantId}")
    @ApiOperation(value = "Finds detailed info about a specific participant",
            notes = "Provide an ID to look up a specific participant. If a participant with a specified ID doesn't exist, the endpoint returns paticipant = null.\n" +
                    "The endpoint is available to all authenticated users.",
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




    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    private ParticipantListResponse getParticipantsAtSpecificDietitianOffice(Integer dietitianOfficeId) {
        List<ParticipantDTO> participantDTOList = participantManagementService.getParticipantsAtSpecificDietitianOffice(dietitianOfficeId);
        return new ParticipantListResponse(participantDTOList);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    private ParticipantListResponse getParticipantsAtSpecificGym(Integer gymId) {
        List<ParticipantDTO> participantDTOList = participantManagementService.getParticipantsAtSpecificTrainerGym(gymId);
        return new ParticipantListResponse(participantDTOList);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR', 'DIETITIAN')")
    private ParticipantListResponse getParticipantsAssignedToSpecificDietitian(Integer dietitianUserId) {
        List<ParticipantDTO> participantDTOList = participantManagementService.getParticipantsAssignedToSpecificDietitian(dietitianUserId);
        return new ParticipantListResponse(participantDTOList);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    private ParticipantListResponse getParticipantsAssignedToSpecificTrainer(@RequestParam(name="trainerUserId") Integer trainerUserId) {
        List<ParticipantDTO> participantDTOList = participantManagementService.getParticipantsAssignedToSpecificTrainer(trainerUserId);
        return new ParticipantListResponse(participantDTOList);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    private ParticipantListResponse getGeneralInfoAboutAllParticipants() {
        List<ParticipantDTO> participantsInfo = participantManagementService.getGeneralInfoAboutAllParticipants();
        return new ParticipantListResponse(participantsInfo);
    }

    private boolean areAllObjectsNull(List<Object> list) {
        return list.stream().noneMatch(Objects::nonNull);
    }
}
