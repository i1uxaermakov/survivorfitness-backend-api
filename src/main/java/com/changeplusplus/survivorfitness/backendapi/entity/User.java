package com.changeplusplus.survivorfitness.backendapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "uid")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String password;

    @Column(columnDefinition = "BOOLEAN")
    private boolean enabled;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_super_admin", columnDefinition = "BOOLEAN")
    private boolean isSuperAdmin = false;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "user")
    private List<LocationAssignment> locationAssignments = new ArrayList<>();


    /**
     * Checks if the user has the specified role.
     * @param requestedRoleType the role to look for
     * @return true if the user has the role and false otherwise
     */
    public boolean hasRole(UserRoleType requestedRoleType) {
        for(LocationAssignment la: locationAssignments) {
            if(la.getUserRoleType().equals(requestedRoleType)) {
                return true;
            }
        }
        return isSuperAdmin && requestedRoleType == UserRoleType.SUPER_ADMIN;
    }


    /**
     * Returns full name of the user
     * @return full name of the user
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }


    /**
     * Checks if the user has a location assignment with @param locationId
     * and @param userRoleType
     * @param locationId ID of the location to search for
     * @param userRoleType role to search for
     * @return true if the user has the specified location assignment and
     * false otherwise
     */
    public boolean hasLocationAssignment(Integer locationId, UserRoleType userRoleType) {
        for(LocationAssignment la: locationAssignments) {
            if(la.getUserRoleType().equals(userRoleType) && la.getLocation().getId().equals(locationId)) {
                return true;
            }
        }
        return false;
    }
}
