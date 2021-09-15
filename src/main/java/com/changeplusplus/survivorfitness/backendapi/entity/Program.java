package com.changeplusplus.survivorfitness.backendapi.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "program_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ProgramProgressStatus programProgressStatus;

    @OneToMany(fetch = FetchType.LAZY)//todo mapping
    private List<Session> sessions = new ArrayList<>();

    @OneToOne(mappedBy = "treatmentProgram", fetch = FetchType.LAZY)
    private Participant participant;

    @ManyToOne(fetch = FetchType.EAGER)//todo mapping
    @JoinColumn(name="trainer_user_id")
    private User trainer;

    @ManyToOne(fetch = FetchType.EAGER)//todo mapping
    @JoinColumn(name="trainer_location_id")
    private Location trainerGym;

    @ManyToOne(fetch = FetchType.EAGER)//todo mapping
    @JoinColumn(name="dietitian_user_id")
    private User dietitian;

    @ManyToOne(fetch = FetchType.EAGER)//todo mapping
    @JoinColumn(name="dietitian_location_id")
    private Location dietitianOffice;

    public Program() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProgramProgressStatus getProgramProgressStatus() {
        return programProgressStatus;
    }

    public void setProgramProgressStatus(ProgramProgressStatus programProgressStatus) {
        this.programProgressStatus = programProgressStatus;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public User getTrainer() {
        return trainer;
    }

    public void setTrainer(User trainer) {
        this.trainer = trainer;
    }

    public Location getTrainerGym() {
        return trainerGym;
    }

    public void setTrainerGym(Location trainerGym) {
        this.trainerGym = trainerGym;
    }

    public User getDietitian() {
        return dietitian;
    }

    public void setDietitian(User dietitian) {
        this.dietitian = dietitian;
    }

    public Location getDietitianOffice() {
        return dietitianOffice;
    }

    public void setDietitianOffice(Location dietitianOffice) {
        this.dietitianOffice = dietitianOffice;
    }
}
