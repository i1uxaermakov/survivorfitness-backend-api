package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationListResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.LocationResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.service.LocationManagementService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationRestController {

    @Autowired
    private LocationManagementService locationManagementService;

    @GetMapping("/")
    @ApiOperation(value = "Finds general info about all locations", response = LocationListResponse.class)
    public LocationListResponse getGeneralInfoAboutAllLocations() {
        List<LocationDTO> locationDTOs = locationManagementService.getGeneralInfoAboutAllLocations();
        return new LocationListResponse(locationDTOs);
    }

//    @PostMapping("/")
//    public String addNewLocation() {
//        return "confirmation";
//    }


    @GetMapping("/{locationId}")
    @ApiOperation(value = "Finds detailed info about specific location",
            notes = "Provide an ID to look up a specific location. If a location with a specified ID doesn't exist, the endpoint returns location = null",
            response = LocationResponse.class)
    public LocationResponse getInfoAboutSpecificLocation(@ApiParam(value = "ID value for the location you need to retrieve", required = true)
                                                             @PathVariable("locationId") Integer locationId) {
        LocationDTO locationDTO = locationManagementService.getInfoAboutParticularLocation(locationId);
        return new LocationResponse(locationDTO);
    }

//    @PutMapping("/{locationId}")
//    public String updateInfoAboutSpecificLocation(@PathVariable("locationId") Long locationId) {
//        return "update confirmation";
//    }
//
//    @DeleteMapping("/{locationId}")
//    public String deactivateSpecificLocation(@PathVariable("locationId") Long locationId) {
//        return "delete confirmation";
//    }
//
//    @GetMapping("/{locationId}/trainers")
//    public String getClientsAssignedToSpecificLocation(@PathVariable("locationId") Long locationId) {
//        return "list of trainers in this location";
//    }
}
