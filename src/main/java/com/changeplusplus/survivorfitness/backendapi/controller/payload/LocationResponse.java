package com.changeplusplus.survivorfitness.backendapi.controller.payload;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;

public class LocationResponse {
    private LocationDTO location;

    public LocationResponse(LocationDTO location) {
        this.location = location;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }
}
