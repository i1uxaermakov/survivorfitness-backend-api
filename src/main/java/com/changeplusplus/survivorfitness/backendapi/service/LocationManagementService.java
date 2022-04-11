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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The class that encapsulates all functionality related to the management of Location
 * entities (retrieval, creation, etc.) @Service annotation indicates to the Spring
 * framework that it should create a singleton of this class and maintain its state.
 * Users of this class can get a reference to it by creating a field of this class in
 * another class and annotating it with @Autowired (see below for examples of
 * autowiring other services.
 */
@Service
public class LocationManagementService {

    /**
     * A reference to the class that encapsulates all functionality
     * related to the management of User entities.
     */
    @Autowired
    private UserManagementService userManagementService;

    /**
     * A reference to the interface that manages interaction with the database that pertains to Location entity
     * (all SQL operations with Locations table)
     */
    @Autowired
    private LocationRepository locationRepository;

    /**
     * A reference to the interface that manages interaction with the database that pertains to User entity
     * (all SQL operations with Users table)
     */
    @Autowired
    private UserRepository userRepository;


    /**
     * Creates a new Location entity and saves it in the database based on the information in locationDTO.
     * Also adds a new LocationAssignment with LOCATION_ADMINISTRATOR role to the administrator of the location.
     * @param locationDTO LocationDTO that contains information about the new location.
     * @return LocationDTO with the same fields as Location Entity that has been saved in the database.
     */
    public LocationDTO createNewLocation(LocationDTO locationDTO) {
        // Retrieve the user from the database and check if it exists
        User administrator = userRepository.findUserById(locationDTO.getAdministrator().getId());
        if(Objects.isNull(administrator)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Administrator with specified ID not found.");
        }

        // Create the location entity based on @param locationDTO and save
        // it in the database
        Location locationEntity = new Location(locationDTO);
        locationEntity.setAdministrator(administrator);
        locationEntity = locationRepository.save(locationEntity);

        // Add a new LocationAssignment with LOCATION_ADMINISTRATOR role at the new
        // location to the user
        userManagementService.addLocationAssignmentToUser(administrator,
                locationEntity, UserRoleType.LOCATION_ADMINISTRATOR);

        // Convert the Location entity to LocationDTO and return it
        return getLocationDtoFromLocationEntity(locationEntity);
    }


    /**
     * Retrieves general information (ID, name, type) about all locations. All LocationDTOs
     * that will be returned will only have ID, name, and type set. All others will be null.
     * This method is used whenever the list of all location has to be displayed on the
     * user's side and no info other than id, name, and type is needed.
     * @return A list of LocationDTOs that contains information about all Locations in the system
     */
    public List<LocationDTO> getGeneralInfoAboutAllLocations() {
        // Retrieve location entities from the database
        List<LocationOnlyIdNameTypeProjection> locationsRawInfo = locationRepository.findAllProjectedBy();

        // Convert all entities to LocationDTOs while setting only ID, name, and type
        List<LocationDTO> locationsPreparedInfo = new ArrayList<>();
        for(LocationOnlyIdNameTypeProjection projection: locationsRawInfo) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(projection.getId());
            locationDTO.setName(projection.getName());
            locationDTO.setType(projection.getType().toString());

            locationsPreparedInfo.add(locationDTO);
        }

        // Return the list of LocationDTOs with no info other than id, name, and type
        return locationsPreparedInfo;
    }


    /**
     * Retrieves information about a specific location and converts it to LocationDTO.
     * @param locationId ID of the location to retrieve
     * @return LocationDTO with the same fields as in Location entity. If a location
     * with @param locationId doesn't exist, returns null
     */
    public LocationDTO getInfoAboutParticularLocation(Integer locationId) {
        Location locationEntity = locationRepository.findLocationById(locationId);
        if(locationEntity == null) {
            return null;
        }
        return getLocationDtoFromLocationEntity(locationEntity);
    }


    /**
     * Updates the Location entity with the fields in the @param locationDTO.
     * @param locationDTO - object that contains updated info about the location
     * @return a DTO of the newly updated location
     */
    @Transactional
    public LocationDTO updateLocation(LocationDTO locationDTO){
        // Retrieve the location entity from the database and check if it exists
        Location locationEntity = locationRepository.findLocationById(locationDTO.getId());
        if(Objects.isNull(locationEntity)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location with the specified ID doesn't exist.");
        }

        // Update the address and the name of the location
        locationEntity.setAddress(locationDTO.getAddress());
        locationEntity.setName(locationDTO.getName());

        // Updating the administrator of the location. Whenever the location administrator changes, both the
        // previous administrator and new administrator accounts have to be updated. The previous admin has to be
        // unassigned from being a LOCATION_ADMINISTRATOR at that location (such LocationAssignment has to be removed.
        // The new admin has to be assigned to be a LOCATION_ADMINISTRATOR at that location (need to add a
        // LocationAssignment to them).

        // Retrieve the old and the new administrators and check that they exist
        User prevAdministrator = userRepository.findUserById(locationEntity.getAdministrator().getId());
        User newAdministrator = userRepository.findUserById(locationDTO.getAdministrator().getId());
        if(Objects.isNull(prevAdministrator) || Objects.isNull(newAdministrator)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Administrator with specified ID not found.");
        }

        // Remove location assignment from the previous administrator
        userManagementService.removeLocationAssignmentFromUser(prevAdministrator, locationEntity,
                UserRoleType.LOCATION_ADMINISTRATOR);

        // Add location assignment to the new administrator
        userManagementService.addLocationAssignmentToUser(newAdministrator, locationEntity,
                UserRoleType.LOCATION_ADMINISTRATOR);

        // Update the administrator of the location
        locationEntity.setAdministrator(newAdministrator);

        // Save the location administrator in the database
        Location savedLocationEntity = locationRepository.save(locationEntity);

        // Convert the saved Location entity to a DTO and return it
        return getLocationDtoFromLocationEntity(savedLocationEntity);
    }


    /**
     * Converts a Location entity to the LocationDTO. For the administrator of the location, the method
     * only retrieves ID, first name, and last name.
     * @param locationEntity The Location entity to convert
     * @return LocationDTO objects with the same fields as in Location entity
     */
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


    /**
     * Convert a Location entity to a LocationDTO while setting only ID, name, and type.
     * @param locationEntity the Location Entity to convert
     * @return a LocationDTO instance with only ID, name, and type set. If @param locationEntity
     * is null, returns null
     */
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
