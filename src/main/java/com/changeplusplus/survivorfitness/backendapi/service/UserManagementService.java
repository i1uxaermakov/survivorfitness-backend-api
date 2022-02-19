package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.Location;
import com.changeplusplus.survivorfitness.backendapi.entity.User;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRole;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRoleType;
import com.changeplusplus.survivorfitness.backendapi.repository.UserRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserManagementService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

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

    public void removeRoleFromUser(User user, UserRoleType userRoleType){
        if (user.hasRole(userRoleType)){
            UserRole role = userRoleRepository.findUserRoleByName(userRoleType);
            user.getRoles().removeIf(r -> r.equals(role));
            userRepository.save(user);
        }
    }


    public void addRoleToUser(User user, UserRoleType userRoleType) {
        if(!user.hasRole(userRoleType)) {
            UserRole role = userRoleRepository.findUserRoleByName(userRoleType);
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

    public void addLocationToUser(User user, Location location){
        user.addLocationIfAbsent(location);
        userRepository.save(user);
    }

    public void removeLocationFromUser(User user, Location location){
        System.out.println("removeLocationFromUser");
        user.removeLocationIfPresent(location);
        userRepository.save(user);
    }
}
