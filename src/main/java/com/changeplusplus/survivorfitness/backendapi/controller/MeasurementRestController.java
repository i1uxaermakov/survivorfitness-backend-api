package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationListResponse;
import com.changeplusplus.survivorfitness.backendapi.dto.MeasurementDTO;
import com.changeplusplus.survivorfitness.backendapi.service.MeasurementManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

/**
 * MeasurementRestController - controller to edit information about a measurement only
 */
@RestController
@RequestMapping("/api/v1/measurements")
@Api(tags = "Measurement Controller", description = "Endpoint to edit information about a management.")
public class MeasurementRestController {

    @Autowired
    private MeasurementManagementService measurementManagementService;

    /**
     * updateMeasurement - endpoint to update info about a measurement
     * @param measurementId - the id of the measurement to update
     * @param measurementDtoToUpdate - the updated information about the measurement
     * @return the updated measurement object retrieved from the DB
     */
    @PutMapping("/{measurementId}")
    @ApiOperation(value = "Edits a measurement object",
            notes="This endpoint is available to all users.",
            response = MeasurementDTO.class)
    public MeasurementDTO updateMeasurement(@PathVariable Integer measurementId, @RequestBody MeasurementDTO measurementDtoToUpdate) {
        if(!Objects.equals(measurementId, measurementDtoToUpdate.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id in URL and id of the measurement are different.");
        }

        return measurementManagementService.updateMeasurement(measurementDtoToUpdate);
    }
}
