package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.Location;
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

        // also need to add the new role to the administrator (LOCATION_ADMINISTRATOR)
        userManagementService.addRoleToUser(administrator, UserRoleType.LOCATION_ADMINISTRATOR);

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

    public LocationDTO updateLocation(LocationDTO locationDTO){
        Location locationEntity = locationRepository.findLocationById(locationDTO.getId());

        if(Objects.isNull(locationEntity)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }

        locationEntity.setAddress(locationDTO.getAddress());
        locationEntity.setName(locationDTO.getName());

        User administrator = userRepository.findUserById(locationDTO.getAdministrator().getId());

        if(Objects.isNull(administrator)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Administrator with specified ID not found.");
        }

        locationEntity.setAdministrator(administrator);


        //should I check if it is a location admin first? or does it not matter
        userManagementService.addRoleToUser(administrator, UserRoleType.LOCATION_ADMINISTRATOR);


        return getLocationDtoFromLocationEntity(locationEntity);


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
