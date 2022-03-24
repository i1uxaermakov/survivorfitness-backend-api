package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.*;
import com.changeplusplus.survivorfitness.backendapi.service.UserManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public UserResponse createNewUser(@RequestBody CreateUserRequest createUserRequestBody) {
        UserDTO newUser = userManagementService.createNewUser(
                createUserRequestBody.getUser(),
                createUserRequestBody.getLocationAssignments());
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
