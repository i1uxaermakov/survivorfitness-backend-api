package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.MeasurementDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.Measurement;
import com.changeplusplus.survivorfitness.backendapi.entity.Session;
import com.changeplusplus.survivorfitness.backendapi.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

/**
 * The class that encapsulates all functionality related to the management of Measurement
 * entities (retrieval, creation, updating, etc.) @Service annotation indicates to the Spring
 * framework that it should create a singleton of this class and maintain its state.
 * Users of this class can get a reference to it by creating a field of this class in
 * another class and annotating it with @Autowired (see below for examples of
 * autowiring other services).
 */
@Service
public class MeasurementManagementService {

    /**
     * A reference to the interface that manages interaction with the database that
     * pertains to Measurement entity (all SQL operations with Measurements table)
     */
    @Autowired
    private MeasurementRepository measurementRepository;


    /**
     * Update a specific Measurement in the database. Does not update the ID because it is immutable.
     * @param measurementDtoToUpdate measurementDTO with the new fields for the Measurement entity
     * @return MeasurementDTO with the fields set to the updated Measurement fields
     */
    public MeasurementDTO updateMeasurement(MeasurementDTO measurementDtoToUpdate) {
        // Retrieves the measurement from the database and check that it exists
        Measurement measurementEntityInDb = measurementRepository.findById(measurementDtoToUpdate.getId()).orElse(null);
        if(Objects.isNull(measurementEntityInDb)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Measurement with specified ID not found.");
        }

        // Updates the values
        measurementEntityInDb.setName(measurementDtoToUpdate.getName());
        measurementEntityInDb.setValue(measurementDtoToUpdate.getValue());
        measurementEntityInDb.setCategory(measurementDtoToUpdate.getCategory());
        measurementEntityInDb.setUnit(measurementDtoToUpdate.getUnit());

        // Saves in the entity in the database, converts it to DTO, and returns it
        return getMeasurementDtoFromMeasurementEntity(measurementRepository.save(measurementEntityInDb));
    }


    /**
     * Convert a Measurement Entity to a MeasurementDTO.
     * @param measurementEntity entity to convert
     * @return MeasurementDTO based on the entity
     */
    private MeasurementDTO getMeasurementDtoFromMeasurementEntity(Measurement measurementEntity) {
        return new MeasurementDTO(measurementEntity);
    }
}
