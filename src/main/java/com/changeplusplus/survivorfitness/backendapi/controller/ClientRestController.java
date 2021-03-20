package com.changeplusplus.survivorfitness.backendapi.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientRestController {

    @GetMapping("/")
    public String getGeneralInfoAboutAllClients() {
        return "general info about all clients";
    }

    @PostMapping("/")
    public String addNewClient() {
        return "confirmation of client added";
    }

    @GetMapping("/{clientId}")
    public String getInfoAboutSpecificClient(@PathVariable("clientId") Long clientId) {
        return "info about specific client";
    }

    @PutMapping("/{clientId}")
    public String updateInfoAboutSpecificClient(@PathVariable("clientId") Long clientId) {
        return "confirmation of client being update";
    }

    @DeleteMapping("/{clientId}")
    public String deactivateSpecificClient(@PathVariable("clientId") Long clientId) {
        return "deactivation of specific client";
    }
}
