package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.SessionDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.SessionResponse;
import com.changeplusplus.survivorfitness.backendapi.service.SessionManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionRestController {

    @Autowired
    private SessionManagementService sessionManagementService;


    @PutMapping("/{sessionId}")
    public SessionResponse updateSession(@PathVariable Integer sessionId,
                                         @RequestBody SessionDTO sessionDtoToUpdate) {
        if(!Objects.equals(sessionId, sessionDtoToUpdate.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id in URL and id of the session are different.");
        }

        SessionDTO updatedSessionDTO = sessionManagementService.updateSession(sessionDtoToUpdate);
        return new SessionResponse(updatedSessionDTO);
    }


}
