package com.changeplusplus.survivorfitness.backendapi.controller.payload;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;

public class InfoAboutParticularLocationResponse {
    private LocationDTO location;

    public InfoAboutParticularLocationResponse(LocationDTO location) {
        this.location = location;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }
}
