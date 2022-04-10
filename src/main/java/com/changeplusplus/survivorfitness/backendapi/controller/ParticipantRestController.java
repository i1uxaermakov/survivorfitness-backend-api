package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.*;
import com.changeplusplus.survivorfitness.backendapi.entity.Session;
import com.changeplusplus.survivorfitness.backendapi.service.ParticipantManagementService;
import com.changeplusplus.survivorfitness.backendapi.service.SessionManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * ParticipantRestController – endpoints to create and edit all information about participants
 */
@RestController
@RequestMapping("/api/v1/participants")
@Api(tags = "Participant Controller", description = "Endpoints for retrieval and management of information about participants.")
public class ParticipantRestController {

    @Autowired
    private ParticipantManagementService participantManagementService;

    @Autowired
    private SessionManagementService sessionManagementService;

    /**
     * getGeneralInfoAboutParticipants - an API to retrieve information about all participants, or
     * filter by one parameter (details in the APIOperation notes)
     * can only use at most 1 of the parameters below
     * @param dietitianOfficeId - optional parameter that indicates a dietitianOffice ID to retrieve info from
     * @param gymId - optional parameter that indicates a gym ID to retrieve info from
     * @param dietitianUserId - optional parameter that indicates a dietitian to retrieve participants info from
     * @param trainerUserId - optional parameter that indicates a trainer user ID to retrieve
     * @return - list of information about the participants
     */
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
            return new ResponseEntity<>("This endpoint only supports sorting of participants by one parameter", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * addNewParticipant - creates a new participant in the database along with a Program for them
     * @param createParticipantRequest - an object that includes all info about the participant,
     *                                along with information about training sessions and when to take measurements
     * @return participant object retrieved from database
     */
    @PostMapping("")
    @ApiOperation(value = "Creates a Participant and the associated Program for the participant",
            response = ParticipantResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ParticipantResponse addNewParticipant(@ApiParam(value = "Object containing information about the participant and the program for him/her", required = true)
                                                     @RequestBody CreateParticipantRequest createParticipantRequest) {
        ParticipantDTO participantDTO = participantManagementService.createNewParticipant(
                createParticipantRequest.getParticipant(),
                createParticipantRequest.getNumberOfTrainerSessions(),
                createParticipantRequest.getSessionsIndicesWhenMeasurementsAreTaken(),
                createParticipantRequest.getMeasurements(),
                createParticipantRequest.getNumberOfDietitianSessions());

        return new ParticipantResponse(participantDTO);
    }


    /**
     * getInfoAboutSpecificParticipant - receives detailed info about a specific participant
     * @param participantId - required - participant to retrieve info about
     * @return - participantDTO retrieved from DB (detailed info about the participant)
     */
    @GetMapping("/{participantId}")
    @ApiOperation(value = "Finds detailed info about a specific participant",
            notes = "Provide an ID to look up a specific participant. If a participant with a specified ID doesn't exist," +
                    " the endpoint returns participant = null.\n" +
                    "The endpoint is available to all authenticated users.",
            response = ParticipantResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR', 'TRAINER', 'DIETITIAN')")
    public ParticipantResponse getInfoAboutSpecificParticipant(
            @ApiParam(value = "ID value for the participant you need to retrieve", required = true)
            @PathVariable("participantId") Integer participantId) {
        ParticipantDTO participantDTO = participantManagementService.getParticipantInfoById(participantId);
        return new ParticipantResponse(participantDTO);
    }


    /**
     * getTrainerSessionNotesOfParticipant – gets all notes from TRAINER sessions w/ participants
     * @param participantId - ID of the participant to get trainer data about
     * @return list of SessionDTO's which contains information about the participant's sessions
     */
    @GetMapping("/{participantId}/trainer-notes")
    @ApiOperation(value = "Find trainer session notes for the participant",
            notes = "Returns participant = null if invalid participant ID provided. " +
                    "This endpoint is available to all participants. ",
            response = SessionListResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR', 'TRAINER', 'DIETITIAN')")
    public SessionListResponse getTrainerSessionNotesOfParticipant(
            @ApiParam(value = "ID value for the participant you need to retrieve", required = true)
            @PathVariable("participantId") Integer participantId) {
        List<SessionDTO> trainerSessions = sessionManagementService.getTrainerSessionsOfParticipant(participantId);
        return new SessionListResponse(trainerSessions, null);
    }

    /**
     * getDietitianSessionNotesOfParticipant - gets all DIETITIAN notes from sessions w/ participants
     * @param participantId - ID of the participant to get trainer data about
     * @return list of SessionDTO's which contains information about the participant's DIETITIAN sessions
     */
    @GetMapping("/{participantId}/dietitian-notes")
    @ApiOperation(value = "Find dietitian session notes for the participant",
            notes = "This endpoint is available to all participants.",
            response = SessionListResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR', 'TRAINER', 'DIETITIAN')")
    public SessionListResponse getDietitianSessionNotesOfParticipant(
            @ApiParam(value = "ID value for the participant you need to retrieve", required = true)
            @PathVariable("participantId") Integer participantId) {
        List<SessionDTO> dietitianSessions = sessionManagementService.getDietitianSessionsOfParticipant(participantId);
        return new SessionListResponse(null, dietitianSessions);
    }

    /**
     * getAllSessionNotesOfParticipant –– gets both trainer and dietitian notes for a participant
     * @param participantId required ID Value of the participant whose notes you are retrieving
     * @return a pair of SessionDTOs - <TrainerSessionNotes, DietitianSessionNotes>
     */
    @GetMapping("/{participantId}/all-notes")
    @ApiOperation(value = "Find all session notes for the participant",
            response = SessionListResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR', 'TRAINER', 'DIETITIAN')")
    public SessionListResponse getAllSessionNotesOfParticipant(
            @ApiParam(value = "ID value for the participant you need to retrieve", required = true)
            @PathVariable("participantId") Integer participantId) {
        Pair<List<SessionDTO>,List<SessionDTO>> listPair = sessionManagementService.getAllSessionNotesOfParticipant(participantId);
        return new SessionListResponse(listPair.getFirst(), listPair.getSecond());
    }


    /** updateParticipant - endpoint to update info about a participant
     *
     * @param participantId - id of the participant to update
     * @param participantDTO - DTO of the new participant object
     * @return - updated participantDTO, retrieved from the database
     */
    @PutMapping("/{participantId}")
    @ApiOperation(value = "Edits info about a specific participant",
            notes = "This endpoint is available to super admins only.",
            response = ParticipantResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR', 'TRAINER', 'DIETITIAN')")
    public ParticipantResponse updateParticipant(@PathVariable("participantId") Integer participantId,
                                    @ApiParam(value = "Object containing information about the participant" +
                                            " and their program", required = true)
                                    @RequestBody ParticipantDTO participantDTO) {
        if(!Objects.equals(participantId, participantDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in URL (" +
                    participantId +
                    ") and ID of the session (" +
                    participantDTO.getId() + ") do not match.");
        }
        ParticipantDTO updatedParticipantDTO = participantManagementService.editParticipant(participantDTO);
        return new ParticipantResponse(updatedParticipantDTO);
    }

//
//    @DeleteMapping("/{participantId}")
//    public String deactivateSpecificParticipant(@PathVariable("participantId") Integer participantId) {
//        return "deactivation of specific participant";
//    }


    /**
     * helper method for getGeneralInfoAboutParticipants to retrieve participants at a dietitian office
     * @param dietitianOfficeId - id of the dietitianOffice to retrieve participant info about
     * @return - list of participants at a dietitian office
     */
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    private ParticipantListResponse getParticipantsAtSpecificDietitianOffice(Integer dietitianOfficeId) {
        List<ParticipantDTO> participantDTOList = participantManagementService.getParticipantsAtSpecificDietitianOffice(dietitianOfficeId);
        return new ParticipantListResponse(participantDTOList);
    }

    /**
     * helper method for getGeneralInfoAboutParticipants to retrieve participant info at a specific gym
     * @param gymId - id of the gym to retrieve participant info about
     * @return - list of participants at a dietitian office
     */
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    private ParticipantListResponse getParticipantsAtSpecificGym(Integer gymId) {
        List<ParticipantDTO> participantDTOList = participantManagementService.getParticipantsAtSpecificTrainerGym(gymId);
        return new ParticipantListResponse(participantDTOList);
    }

    /**
     * helper method for getGeneralInfoAboutParticipants to retrieve participant info with a specific dietitian
     * @param dietitianUserId - id of the dietitian to retrieve participant info about
     * @return - list of participants with that assigned dietitian
     */
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR', 'DIETITIAN')")
    private ParticipantListResponse getParticipantsAssignedToSpecificDietitian(Integer dietitianUserId) {
        List<ParticipantDTO> participantDTOList = participantManagementService.getParticipantsAssignedToSpecificDietitian(dietitianUserId);
        return new ParticipantListResponse(participantDTOList);
    }

    /**
     * helper method for getGeneralInfoAboutParticipants to retrieve participant info with a specific trainer
     * @param trainerUserId - id of the trainer to retrieve participant info about
     * @return - list of participants with that assigned trainer
     */
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    private ParticipantListResponse getParticipantsAssignedToSpecificTrainer(@RequestParam(name="trainerUserId") Integer trainerUserId) {
        List<ParticipantDTO> participantDTOList = participantManagementService.getParticipantsAssignedToSpecificTrainer(trainerUserId);
        return new ParticipantListResponse(participantDTOList);
    }

    /**
     * gets general info about all participants - when no query parameter ID is passed into the getParticipants call
     * @return list of all participants
     */
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    private ParticipantListResponse getGeneralInfoAboutAllParticipants() {
        List<ParticipantDTO> participantsInfo = participantManagementService.getGeneralInfoAboutAllParticipants();
        return new ParticipantListResponse(participantsInfo);
    }

    private boolean areAllObjectsNull(List<Object> list) {
        return list.stream().noneMatch(Objects::nonNull);
    }
}
