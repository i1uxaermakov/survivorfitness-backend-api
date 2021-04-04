package com.changeplusplus.survivorfitness.backendapi.entity;

import javax.persistence.*;
import java.util.List;

@Entity
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

    @OneToOne
    private User administrator;

    @OneToMany(mappedBy = "location")
    private List<ParticipantAssignment> assignments;

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

    public List<ParticipantAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<ParticipantAssignment> assignments) {
        this.assignments = assignments;
    }
}
