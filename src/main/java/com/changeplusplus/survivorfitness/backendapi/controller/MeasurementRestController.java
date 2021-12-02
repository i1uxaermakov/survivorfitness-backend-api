package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.MeasurementDTO;
import com.changeplusplus.survivorfitness.backendapi.service.MeasurementManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/measurements")
public class MeasurementRestController {

    @Autowired
    private MeasurementManagementService measurementManagementService;

    @PutMapping("/{measurementId}")
    public MeasurementDTO updateMeasurement(@PathVariable Integer measurementId, @RequestBody MeasurementDTO measurementDtoToUpdate) {
        if(!Objects.equals(measurementId, measurementDtoToUpdate.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id in URL and id of the measurement are different.");
        }

        return measurementManagementService.updateMeasurement(measurementDtoToUpdate);
    }
}
