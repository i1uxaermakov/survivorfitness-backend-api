package com.changeplusplus.survivorfitness.backendapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(exclude = {"password"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isSuperAdmin;

    @JsonIgnore
    private String password;
    private String phoneNumber;
    private List<LocationDTO> locations;
    private List<String> roles;

    private List<LocationAssignmentDTO> locationAssignments;
}
