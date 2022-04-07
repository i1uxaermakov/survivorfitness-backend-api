package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationListResponse;
import com.changeplusplus.survivorfitness.backendapi.export.OnDataExportRequestedEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * DataExportController - controller class used to export participant data
 */
@RestController
@RequestMapping("/api/v1/export-data")
@Api(tags = "Data Export Controller",
        description = "Provides the endpoint to export data.")
public class DataExportController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    /**
     * Export Data endpoint - creates a request to export all the available data about the participants
     * @return if successful, a message indicating that the request was received.
     */
    @GetMapping("")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    @ApiOperation(value = "Creates a request to export all existing data about participants/",
            notes="The endpoint is only available to users with the role SUPER_ADMIN.",
            response = ResponseEntity.class)
    public ResponseEntity<String> exportData() {
        // Get the email of the current user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = (String) authentication.getPrincipal();

        // Publish an event to trigger the export of the data in the background
        eventPublisher.publishEvent(new OnDataExportRequestedEvent(this, List.of(currentUserEmail)));

        // Let the user know that the data export process has started
        return ResponseEntity.ok("Your request for exporting the data has been received. " +
                "We will send you an email when the data export is complete.");
    }
}
