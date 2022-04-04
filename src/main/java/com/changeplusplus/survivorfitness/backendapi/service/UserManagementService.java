package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationAssignmentDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.LocationAssignmentRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.LocationRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserManagementService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationAssignmentRepository locationAssignmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private static final int MINIMUM_PASSWORD_LENGTH = 8;


    /**
     * Finds all specialists that have the specified @param role type.
     * @param type Type of the Users needed (TRAINER, DIETITIAN, LOCATION_ADMINISTRATOR, or SUPER_ADMIN)
     * @return a list of users of the specified type
     */
    public List<UserDTO> getGeneralInfoAboutAllSpecialistsOfSpecificType(UserRoleType type) {
        if(Objects.equals(UserRoleType.SUPER_ADMIN, type)) {
            List<User> superAdmins = userRepository.findUsersBySuperAdmin(true);
            return getUserDTOsBasedOnUserEntities(superAdmins);
        }

        List<User> specialistEntities = userRepository.findDistinctUsersByLocationAssignmentsUserRoleType(type);
        return getUserDTOsBasedOnUserEntities(specialistEntities);
    }


    /**
     * Find all specialists that have the @param role in the location specified by @param locationId
     * @param role Role of the Users needed
     * @param locationId The location the users have to be assigned to
     * @return a list of users with the specified role in the specified location
     */
    public List<UserDTO> getGeneralInfoAboutSpecialistsOfSpecificTypeInSpecificLocation(UserRoleType role, Integer locationId) {
        List<User> userList = userRepository.findUsersByLocationAssignmentsUserRoleTypeAndLocationAssignmentsLocationId(role, locationId);
        return getUserDTOsBasedOnUserEntities(userList);
    }


    /**
     * Finds the User with the specified @param email.
     * @param email The email to look for
     * @return Info about the user that has the email. If such a user doesn't exist, returns null.
     */
    public UserDTO getUserInfoByEmail(String email) {
        User userEntity = userRepository.findUserByEmail(email);
        UserDTO userDTO = getUserDtoBasedOnUserEntity(userEntity);
        return userDTO;
    }


    /**
     * Converts a list of User entities into a list of UserDTOs.
     * @param users The list of User entities to convert
     * @return a list of UserDTOs that are based on User entities
     */
    private List<UserDTO> getUserDTOsBasedOnUserEntities(List<User> users) {
        // Convert the list into a stream of User objects
        return users.stream()

            // Convert each User object into a UserDTO object
            .map(this::getUserDtoBasedOnUserEntity)

            // Collect all UserDTOs into a list and return it
            .collect(Collectors.toList());
    }


    /**
     * Converts a User entity into a UserDTO. Returns the new UserDTO
     * @param userEntity the entity to convert to the DTO
     * @return UserDTO with the fields set to the values of User Entity
     */
    private UserDTO getUserDtoBasedOnUserEntity(User userEntity) {
        if(userEntity == null) {
            return null;
        }

        UserDTO userDTO = getConciseUserDTOBasedOnUserEntity(userEntity);
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setIsSuperAdmin(userEntity.isSuperAdmin());
        userDTO.setPhoneNumber(userEntity.getPhoneNumber());

        // Assign the roles of the userDTO based on Location Assignments
        List<LocationAssignment> locationAssignments = userEntity.getLocationAssignments();
        Set<String> userRolesSet = new HashSet<>();
        for(LocationAssignment la: locationAssignments) {
            userRolesSet.add(la.getUserRoleType().name());
        }

        // Add a Super Admin role to the userDTO if the user is super admin
        // This step is necessary because SUPER_ADMIN role is not indicated
        // in Location Assignments
        if(userEntity.isSuperAdmin()) {
            userRolesSet.add(UserRoleType.SUPER_ADMIN.name());
        }

        // Assign the list of roles of the user to the userDTO
        userDTO.setRoles(new ArrayList<>(userRolesSet));

        // Get Locations the user is assigned to from te Location Assignments
        Set<LocationDTO> locationDTOs = new HashSet<>();
        for(LocationAssignment la: locationAssignments) {
            Location location = la.getLocation();

            LocationDTO locationDTO = new LocationDTO()
                    .setId(location.getId())
                    .setName(location.getName())
                    .setType(location.getType().name());
            locationDTOs.add(locationDTO);
        }
        userDTO.setLocations(new ArrayList<>(locationDTOs));

        return userDTO;
    }


    /**
     * Creates a new User based on @param newUserData and sets its LocationAssignments based
     * on @param newUserLocationAssignments. Saves the new User in the database. Creates a random
     * password for the new user and sends it to them in an email.
     * @param newUserData UserDTO containing the data about the new User
     * @param newUserLocationAssignments contains the locations and the roles in those locations of the new user
     * @return UserDTO based on the new User entity as it has just been saved in the database
     */
    @Transactional
    public UserDTO createNewUser(UserDTO newUserData, List<LocationAssignmentDTO> newUserLocationAssignments) {
        // Check if the current user is allowed to add the new user and
        // get the user entity populated with the data about the new user
        User newUserEntity = getPopulatedUserEntityForCreatingUser(
                newUserData, newUserLocationAssignments);

        // Create a random password for the user and encode it
        String rawPassword = RandomStringUtils.randomAlphanumeric(MINIMUM_PASSWORD_LENGTH);
        String hashedPassword = passwordEncoder.encode(rawPassword);
        newUserEntity.setPassword(hashedPassword);

        // Save the new user in the database
        User savedUserEntity = userRepository.save(newUserEntity);

        // Send an email containing the login credentials to the new user
        emailService.sendEmail(
                savedUserEntity.getEmail(),
                "Your Survivor Fitness Account",
                "Hi!\n" +
                        "An account has been created for you in the Survivor Fitness App! Below are your login credentials.\n" +
                        "Please log into the app and change your password.\n\n" +
                        "Email/Username: " + savedUserEntity.getEmail() + "\n" +
                        "Password: " + rawPassword + "\n\n" +
                        "Thanks,\n" +
                        "Survivor Fitness Team");

        // Convert the user entity to the DTO and return it
        return getUserDtoBasedOnUserEntity(savedUserEntity);
    }


    /**
     * A helper method for createNewUser(). Creates a new User objects and populates it with the data
     * from @param newUserData and @param newUserLocationAssignments
     * @param newUserData UserDTO containing the data about the new User
     * @param newUserLocationAssignments contains the locations and the roles in those locations of the new user
     * @return User entity with all the fields assigned according to the parameters. The entity has not been saved yet.
     */
    private User getPopulatedUserEntityForCreatingUser(UserDTO newUserData,
                                                       List<LocationAssignmentDTO> newUserLocationAssignments) {
        // Create the new user entity and populate the fields that don't need verification
        User userEntity = new User()
                .setEmail(newUserData.getEmail())
                .setEnabled(true)
                .setFirstName(newUserData.getFirstName())
                .setLastName(newUserData.getLastName())
                .setPhoneNumber(newUserData.getPhoneNumber())
                .setSuperAdmin(newUserData.getIsSuperAdmin());

        // Get information about the current user (the one who is adding a new user)
        User currentUser = getCurrentlyLoggedInUser();

        // If a non-SuperAdmin user is trying to create a super admin user, block it
        if(newUserData.getIsSuperAdmin() && !currentUser.hasRole(UserRoleType.SUPER_ADMIN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The current user is not allowed to create Super Admins.");
        }

        // Get the verified location assignments of the new user and assign them to the new user object
        List<LocationAssignment> locationAssignments = verifyAndGetLocationAssignmentsOfNewUser(
                currentUser, userEntity, newUserLocationAssignments);
        userEntity.setLocationAssignments(locationAssignments);

        return userEntity;
    }


    /**
     * A helper method for createUser method. Verifies that the current user (the one currently
     * logged in) can add the new user to the locations/roles specified in @param newUserLocationAssignments.
     * If the current User is authorized to do that, the method will return a list of location
     * assignments for the new user. These location assignments will not have been saved into the database.
     * @param currentUser currently logged in user
     * @param newUser the new user entity the location assignments should be checked for
     * @param newUserLocationAssignments new user's location assignments that should be checked
     * @return a list of LocationAssignments for the new user
     */
    private List<LocationAssignment> verifyAndGetLocationAssignmentsOfNewUser(
            User currentUser, User newUser,
            List<LocationAssignmentDTO> newUserLocationAssignments) {

        // If a super admin is adding the user, they can add them to any location
        if(currentUser.hasRole(UserRoleType.SUPER_ADMIN)) {
            List<LocationAssignment> locationAssignments = new ArrayList<>();

            // Go over all locations the new user is assigned to and check if
            // they exist
            for(LocationAssignmentDTO locationAssignmentDTO: newUserLocationAssignments) {
                Location locationEntity = locationRepository.findLocationById(locationAssignmentDTO.getLocationId());
                if(Objects.isNull(locationEntity)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location " +
                            locationAssignmentDTO.getLocationId() + " the new user is assigned to does not exist.");
                }
                locationAssignments.add(new LocationAssignment(newUser, locationEntity,
                        locationAssignmentDTO.getUserRoleType()));
            }

            // All checks have been verified, return the location assignment entities
            return locationAssignments;
        }
        // If a location administrator is adding the user, they can add them only to the location they administer
        else if(currentUser.hasRole(UserRoleType.LOCATION_ADMINISTRATOR)) {
            // Location admins cannot add a user to multiple locations
            if(newUserLocationAssignments.size() != 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Location Administrator has to assign the new user to one location only.");
            }
            LocationAssignmentDTO singleLocationAssignment = newUserLocationAssignments.get(0);

            // Find the location that the new user is being assigned to and check if it exists
            Location locationNewUserIsAssignedTo = locationRepository.findLocationById(
                    singleLocationAssignment.getLocationId());
            if(Objects.isNull(locationNewUserIsAssignedTo)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "The location the new user is assigned to does not exist.");
            }

            // Check if the location administrator is adding the user to the
            // location they are administering
            if(!Objects.equals(locationNewUserIsAssignedTo.getAdministrator().getId(), currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "The user is not the administrator of the location");
            }

            // Check if the location administrator is adding only Dietitians or Trainers
            if(singleLocationAssignment.getUserRoleType() != UserRoleType.DIETITIAN &&
                    singleLocationAssignment.getUserRoleType() != UserRoleType.TRAINER) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Location Administrator can only add Trainers and Dietitians");
            }

            // All checks have been verified, return the location list
            return List.of(new LocationAssignment(newUser, locationNewUserIsAssignedTo,
                    singleLocationAssignment.getUserRoleType()));
        }

        // Only SUPER_ADMINS and LOCATION_ADMINS are allowed to add new users
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "The current user is not allowed to add new users.");
    }


    /**
     * Creates a UserDTO based on the User entity but only sets the id and the first/last name.
     * This method is used whenever only these fields are needed.
     * @param specialistEntity the User entities to retrieve values from
     * @return a new UserDTO with id, first name, and last name set
     */
    public static UserDTO getConciseUserDTOBasedOnUserEntity(User specialistEntity) {
        if(specialistEntity == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(specialistEntity.getId());
        userDTO.setFirstName(specialistEntity.getFirstName());
        userDTO.setLastName(specialistEntity.getLastName());

        return userDTO;
    }


    /**
     * Finds UserDTO by the specified @param userId
     * @param userId ID of the user to look for
     * @return UserDTO with the specified userId
     */
    public UserDTO getUserInfoById(Integer userId) {
        User userEntity = userRepository.findUserById(userId);
        UserDTO userDTO = getUserDtoBasedOnUserEntity(userEntity);
        return userDTO;
    }


    /**
     * Adds a Location Assignment to the user and saves the user in the database.
     * @param user the User the new Location Assignment is created for
     * @param location The Location the user is assigned to
     * @param userRoleType The role the user has at that location
     */
    public void addLocationAssignmentToUser(User user, Location location, UserRoleType userRoleType){
        LocationAssignment locationAssignment = new LocationAssignment(user, location, userRoleType);
        user.getLocationAssignments().add(locationAssignment);
        userRepository.save(user);
    }


    /**
     * Removes a Location Assignment from a user and deletes the Location Assignment from the database.
     * @param user the User whose Location Assignment has to be removed
     * @param location The Location of the Location Assignment that has to be removed
     * @param userRoleType The role at @param location that has to be removed
     */
    public void removeLocationAssignmentFromUser(User user, Location location, UserRoleType userRoleType){
        user.getLocationAssignments()
                .stream()
                .filter(
                    la -> la.getLocation().getId().equals(location.getId())
                            && la.getUser().getId().equals(user.getId())
                            && la.getUserRoleType().name().equals(userRoleType.name()))
                .forEach(la -> {
                    user.getLocationAssignments().remove(la);
                    locationAssignmentRepository.delete(la);
                });
    }


    /**
     * Verifies the old password of the user and changes the password to
     * the new one.
     * @param userId ID of the user that the password is changed for
     * @param oldPassword old password of the user
     * @param newPassword new password of the user
     */
    public void changePassword(int userId, String oldPassword, String newPassword) {
        User userChangingPassword = userRepository.findUserById(userId);
        if(Objects.isNull(userChangingPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found.");
        }

        // Get information about the current user (the one who is changing
        // the password of the user)
        User currentUser = getCurrentlyLoggedInUser();

        // Only the user themselves and the super admin can change the password of a user
        // Return an error if another user is trying to change the password
        if(!(currentUser.getId() == userId || currentUser.isSuperAdmin())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Only the user themselves or a super admin can change password of this user.");
        }

        // Check if the old password provided matches the one saved
        if(!passwordEncoder.matches(oldPassword, userChangingPassword.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "The old password does not match the one currently saved in the database!");
        }

        // Update password and save the user
        userChangingPassword.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userChangingPassword);

        // Send a confirmation email to the user
        emailService.sendEmail(userChangingPassword.getEmail(),
                "Your password has been changed!",
                "Hi!\n\n" +
                        "Your password has just been changed. If it wasn't you, " +
                        "please contact Survivor Fitness Foundation management " +
                        "so that they could reset your password.\n\n" +
                        "Thanks,\n" +
                        "Survivor Fitness Team");
    }


    /**
     * Updates the user in the database. Does NOT change id, password, or email.
     * @param userDtoToUpdate UserDTO of the User to be updated
     * @return UserDTO as it is now saved in the database
     */
    public UserDTO updateUser(UserDTO userDtoToUpdate, List<LocationAssignmentDTO> locationAssignmentDTOS) {
        // Find the User entity in the database. Throw an exception if the user
        // hasn't been found
        User userEntityToUpdate = userRepository.findUserById(userDtoToUpdate.getId());
        if(Objects.isNull(userEntityToUpdate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found.");
        }

        // Update primitive fields of the User
        userEntityToUpdate.setFirstName(userDtoToUpdate.getFirstName())
                .setLastName(userDtoToUpdate.getLastName())
                .setPhoneNumber(userDtoToUpdate.getPhoneNumber());

        // Go over all LocationAssignmentDTOs received from the request and
        // add all LocationAssignments that are missing
        for(LocationAssignmentDTO laDTO: locationAssignmentDTOS) {
            if(!userEntityToUpdate.hasLocationAssignment(laDTO.getLocationId(), laDTO.getUserRoleType())) {
                // Retrieve the Location object from the database and throw
                // an exception if it does not exist
                Location location = locationRepository.findLocationById(laDTO.getLocationId());
                if(Objects.isNull(location)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Location with id " + laDTO.getLocationId() + " does not exist!");
                }

                // Add the missing LocationAssignment to the User
                userEntityToUpdate.getLocationAssignments().add(
                        new LocationAssignment(userEntityToUpdate, location,
                                laDTO.getUserRoleType()));
            }
        }

        // If after adding all missing LocationAssignments the sizes of
        // locationAssignmentDTOS and userEntityToUpdate.getLocationAssignments()
        // aren't equal, it means that some LocationAssignments have been deleted.
        // Go over the LocationAssignments of the User and see which ones are missing
        // in the locationAssignmentDTOS list – delete those from the user and from
        // the database.
        if(locationAssignmentDTOS.size() != userEntityToUpdate.getLocationAssignments().size()) {
            List<LocationAssignment> lasToDelete = new ArrayList<>();

            // Go over all LocationAssignments and add the ones not present
            // in the locationAssignmentDTOS to lasToDelete
            for(LocationAssignment la: userEntityToUpdate.getLocationAssignments()) {
                if(!dtoListContainsLocationAssignmentDTO(locationAssignmentDTOS, la)) {
                    lasToDelete.add(la);
                }
            }

            // Delete all identified LocationAssignments from the User and
            // from the database
            userEntityToUpdate.getLocationAssignments().removeAll(lasToDelete);
            locationAssignmentRepository.deleteAll(lasToDelete);
        }

        // Save the updated User to the database
        User updatedUserEntity = userRepository.save(userEntityToUpdate);

        // Convert User object to the UserDTO and return it
        return getUserDtoBasedOnUserEntity(updatedUserEntity);
    }


    /**
     * Checks if the list of LocationAssignmentDTOs contains a LocationAssignmentDTO with the
     * same fields as @param locationAssignment. This method is used in the updateUser function
     * to check if the LocationAssignments of the User have changed.
     * @param laDTOs the list of LocationAssignmentDTOs to inspect
     * @param locationAssignment the locationAssignment to look for
     * @return true if a laDTOs contains a dto with the same fields as locationAssignment and false otherwise
     */
    private boolean dtoListContainsLocationAssignmentDTO(List<LocationAssignmentDTO> laDTOs,
                                                         LocationAssignment locationAssignment) {
        return laDTOs.stream()
                .anyMatch(dto -> dto.getLocationId() == locationAssignment.getLocation().getId() &&
                        dto.getUserRoleType().equals(locationAssignment.getUserRoleType()));
    }


    /**
     * Gets the currently logged-in user. The method can only be used by
     * authenticated users.
     * @return User object of the currently logged-in user
     */
    @PreAuthorize("isAuthenticated()")
    public User getCurrentlyLoggedInUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = (String) authentication.getPrincipal();

        return userRepository.findUserByEmail(currentUserEmail);
    }


    /**
     * Get all users that exist in the database;
     * @return A list of UserDTOs (all users)
     */
    public List<UserDTO> getAllUsers() {
        // Retrieve all user entities from the database
        return userRepository.findAll()

                // Convert the list into a stream of User objects
                .stream()

                // Convert each User object into a UserDTO object
                .map(this::getUserDtoBasedOnUserEntity)

                // Collect all UserDTOs into a list and return it
                .collect(Collectors.toList());
    }


    /**
     * Resets user's password to a new random one. Sends an email to user with the new password.
     * @param email Email of the user to reset password for
     */
    public void resetUserPassword(String email) {
        // Make the email all-lowercase b/c they are saved so in the database
        email = email.toLowerCase();

        // Find the User entity in the database. Throw an exception if the user
        // hasn't been found
        User userEntityToUpdate = userRepository.findUserByEmail(email);
        if(Objects.isNull(userEntityToUpdate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found.");
        }

        // Create a new random password for the user and encode it
        String rawPassword = RandomStringUtils.randomAlphanumeric(MINIMUM_PASSWORD_LENGTH);
        String hashedPassword = passwordEncoder.encode(rawPassword);
        userEntityToUpdate.setPassword(hashedPassword);

        // Save the user in the database
        User savedUserEntity = userRepository.save(userEntityToUpdate);

        // Send an email containing the new login credentials to the user
        emailService.sendEmail(
                savedUserEntity.getEmail(),
                "Your Survivor Fitness Account: Password has been reset",
                "Hi!\n\n" +
                        "Your password in the Survivor Fitness App has been reset! Below are your new login credentials. " +
                        "Please log into the app and change your password.\n\n" +
                        "Email/Username: " + email + "\n" +
                        "Password: " + rawPassword + "\n\n" +
                        "Thanks,\n" +
                        "Survivor Fitness Team");
    }
}
