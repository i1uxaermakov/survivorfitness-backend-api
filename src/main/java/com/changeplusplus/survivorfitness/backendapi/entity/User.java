package com.changeplusplus.survivorfitness.backendapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private boolean enabled;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "is_super_admin")
    private boolean isSuperAdmin = false;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(
            name="user_roles",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id"))
    private List<UserRole> roles = new ArrayList<>();


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name="user_locations",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="location_id"))
    private List<Location> locationsAssignedTo = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy = "user")
    private List<LocationAssignment> locationAssignments = new ArrayList<>();

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
}
