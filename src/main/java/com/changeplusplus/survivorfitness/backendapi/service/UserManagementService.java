package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationAssignmentDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.LocationAssignmentRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.LocationRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.UserRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.UserRoleRepository;
import com.changeplusplus.survivorfitness.backendapi.security.UsernamePwdUserInfoAuthenticationToken;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    private UserRoleRepository userRoleRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationAssignmentRepository locationAssignmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private static final int MINIMUM_PASSWORD_LENGTH = 8;

    public List<UserDTO> getGeneralInfoAboutAllSpecialistsOfSpecificType(UserRoleType type) {
        List<User> dietitiansList = userRepository.findUsersByLocationAssignmentsUserRoleType(type);
        return getListOfUserDTOsWithUserInfoAndLocationInfo(dietitiansList);
    }

    public List<UserDTO> getGeneralInfoAboutSpecialistsOfSpecificTypeInSpecificLocation(UserRoleType type, Integer locationId) {
        List<User> userList = userRepository.findUsersByLocationAssignmentsUserRoleTypeAndLocationAssignmentsLocationId(type, locationId);
        return getListOfUserDTOsWithUserInfoAndLocationInfo(userList);
    }

    public UserDTO getUserInfoByEmail(String email) {
        User userEntity = userRepository.findUserByEmail(email);
        UserDTO userDTO = getUserDtoBasedOnUserEntity(userEntity);
        return userDTO;
    }

    private List<UserDTO> getListOfUserDTOsWithUserInfoAndLocationInfo(List<User> usersWithRole) {
        List<UserDTO> userDTOList = new ArrayList<>();

        for(User userEntity: usersWithRole) {
            userDTOList.add(getUserDtoBasedOnUserEntity(userEntity));
        }

        return userDTOList;
    }


    private UserDTO getUserDtoBasedOnUserEntity(User userEntity) {
        if(userEntity == null) {
            return null;
        }

        UserDTO userDTO = getConciseUserDTOBasedOnUserEntity(userEntity);
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setSuperAdmin(userEntity.isSuperAdmin());

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
            LocationDTO locationDTO = new LocationDTO()
                    .setId(la.getLocation().getId())
                    .setName(la.getLocation().getName())
                    .setType(la.getLocation().getType().name());
            locationDTOs.add(locationDTO);
        }
        userDTO.setLocations(new ArrayList<>(locationDTOs));

        return userDTO;
    }


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



    private User getPopulatedUserEntityForCreatingUser(UserDTO newUserData, List<LocationAssignmentDTO> newUserLocationAssignments) {
        // Create the new user entity and populate the fields that don't need verification
        User userEntity = new User()
                .setEmail(newUserData.getEmail())
                .setEnabled(true)
                .setFirstName(newUserData.getFirstName())
                .setLastName(newUserData.getLastName())
                .setPhoneNumber(newUserData.getPhoneNumber())
                .setSuperAdmin(newUserData.isSuperAdmin());

        // Get information about the current user (the one who is adding a new user)
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = (String) authentication.getPrincipal();
        User currentUser = userRepository.findUserByEmail(currentUserEmail);

        // If a non-SuperAdmin user is trying to create a super admin user, block it
        if(newUserData.isSuperAdmin() && !currentUser.hasRole(UserRoleType.SUPER_ADMIN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The current user is not allowed to create Super Admins.");
        }

        List<LocationAssignment> locationAssignments = verifyAndGetLocationAssignmentsOfNewUser(currentUser, userEntity, newUserLocationAssignments);
        userEntity.setLocationAssignments(locationAssignments);

        return userEntity;
    }


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
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location " + locationAssignmentDTO.getLocationId() + " the new user is assigned to does not exist.");
                }
                locationAssignments.add(new LocationAssignment(newUser, locationEntity, locationAssignmentDTO.getUserRoleType()));
            }

            // All checks have been verified, return the location assignment entities
            return locationAssignments;
        }
        // If a location administrator is adding the user, they can add them only to the location they administer
        else if(currentUser.hasRole(UserRoleType.LOCATION_ADMINISTRATOR)) {
            // Location admins cannot add a user to multiple locations
            if(newUserLocationAssignments.size() != 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location Administrator has to assign the new user to one location only.");
            }
            LocationAssignmentDTO singleLocationAssignment = newUserLocationAssignments.get(0);

            // Find the location that the new user is being assigned to and check if it exists
            Location locationNewUserIsAssignedTo = locationRepository.findLocationById(singleLocationAssignment.getLocationId());
            if(Objects.isNull(locationNewUserIsAssignedTo)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location the new user is assigned to does not exist.");
            }

            // Check if the location administrator is adding the user to the
            // location they are administering
            if(!Objects.equals(locationNewUserIsAssignedTo.getAdministrator().getId(), currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user is not the administrator of the location");
            }

            // Check if the location administrator is adding only Dietitians or Trainers
            if(singleLocationAssignment.getUserRoleType() != UserRoleType.DIETITIAN &&
                    singleLocationAssignment.getUserRoleType() != UserRoleType.TRAINER) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location Administrator can only add Trainers and Dietitians");
            }

            // All checks have been verified, return the location list
            return List.of(new LocationAssignment(newUser, locationNewUserIsAssignedTo, singleLocationAssignment.getUserRoleType()));
        }

        // Only SUPER_ADMINS and LOCATION_ADMINS are allowed to add new users
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The current user is not allowed to add new users.");
    }


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

    public UserDTO getUserInfoById(Integer userId) {
        User userEntity = userRepository.findUserById(userId);
        UserDTO userDTO = getUserDtoBasedOnUserEntity(userEntity);
        return userDTO;
    }

    public void addLocationAssignmentToUser(User user, Location location, UserRoleType userRoleType){
        LocationAssignment locationAssignment = new LocationAssignment(user, location, userRoleType);
        user.getLocationAssignments().add(locationAssignment);
        userRepository.save(user);
    }

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

        // Get information about the current user (the one who is adding a new user)
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = (String) authentication.getPrincipal();
        User currentUser = userRepository.findUserByEmail(currentUserEmail);

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
     * @param userDtoToUpdate
     * @return
     */
    public UserDTO updateUser(UserDTO userDtoToUpdate) {
        User userEntityToUpdate = userRepository.findUserById(userDtoToUpdate.getId());
        if(Objects.isNull(userEntityToUpdate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found.");
        }

        userEntityToUpdate.setFirstName(userDtoToUpdate.getFirstName())
                .setLastName(userDtoToUpdate.getLastName())
                .setPhoneNumber(userDtoToUpdate.getPhoneNumber());

        if(!Objects.equals(userDtoToUpdate.isSuperAdmin(), userEntityToUpdate.isSuperAdmin())
            // add check if the user is admin
        ) {

        }

        return null;
    }


    /**
     *
     * @return
     */
    public User getCurrentlyLoggedInUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = (String) authentication.getPrincipal();
        User currentUser = userRepository.findUserByEmail(currentUserEmail);

        return currentUser;
    }
}
