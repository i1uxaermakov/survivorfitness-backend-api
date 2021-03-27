package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.entity.Participant;
import com.changeplusplus.survivorfitness.backendapi.service.ParticipantManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.changeplusplus.survivorfitness.backendapi.controller.payload.InfoAboutParticularParticipantResponse;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

    @Autowired
    private ParticipantManagementService participantManagementService;

    @GetMapping("/")
    public String getGeneralInfoAboutAllParticipants() {
        return "general info about all participants";
    }

    @PostMapping("/")
    public String addNewParticipant() {
        return "confirmation of participant added";
    }

    @GetMapping("/{participantId}")
    public InfoAboutParticularParticipantResponse getInfoAboutSpecificParticipant(@PathVariable("participantId") Integer participantId) {
        Participant participant = participantManagementService.getParticipantInfoById(participantId);
        return new InfoAboutParticularParticipantResponse(participant);
    }

    @PutMapping("/{participantId}")
    public String updateInfoAboutSpecificParticipant(@PathVariable("participantId") Integer participantId) {
        return "confirmation of participant being update";
    }

    @DeleteMapping("/{participantId}")
    public String deactivateSpecificParticipant(@PathVariable("participantId") Integer participantId) {
        return "deactivation of specific participant";
    }
}
