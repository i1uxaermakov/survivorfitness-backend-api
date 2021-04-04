package com.changeplusplus.survivorfitness.backendapi.controller.payload;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;

import java.util.List;

public class InfoAboutAllLocationsResponse {
    private List<LocationDTO> locations;

    public InfoAboutAllLocationsResponse(List<LocationDTO> locations) {
        super();
        this.locations = locations;
    }

    public InfoAboutAllLocationsResponse() {
        super();
    }

    public List<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDTO> locations) {
        this.locations = locations;
    }
}
