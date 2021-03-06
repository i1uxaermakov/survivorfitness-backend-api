package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.Location;
import com.changeplusplus.survivorfitness.backendapi.entity.User;
import com.changeplusplus.survivorfitness.backendapi.entity.projection.LocationOnlyIdNameTypeProjection;
import com.changeplusplus.survivorfitness.backendapi.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationManagementService {

    @Autowired
    public LocationRepository locationRepository;

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
