package com.changeplusplus.survivorfitness.backendapi.entity;

import javax.persistence.*;

@Entity
@Table(name = "participant_assignment")
public class ParticipantAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pa_id")
    private Integer id;

    @Column(name = "specialist_type")
    @Enumerated(EnumType.STRING)
    private SpecialistType specialistType;

    @ManyToOne(fetch = FetchType.EAGER)
    private User specialist;

    @ManyToOne(fetch = FetchType.LAZY)
    private Participant participant;

    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;

    public ParticipantAssignment() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getSpecialist() {
        return specialist;
    }

    public void setSpecialist(User specialist) {
        this.specialist = specialist;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public SpecialistType getSpecialistType() {
        return specialistType;
    }

    public void setSpecialistType(SpecialistType specialistType) {
        this.specialistType = specialistType;
    }
}
