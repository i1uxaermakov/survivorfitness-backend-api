package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.Location;
import com.changeplusplus.survivorfitness.backendapi.entity.User;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRole;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRoleType;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
    private PasswordEncoder passwordEncoder;

    private static final int MINIMUM_PASSWORD_LENGTH = 8;

    public List<UserDTO> getGeneralInfoAboutAllSpecialistsOfSpecificType(UserRoleType type) {
        List<User> dietitiansList = userRepository.findUsersByRolesName(type);
        return getListOfUserDTOsWithUserInfoAndLocationInfo(dietitiansList);
    }

    public List<UserDTO> getGeneralInfoAboutSpecialistsOfSpecificTypeInSpecificLocation(UserRoleType type, Integer locationId) {
        List<User> userList = userRepository.findUsersByRolesNameAndLocationsAssignedToId(type, locationId);
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

        List<Location> locationEntities = userEntity.getLocationsAssignedTo();
        for(Location locationEntity: locationEntities) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(locationEntity.getId());
            locationDTO.setName(locationEntity.getName());

            userDTO.getLocations().add(locationDTO);
        }

        List<UserRole> roleEntities = userEntity.getRoles();
        for(UserRole roleEntity: roleEntities) {
            userDTO.getRoles().add(roleEntity.getName().toString());
        }

        return userDTO;
    }

    public UserDTO createNewUser(UserDTO newUserData) {
        // Check if the current user is allowed to add the new user and
        // get the user entity populated with the data about the new user
        User newUserEntity = getPopulatedUserEntityAndCheckIfAllowedToCreate(newUserData);

        // Create a random password for the user and encode it
        String rawPassword = RandomStringUtils.randomAlphanumeric(MINIMUM_PASSWORD_LENGTH);
        String hashedPassword = passwordEncoder.encode(rawPassword);
        newUserEntity.setPassword(hashedPassword);

        // Save the new user in the database
        User savedUserEntity = userRepository.save(newUserEntity);

        // TODO send an email with the password to the user

        // Convert the user entity to the DTO and return it
        // TODO REMOVE THE RAW PASSWORD
        return getUserDtoBasedOnUserEntity(savedUserEntity).setPassword(rawPassword);
    }


    /**
     * Does NOT set the password of the new user!
     * @param newUserData
     * @return
     */
    private User getPopulatedUserEntityAndCheckIfAllowedToCreate(UserDTO newUserData) {
        // Create the new user entity and populate the fields that don't need verification
        User userEntity = new User()
                .setEmail(newUserData.getEmail())
                .setEnabled(true)
                .setFirstName(newUserData.getFirstName())
                .setLastName(newUserData.getLastName())
                .setPhoneNumber(newUserData.getPhoneNumber());

        // Get information about the current user (the one who is adding a new user)
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = (String) authentication.getPrincipal();
        User currentUser = userRepository.findUserByEmail(currentUserEmail);

        // Check if the current user can assign the new user to the locations
        // specified and get a list of location entities. Set new user's
        // locations to the verified locations
        List<Location> locationsOfNewUser = getLocationEntitiesOfNewUserAndCheckIfAllowedToAssignToLocations(
                currentUser, newUserData.getLocations());
        userEntity.setLocationsAssignedTo(locationsOfNewUser);

        // Check if the current user can assign the specified roles to the new user
        // get a list of associated role entities. Set new user's
        // roles to the verified roles
        List<UserRole> rolesOfNewUser = getRoleEntitiesOfNewUserAndCheckIfAllowedToAssignRoles(
                currentUser, newUserData.getRoles());
        userEntity.setRoles(rolesOfNewUser);

        return userEntity;
    }


    private List<Location> getLocationEntitiesOfNewUserAndCheckIfAllowedToAssignToLocations(User currentUser, List<LocationDTO> locationDtosOfNewUser) {
        if(locationDtosOfNewUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user is not assigned to any locations.");
        }

        // If a super admin is adding the user, they can add them to any location
        if(currentUser.hasRole(UserRoleType.SUPER_ADMIN)) {
            List<Location> locationsAssignedTo = new ArrayList<>();

            // Go over all locations the new user is assigned to and check if
            // they exist
            for(LocationDTO locationDTO: locationDtosOfNewUser) {
                Location locationEntity = locationRepository.findLocationById(locationDTO.getId());
                if(Objects.isNull(locationEntity)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location " + locationDTO.getId() + " the new user is assigned to does not exist.");
                }
                locationsAssignedTo.add(locationEntity);
            }

            // All checks have been verified, return the location entities
            return locationsAssignedTo;
        }
        // If a location administrator is adding the user, they can add them only to the location they administer
        else if(currentUser.hasRole(UserRoleType.LOCATION_ADMINISTRATOR)) {
            // Location admins cannot add a user to multiple locations
            if(locationDtosOfNewUser.size() != 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location Administrator cannot assign the user to multiple locations.");
            }

            // Find the location that the new user is being assigned to and check if it exists
            Location locationNewUserIsAssignedTo = locationRepository.findLocationById(locationDtosOfNewUser.get(0).getId());
            if(Objects.isNull(locationNewUserIsAssignedTo)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The location the new user is assigned to does not exist.");
            }

            // Check if the location administrator is adding the user to the
            // location they are administering
            if(!Objects.equals(locationNewUserIsAssignedTo.getAdministrator().getId(), currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user is not the administrator of the location");
            }

            // All checks have been verified, return the location list
            return List.of(locationNewUserIsAssignedTo);
        }

        // Only SUPER_ADMINS and LOCATION_ADMINS are allowed to add new users
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The current user is not allowed to add new users.");
    }



    private List<UserRole> getRoleEntitiesOfNewUserAndCheckIfAllowedToAssignRoles(User currentUser, List<String> rolesOfNewUser) {
        if(rolesOfNewUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user is not assigned any role.");
        }

        // Super admins are allowed to create users with any roles
        if(currentUser.hasRole(UserRoleType.SUPER_ADMIN)) {
            List<UserRole> userRolesAssigned = new ArrayList<>();

            for(String roleName: rolesOfNewUser) {
                // Check if the role exists
                if(!EnumUtils.isValidEnum(UserRoleType.class, roleName)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The role " + roleName + " doesn't exist!");
                }

                // Retrieve the database entity from the database
                UserRole userRole = userRoleRepository.findUserRoleByName(UserRoleType.valueOf(roleName));

                // Add the entity to the list
                userRolesAssigned.add(userRole);
            }

            return userRolesAssigned;
        }
        // Location admins are allowed to create Dietitians and Trainers only
        else if(currentUser.hasRole(UserRoleType.LOCATION_ADMINISTRATOR)) {
            List<UserRole> userRolesAssigned = new ArrayList<>();

            for(String roleName: rolesOfNewUser) {
                if(!EnumUtils.isValidEnum(UserRoleType.class, roleName)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The role " + roleName + " doesn't exist!");
                }

                UserRoleType userRoleType = UserRoleType.valueOf(roleName);
                if(!(Objects.equals(userRoleType, UserRoleType.DIETITIAN) || Objects.equals(userRoleType, UserRoleType.TRAINER))) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The current user is not allowed to create a user with a role " + roleName);
                }

                // Retrieve the databse entity from the database
                UserRole userRole = userRoleRepository.findUserRoleByName(UserRoleType.valueOf(roleName));

                // Add the entity to the list
                userRolesAssigned.add(userRole);
            }

            return userRolesAssigned;
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

    public void addRoleToUser(User user, UserRoleType userRoleType) {
        if(!user.hasRole(userRoleType)) {
            UserRole role = userRoleRepository.findUserRoleByName(userRoleType);
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }
}
