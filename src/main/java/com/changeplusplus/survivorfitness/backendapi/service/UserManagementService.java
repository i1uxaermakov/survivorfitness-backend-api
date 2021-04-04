package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.Location;
import com.changeplusplus.survivorfitness.backendapi.entity.User;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRole;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRoleName;
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
        UserRole userRoleEntity = userRoleRepository.findUserRoleByName(UserRoleName.DIETITIAN);

        return new ArrayList<>();
    }


    private List<UserDTO> getListOfUserDTOsWithUserInfoAndLocationInfo(List<User> userEntityList) {
        List<UserDTO> userDTOs = new ArrayList<>();

        for(User userEntity: userEntityList) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userEntity.getId());
            userDTO.setFirstName(userEntity.getFirstName());
            userDTO.setLastName(userEntity.getLastName());


        }
        return userDTOs;
    }
}
