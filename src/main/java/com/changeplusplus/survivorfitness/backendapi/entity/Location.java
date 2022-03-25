package com.changeplusplus.survivorfitness.backendapi.entity;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations")
@ToString
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

    public Location(LocationDTO locationDTO) {
        this.address = locationDTO.getAddress();
        this.name = locationDTO.getName();
        this.type = LocationType.valueOf(locationDTO.getType());
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

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", type=" + type +
                ", administrator=" + administrator +
                ", specialists=" + specialists +
                '}';
    }
}
