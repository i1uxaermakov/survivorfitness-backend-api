package com.changeplusplus.survivorfitness.backendapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrEditUserRequest {
    private UserDTO user;
    private List<LocationAssignmentDTO> locationAssignments;
}
