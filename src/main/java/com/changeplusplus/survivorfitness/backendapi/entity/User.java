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

    public boolean hasLocation(Location location){
        for (Location l : locationsAssignedTo) {
            if (l.getId().equals(location.getId())){
                return true;
            }
        }
        return false;
    }

    public void addLocationIfAbsent(Location location){
        if (!hasLocation(location)){
            locationsAssignedTo.add(location);
        }
    }
    public void removeLocationIfPresent(Location location){
        System.out.println("here");
        locationsAssignedTo.removeIf(l -> l.getId().equals(location.getId()));
        for (Location l : locationsAssignedTo) {
            System.out.println(l);
        }
    }

    public boolean hasRole(UserRoleType requestedRoleType) {
        for(UserRole role: roles) {
            if(role.getName().equals(requestedRoleType)) {
                return true;
            }
        }
        return false;
    }
}
