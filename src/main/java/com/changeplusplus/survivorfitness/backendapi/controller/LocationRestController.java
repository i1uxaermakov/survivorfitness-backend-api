package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.controller.payload.LocationListResponse;
import com.changeplusplus.survivorfitness.backendapi.controller.payload.LocationResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.service.LocationManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationRestController {

    @Autowired
    private LocationManagementService locationManagementService;

    @GetMapping("/")
    public LocationListResponse getGeneralInfoAboutAllLocations() {
        List<LocationDTO> locationDTOs = locationManagementService.getGeneralInfoAboutAllLocations();
        return new LocationListResponse(locationDTOs);
    }

    @PostMapping("/")
    public String addNewLocation() {
        return "confirmation";
    }


    @GetMapping("/{locationId}")
    public LocationResponse getInfoAboutSpecificLocation(@PathVariable("locationId") Integer locationId) {
        LocationDTO locationDTO = locationManagementService.getInfoAboutParticularLocation(locationId);
        return new LocationResponse(locationDTO);
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
