package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.controller.payload.InfoAboutAllParticipantsResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.Participant;
import com.changeplusplus.survivorfitness.backendapi.service.ParticipantManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.changeplusplus.survivorfitness.backendapi.controller.payload.InfoAboutParticularParticipantResponse;

import java.util.List;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

    @Autowired
    private ParticipantManagementService participantManagementService;

    @GetMapping("")
    public InfoAboutAllParticipantsResponse getGeneralInfoAboutAllParticipants() {
        List<ParticipantDTO> participantsInfo = participantManagementService.getGeneralInfoAboutAllParticipants();
        return new InfoAboutAllParticipantsResponse(participantsInfo);
    }

    @PostMapping("/")
    public String addNewParticipant() {
        return "confirmation of participant added";
    }

    @GetMapping("/{participantId}")
    public InfoAboutParticularParticipantResponse getInfoAboutSpecificParticipant(@PathVariable("participantId") Integer participantId) {
        ParticipantDTO participantDTO = participantManagementService.getParticipantInfoById(participantId);
        return new InfoAboutParticularParticipantResponse(participantDTO);
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
