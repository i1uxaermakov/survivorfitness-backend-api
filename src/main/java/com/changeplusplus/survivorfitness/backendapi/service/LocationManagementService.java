package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.Location;
import com.changeplusplus.survivorfitness.backendapi.entity.LocationType;
import com.changeplusplus.survivorfitness.backendapi.entity.User;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRoleType;
import com.changeplusplus.survivorfitness.backendapi.entity.projection.LocationOnlyIdNameTypeProjection;
import com.changeplusplus.survivorfitness.backendapi.repository.LocationRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LocationManagementService {

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    public LocationDTO createNewLocation(LocationDTO locationDTO) {
        User administrator = userRepository.findUserById(locationDTO.getAdministrator().getId());
        if(Objects.isNull(administrator)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Administrator with specified ID not found.");
        }

        Location locationEntity = new Location(locationDTO);
        locationEntity.setAdministrator(administrator);
        locationEntity = locationRepository.save(locationEntity);


        userManagementService.addLocationAssignmentToUser(administrator,
                locationEntity, UserRoleType.LOCATION_ADMINISTRATOR);

        return getLocationDtoFromLocationEntity(locationEntity);
    }


    public List<LocationDTO> getGeneralInfoAboutAllLocations() {
        List<LocationOnlyIdNameTypeProjection> locationsRawInfo = locationRepository.findAllProjectedBy();

        List<LocationDTO> locationsPreparedInfo = new ArrayList<>();
        for(LocationOnlyIdNameTypeProjection projection: locationsRawInfo) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(projection.getId());
            locationDTO.setName(projection.getName());
            locationDTO.setType(projection.getType().toString());

            locationsPreparedInfo.add(locationDTO);
        }

        return locationsPreparedInfo;
    }


    public LocationDTO getInfoAboutParticularLocation(Integer locationId) {
        Location locationEntity = locationRepository.findLocationById(locationId);
        if(locationEntity == null) {
            return null;
        }
        return getLocationDtoFromLocationEntity(locationEntity);
    }

    //function to implement details of updating the location
    public LocationDTO updateLocation(LocationDTO locationDTO){

        //gets relevant location object from database + old administrator
        Location locationEntity = locationRepository.findLocationById(locationDTO.getId());
        if(Objects.isNull(locationEntity)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }

        //updates address + name
        locationEntity.setAddress(locationDTO.getAddress());
        locationEntity.setName(locationDTO.getName());

        //logic to update the administrator
        User prevAdministrator = userRepository.findUserById(locationEntity.getAdministrator().getId());
        User newAdministrator = userRepository.findUserById(locationDTO.getAdministrator().getId());
        if(Objects.isNull(newAdministrator)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Administrator with specified ID not found.");
        }

        locationEntity.setAdministrator(newAdministrator);
        //info is now all updated about the location

        //add location assignment to new administrator
        userManagementService.addLocationAssignmentToUser(newAdministrator, locationEntity, UserRoleType.LOCATION_ADMINISTRATOR);

        //remove location assignment from previous administrator
        userManagementService.removeLocationAssignmentFromUser(prevAdministrator, locationEntity, UserRoleType.LOCATION_ADMINISTRATOR);

        Location savedLocationEntity = locationRepository.save(locationEntity);
        return getLocationDtoFromLocationEntity(savedLocationEntity);
    }

    private LocationDTO getLocationDtoFromLocationEntity(Location locationEntity) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(locationEntity.getId());
        locationDTO.setName(locationEntity.getName());
        locationDTO.setAddress(locationEntity.getAddress());
        locationDTO.setType(locationEntity.getType().toString());

        User userEntity = locationEntity.getAdministrator();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        locationDTO.setAdministrator(userDTO);
        return locationDTO;
    }


    public static LocationDTO getConciseLocationDTOBasedOnLocationEntity(Location locationEntity) {
        if(locationEntity == null) {
            return null;
        }

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(locationEntity.getId());
        locationDTO.setName(locationEntity.getName());
        locationDTO.setType(locationEntity.getType().toString());

        return locationDTO;
    }


}
