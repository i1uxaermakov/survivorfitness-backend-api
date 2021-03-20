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
    private String type;

    @OneToOne
    private User administrator;

    @OneToMany(mappedBy = "location")
    private List<ParticipantAssignment> assignments;


}
