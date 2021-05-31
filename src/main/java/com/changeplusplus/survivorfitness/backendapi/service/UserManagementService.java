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
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getGeneralInfoABoutAllDietitians() {
        UserRole userRoleEntity = userRoleRepository.findUserRoleByName(UserRoleType.DIETITIAN);
        return getListOfUserDTOsWithUserInfoAndLocationInfo(userRoleEntity.getUsersWithRole());
    }

    public List<UserDTO> getGeneralInfoABoutAllTrainers() {
        UserRole userRoleEntity = userRoleRepository.findUserRoleByName(UserRoleType.TRAINER);
        return getListOfUserDTOsWithUserInfoAndLocationInfo(userRoleEntity.getUsersWithRole());
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
}
