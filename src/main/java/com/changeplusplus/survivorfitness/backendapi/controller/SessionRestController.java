package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.SessionDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.SessionResponse;
import com.changeplusplus.survivorfitness.backendapi.service.SessionManagementService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

/**
 * SessionRestController – provides the endpoint to update information from a participant session
 */
@RestController
@RequestMapping("/api/v1/sessions")
public class SessionRestController {

    @Autowired
    private SessionManagementService sessionManagementService;


    /**
     * updateSession - endpoint to update session info
     * @param sessionId - id of the session to update
     * @param sessionDtoToUpdate - session object with new, updated information
     * @return newly updated sessionDTO retrieved from the database
     */
    @PutMapping("/{sessionId}")
    @ApiOperation(value = "Update a session with a participant",
            notes = "Available to all users",
            response = ParticipantResponse.class)
    public SessionResponse updateSession(@PathVariable Integer sessionId,
                                         @RequestBody SessionDTO sessionDtoToUpdate) {
        if(!Objects.equals(sessionId, sessionDtoToUpdate.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id in URL and id of the session are different.");
        }

        SessionDTO updatedSessionDTO = sessionManagementService.updateSession(sessionDtoToUpdate);
        return new SessionResponse(updatedSessionDTO);
    }


}
