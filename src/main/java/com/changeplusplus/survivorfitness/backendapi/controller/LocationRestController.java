package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.controller.payload.InfoAboutAllLocationsResponse;
import com.changeplusplus.survivorfitness.backendapi.controller.payload.InfoAboutParticularLocationResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.service.LocationManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationRestController {

    @Autowired
    private LocationManagementService locationManagementService;

    @GetMapping("/")
    public InfoAboutAllLocationsResponse getGeneralInfoAboutAllLocations() {
        List<LocationDTO> locationDTOs = locationManagementService.getGeneralInfoAboutAllLocations();
        return new InfoAboutAllLocationsResponse(locationDTOs);
    }

    @PostMapping("/")
    public String addNewLocation() {
        return "confirmation";
    }


    @GetMapping("/{locationId}")
    public InfoAboutParticularLocationResponse getInfoAboutSpecificLocation(@PathVariable("locationId") Integer locationId) {
        LocationDTO locationDTO = locationManagementService.getInfoAboutParticularLocation(locationId);
        return new InfoAboutParticularLocationResponse(locationDTO);
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
