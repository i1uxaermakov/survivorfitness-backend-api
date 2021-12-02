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

@Service
public class MeasurementManagementService {

    @Autowired
    private MeasurementRepository measurementRepository;

    public MeasurementDTO updateMeasurement(MeasurementDTO measurementDtoToUpdate) {
        Measurement measurementEntityInDb = measurementRepository.findById(measurementDtoToUpdate.getId()).orElse(null);
        if(Objects.isNull(measurementEntityInDb)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Measurement with specified ID not found.");
        }

        measurementEntityInDb.setName(measurementDtoToUpdate.getName());
        measurementEntityInDb.setValue(measurementDtoToUpdate.getValue());
        measurementEntityInDb.setCategory(measurementDtoToUpdate.getCategory());
        measurementEntityInDb.setUnit(measurementDtoToUpdate.getUnit());

        return getMeasurementDtoFromMeasurementEntity(measurementRepository.save(measurementEntityInDb));
    }

    private MeasurementDTO getMeasurementDtoFromMeasurementEntity(Measurement measurementEntity) {
        return new MeasurementDTO(measurementEntity);
    }
}
