package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.UserListResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRoleType;
import com.changeplusplus.survivorfitness.backendapi.service.UserManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Dietitian Controller - used to retrieve all info about dietitians (only retrieval happens here, creation of
 * dietitians and all users in general is in the UserRestController)
 */
@RestController
@RequestMapping("/api/v1/dietitians")
@Api(tags = "Dietitian Controller", description = "Endpoints for retrieval of information about dietitians.")
public class DietitianRestController {

    @Autowired
    UserManagementService userManagementService;

    /**
     * getInfoAboutDietitians - retrieves information about dietitians in general, or at a specific location
     * @param locationId - an optional parameter to retrieve dietitians at a specific location. If not provided,
     *                   the endpoint will return information about all dietitians
     * @return list of dietitians requested (all dietitians if no locationId is specified, or
     * only those at the specified location)
     */
    @GetMapping("")
    @ApiOperation(value = "Finds general info about dietitians.",
            notes = "If locationId is specified, the endpoint returns information about dietitians in that location. " +
                    "Otherwise, the endpoint returns information about all dietitians.\n" +
                    "The endpoint is available to users with roles SUPER_ADMIN and LOCATION_ADMINISTRATOR.",
            response = UserListResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    public UserListResponse getInfoAboutDietitians(@RequestParam(name="locationId", required = false) Integer locationId) {
        if(locationId != null) {
            List<UserDTO> dietitiansAtLocationList = userManagementService.getGeneralInfoAboutSpecialistsOfSpecificTypeInSpecificLocation(UserRoleType.DIETITIAN, locationId);
            return new UserListResponse(dietitiansAtLocationList);
        }
        else {
            List<UserDTO> dietitiansList = userManagementService.getGeneralInfoAboutAllSpecialistsOfSpecificType(UserRoleType.DIETITIAN);
            return new UserListResponse(dietitiansList);
        }
    }
}
