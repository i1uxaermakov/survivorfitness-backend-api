package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationListResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.LocationResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.service.LocationManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

/**
 * LocationRestController - contains endpoints to create, read and update information about locations
 */
@RestController
@RequestMapping("/api/v1/locations")
@Api(tags = "Location Controller", description = "Endpoints for retrieval and management " +
        "of information about locations like Gyms and Dietitian Offices.")
public class LocationRestController {

    @Autowired
    private LocationManagementService locationManagementService;

    /** getGeneralInfoAboutAllLocations -
     * Gets all general information about the locations
     * @return a list of locationDTOs that contains basic info (id/name/type/address/administrator)
     * about all existing locations
     */
    @GetMapping("")
    @ApiOperation(value = "Finds general info about all locations",
            notes="The endpoint is available to users with the role SUPER_ADMIN.",
            response = LocationListResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public LocationListResponse getGeneralInfoAboutAllLocations() {
        List<LocationDTO> locationDTOs = locationManagementService.getGeneralInfoAboutAllLocations();
        return new LocationListResponse(locationDTOs);
    }

    /**
     * createNewLocation - creates a new Location entity in the database
     * Also assigns the admin to the location, and assigns them the LOCATION_ADMINISTRATOR role
     *
     * @param locationDTO - new object containing info about the location to create
     * @return a new locationDTO that includes information about the created location
     */
    @PostMapping("")
    @ApiOperation(value = "Creates a new Location and assigns an administrator to it.",
            notes="Creates a new Location entity and adds the LOCATION_ADMINISTRATOR role to the user specified as administrator.\n" +
                    "The endpoint is available to users with the role SUPER_ADMIN.",
            response = LocationListResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public LocationResponse createNewLocation(@RequestBody LocationDTO locationDTO) {
        LocationDTO savedLocationDTO = locationManagementService.createNewLocation(locationDTO);
        return new LocationResponse(savedLocationDTO);
    }

    /**
     * finds detailed info about a specific location
     * @param locationId - required ID of the location to return information about
     * @return A locationDTO that contains all information about the location
     */
    @GetMapping("/{locationId}")
    @ApiOperation(value = "Finds detailed info about specific location",
            notes = "Provide an ID to look up a specific location. If a location with a specified ID doesn't exist, the endpoint returns location = null.\n" +
                    "The endpoint is available to users with roles SUPER_ADMIN and LOCATION_ADMINISTRATOR.",
            response = LocationResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    public LocationResponse getInfoAboutSpecificLocation(@ApiParam(value = "ID value for the location you need to retrieve", required = true)
                                                             @PathVariable("locationId") Integer locationId) {
        LocationDTO locationDTO = locationManagementService.getInfoAboutParticularLocation(locationId);
        return new LocationResponse(locationDTO);
    }


    /** updateLocation - endpoint that is used to update information about a location
     *
     * @param locationId - ID of the location
     * @param locationDTO - DTO object of the location object to update
     * @return - locationDTO object that contains the updated object's information
     */
    @PutMapping("/{locationId}")
    @ApiOperation(value = "Edits  info about specific location",
            notes = "Provide ID of the location to update. " +
                    "If a location with a specified ID doesn't exist, the endpoint returns location = null.\n" +
                    "The endpoint is available to users with roles SUPER_ADMIN and LOCATION_ADMINISTRATOR.",
            response = LocationResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public LocationResponse updateLocation(@PathVariable("locationId") Integer locationId, @RequestBody LocationDTO locationDTO) {
        if(!Objects.equals(locationId, locationDTO.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in URL " +
                    locationId + " and ID of the session (" +
           locationDTO.getId() + ") do not match.");
        }

        LocationDTO updatedLocationDTO = locationManagementService.updateLocation(locationDTO);
        return new LocationResponse(updatedLocationDTO);
    }

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
