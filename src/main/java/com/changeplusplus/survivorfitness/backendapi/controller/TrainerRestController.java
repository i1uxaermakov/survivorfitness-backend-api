package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.UserListResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRoleType;
import com.changeplusplus.survivorfitness.backendapi.service.UserManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Trainer Controller - used to retrieve all info about trainers (only retrieval happens here, creation of
 * trainers and all users in general is in the UserRestController)
 */
@RestController
@RequestMapping("/api/v1/trainers")
@Api(tags = "Trainer Controller", description = "Endpoints for retrieval of information about trainers.")
public class TrainerRestController {
    @Autowired
    UserManagementService userManagementService;

    /**
     * getInfoAboutTrainers - retrieves information about trainers in general, or at a specific location
     * @param locationId - an optional parameter to retrieve trainers at a specific location. If not provided,
     *                   the endpoint will return information about all trainers
     * @return list of trainers requested (all trainers if no locationId is specified, or
     * only those at the specified location)
     */
    @GetMapping("")
    @ApiOperation(value = "Finds general info about trainers.",
            notes = "If locationId is specified, the endpoint returns information about trainers in that location. Otherwise, the endpoint returns information about all trainers.\n" +
                    "The endpoint is available to users with roles SUPER_ADMIN and LOCATION_ADMINISTRATOR.",
            response = UserListResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    public UserListResponse getInfoAboutTrainers(@RequestParam(name="locationId", required = false) Integer locationId,
                                                 @RequestParam(required = false, defaultValue="false") boolean includeDisabledAccounts) {
        if(locationId != null) {
            List<UserDTO> trainersAtLocation = userManagementService.getGeneralInfoAboutSpecialistsOfSpecificTypeInSpecificLocation(
                    UserRoleType.TRAINER, locationId, includeDisabledAccounts);
            return new UserListResponse(trainersAtLocation);
        }
        else {
            List<UserDTO> trainersList = userManagementService.getGeneralInfoAboutAllSpecialistsOfSpecificType(UserRoleType.TRAINER, includeDisabledAccounts);
            return new UserListResponse(trainersList);
        }
    }


}
