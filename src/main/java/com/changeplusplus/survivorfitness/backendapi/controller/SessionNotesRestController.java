package com.changeplusplus.survivorfitness.backendapi.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session-notes")
public class SessionNotesRestController {

    @GetMapping("/{notesId}")
    public String getInfoAboutSpecificSessionNotes(@PathVariable("notesId") Long notesId) {
        return "info about specific session notes";
    }

    @GetMapping("/")
    public String getNotesCreatedForSpecificClient(@RequestParam("clientId") Long clientId) { //or QueryParam????
        return "all notes created for a specific client";
    }


}
