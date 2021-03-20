package com.changeplusplus.survivorfitness.backendapi.entity;

import javax.persistence.*;

@Entity
@Table(name = "participant_assignment")
public class ParticipantAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pa_id")
    private Integer id;

    @ManyToOne
    private User specialist;

    @ManyToOne
    private Participant participant;

    @ManyToOne
    private Location location;
}
