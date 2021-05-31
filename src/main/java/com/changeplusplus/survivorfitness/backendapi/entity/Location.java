package com.changeplusplus.survivorfitness.backendapi.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lid")
    private Integer id;
    private String name;
    private String address;

    @Column(name = "location_type")
    @Enumerated(EnumType.STRING)
    private LocationType type;

    @ManyToOne(optional = false)
    @JoinColumn(name="admin_user_id")
    private User administrator;


    @ManyToMany(mappedBy = "locationsAssignedTo", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<User> specialists = new ArrayList<>();

    public Location() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public User getAdministrator() {
        return administrator;
    }

    public void setAdministrator(User administrator) {
        this.administrator = administrator;
    }

    public List<User> getSpecialists() {
        return specialists;
    }

    public void setSpecialists(List<User> specialists) {
        this.specialists = specialists;
    }
}
