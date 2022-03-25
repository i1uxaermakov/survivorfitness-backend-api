package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.*;
import com.changeplusplus.survivorfitness.backendapi.service.UserManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "User Controller", description = "Endpoints for retrieval of information about users in general.")
public class UserRestController {

    @Autowired
    private UserManagementService userManagementService;

    @GetMapping("/{userId}")
    @ApiOperation(value = "Finds detailed info about a specific user.",
            notes = "The endpoint is available to all authenticated users.",
            response = UserResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    public UserResponse getDetailedInfoAboutSpecificUser(@PathVariable Integer userId) {
        UserDTO userDTO = userManagementService.getUserInfoById(userId);
        return new UserResponse(userDTO);
    }

    @PostMapping("")
    @ApiOperation(value = "Creates a new user and returns it back to the caller.",
            response = UserResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    public UserResponse createNewUser(@RequestBody CreateOrEditUserRequest createOrEditUserRequestBody) {
        UserDTO newUser = userManagementService.createNewUser(
                createOrEditUserRequestBody.getUser(),
                createOrEditUserRequestBody.getLocationAssignments());
        return new UserResponse(newUser);
    }

    @PostMapping("/{userId}/change_password")
    public ResponseEntity changePassword(@PathVariable int userId,
                                         @RequestBody ChangePasswordRequest changePasswordRequestBody) {
        userManagementService.changePassword(userId,
                changePasswordRequestBody.getCurrentPassword(),
                changePasswordRequestBody.getNewPassword());

        return ResponseEntity.ok("Password has been successfully changed!");
    }


    /**
     * This endpoint can be used to update personal info of the user specified by @param userId.
     * Note: this endpoint does not update the password of the user – use /users/{userId}/change_password for that
     * Note: this endpoint does not update the email and userId because those values are immutable.
     * The operation is only allowed for SuperAdmins.
     * @param userId ID of the user to be updated.
     * @param createOrEditUserRequestBody The body of the request. Contains the fields 'user' (personal
     *                                    info about the user) and 'locationAssignments' (locations
     *                                    and roles the user is assigned to)
     * @return User info as it is now saved in the database
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public UserResponse updateUser(@PathVariable Integer userId, @RequestBody CreateOrEditUserRequest createOrEditUserRequestBody) {
        if(!Objects.equals(userId, createOrEditUserRequestBody.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in URL and id of the user are different.");
        }

        UserDTO updatedUser = userManagementService.updateUser(
                createOrEditUserRequestBody.getUser(),
                createOrEditUserRequestBody.getLocationAssignments());
        return new UserResponse(updatedUser);
    }
  
  
    /**
     * Find all users in the database. This operation is only allowed
     * for Super Admins.
     * @return A UserListResponse with the list of all users inside
     */
    @GetMapping("")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public UserListResponse getAllUsers() {
        return new UserListResponse(userManagementService.getAllUsers());
    }
}
