package com.changeplusplus.survivorfitness.backendapi.controller;

import com.changeplusplus.survivorfitness.backendapi.export.OnDataExportRequestedEvent;
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

@RestController
@RequestMapping("/api/v1/export-data")
public class DataExportController {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping()
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<String> exportData() {
        // Get the email of the current user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = (String) authentication.getPrincipal();

        // Publish an event to trigger the export of the data in the background
        eventPublisher.publishEvent(new OnDataExportRequestedEvent(this, List.of(currentUserEmail)));

        // Let the user know that the data export process has started
        return ResponseEntity.ok("Your request for exporting the data has been received. We will send you an email when the data export is complete.");
    }
}
