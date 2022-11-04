package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.*;
import com.changeplusplus.survivorfitness.backendapi.service.UserManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
@RequestMapping("/api/v1/users")
@Api(tags = "User Controller", description = "Endpoints for retrieval of information about users in general.")
public class UserRestController {

    @Autowired
    private UserManagementService userManagementService;

    /**
     * Domain name of where the application is hosted. The value for this field is
     * retrieved from /resources/application.properties file. This variable is used
     * to create links to the backend (for example, a link to reset the password)
     */
    @Value("${survivorfitness-backend.domain-name}")
    private String SFF_DOMAIN_NAME;

    @GetMapping("/{userId}")
    @ApiOperation(value = "Finds detailed info about a specific user.",
            notes = "The endpoint is available to all authenticated users.",
            response = UserResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    @ResponseBody
    public UserResponse getDetailedInfoAboutSpecificUser(@PathVariable Integer userId) {
        UserDTO userDTO = userManagementService.getUserInfoById(userId);
        return new UserResponse(userDTO);
    }

    @PostMapping("")
    @ApiOperation(value = "Creates a new user and returns it back to the caller.",
            response = UserResponse.class)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    @ResponseBody
    public UserResponse createNewUser(@RequestBody CreateOrEditUserRequest createOrEditUserRequestBody) {
        UserDTO newUser = userManagementService.createNewUser(
                createOrEditUserRequestBody.getUser(),
                createOrEditUserRequestBody.getLocationAssignments());
        return new UserResponse(newUser);
    }

    @PostMapping("/{userId}/change_password")
    @ResponseBody
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
     * @param userId ID of the user to be updated.
     * @param createOrEditUserRequestBody The body of the request. Contains the fields 'user' (personal
     *                                    info about the user) and 'locationAssignments' (locations
     *                                    and roles the user is assigned to)
     * @return User info as it is now saved in the database
     */
    @PutMapping("/{userId}")
    @ResponseBody
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
     * Find all enabled users in the database. This operation is only allowed
     * for Super Admins.
     * @param includeDisabledAccounts if true, the response will include the archived users
     * @return A UserListResponse with the list of all users inside
     */
    @GetMapping("")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @ResponseBody
    public UserListResponse getAllUsers(@RequestParam(required = false, defaultValue="false") boolean includeDisabledAccounts) {
        return new UserListResponse(userManagementService.getAllUsers(includeDisabledAccounts));
    }


    /**
     * An endpoint to reset the password of a user identified by the @param token.
     * The endpoint can be used by the users that are not logged in.
     * @param token The token that identifies the user whose password should be changed
     * @return A string saying that the password has been successfully reset.
     */
    @GetMapping("/reset_password")
    @ResponseBody
    public ResponseEntity<String> resetPassword(@RequestParam String token) {
        userManagementService.resetUserPassword(token);
        return ResponseEntity.ok("The password for user with token " + token +
                " has been successfully reset!");
    }

    /**
     * An endpoint to request the reset of the password of the user identified by @param email. The user will
     * receive an email with a link that will actually reset the password.
     * The endpoint can be used by the users that are not logged in.
     * @param email Email of the user whose password should be changed.
     * @return A string saying that the user will receive an email with a link that actually resets
     * their password
     */
    @GetMapping("/request_password_reset")
    @ResponseBody
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        userManagementService.requestPasswordReset(email);
        return ResponseEntity.ok("The password reset has been requested for user " + email +
                ". The user will receive an email with a link that will actually reset the password.");
    }


    /**
     * An endpoint to receive the page that has a button to reset the password. A link to this page is sent to the
     * user in an email when they request a password reset. Page has been implemented because some mail applications
     * visit the link to scan for viruses, thereby resetting the password before the user themselves visit the link.
     * @param token The token that identifies the user whose password should be changed
     * @param model Model object that allows to pass arguments to Views
     * @return A page based on the /resources/templates/reset_password_template.html template
     */
    @GetMapping("/reset_password_page")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("reset_password_link", "http://" + SFF_DOMAIN_NAME + "/api/v1/users/reset_password?token=" + token);
        return "reset_password_template";
    }


    /**
     * An endpoint to "delete" an account of the user. We cannot actually delete it from the database because the
     * user might have a lot of data associated with them via foreign keys. Before deleting the user we would have
     * to delete/change all the data associated with them. Instead of deleting the user, we mark their account as
     * disabled. Users will not be able to log into the disabled account. The account also will not show up in the
     * results of queries like getAllUsers unless the requester specifies that they want the disabled accounts to
     * be included in the results. Only SUPER ADMINS and LOCATION_ADMINISTRATORs are allowed to disable users'
     * accounts.
     * @param userId ID of the user to disable
     * @return User DTO with information about the disabled user account.
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'LOCATION_ADMINISTRATOR')")
    @ResponseBody
    public UserDTO deleteUser(@PathVariable Integer userId) {
        return userManagementService.disableUserAccount(userId);
    }
}
