package com.changeplusplus.survivorfitness.backendapi.dto;

import com.changeplusplus.survivorfitness.backendapi.entity.LocationAssignment;
import com.changeplusplus.survivorfitness.backendapi.entity.UserRoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationAssignmentDTO {
    private int locationId;
    private UserRoleType userRoleType;

    public LocationAssignmentDTO(LocationAssignment locationAssignmentEntity) {
        this.locationId = locationAssignmentEntity.getLocation().getId();
        this.userRoleType = locationAssignmentEntity.getUserRoleType();
    }
}
