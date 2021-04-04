package com.changeplusplus.survivorfitness.backendapi.entity.projection;

import com.changeplusplus.survivorfitness.backendapi.entity.LocationType;

public interface LocationOnlyIdNameTypeProjection {
    Integer getId();
    String getName();
    String getAddress();
    LocationType getType();
}
