package com.changeplusplus.survivorfitness.backendapi.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
public class LocationRestController {

    @GetMapping("/")
    public String getGeneralInfoAboutAllLocations() {
        return "all locations";
    }

    @PostMapping("/")
    public String addNewLocation() {
        return "confirmation";
    }


    @GetMapping("/{locationId}")
    public String getInfoAboutSpecificLocation(@PathVariable("locationId") Long locationId) {
        return "Info About specific location";
    }

    @PutMapping("/{locationId}")
    public String updateInfoAboutSpecificLocation(@PathVariable("locationId") Long locationId) {
        return "update confirmation";
    }

    @DeleteMapping("/{locationId}")
    public String deactivateSpecificLocation(@PathVariable("locationId") Long locationId) {
        return "delete confirmation";
    }

    @GetMapping("/{locationId}/trainers")
    public String getClientsAssignedToSpecificLocation(@PathVariable("locationId") Long locationId) {
        return "list of trainers in this location";
    }
}
