package com.changeplusplus.survivorfitness.backendapi.entity;

import org.hibernate.annotations.Where;

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

    @OneToMany(mappedBy="program", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)//todo mapping
    @Where(clause = "whoseNotes='TRAINER'")
    private List<Session> trainerSessions = new ArrayList<>();

    @OneToMany(mappedBy="program", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)//todo mapping
    @Where(clause = "whoseNotes='DIETITIAN'")
    private List<Session> dietitianSessions = new ArrayList<>();

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

    public List<Session> getTrainerSessions() {
        return trainerSessions;
    }

    public void setTrainerSessions(List<Session> trainerSessions) {
        this.trainerSessions = trainerSessions;
    }

    public List<Session> getDietitianSessions() {
        return dietitianSessions;
    }

    public void setDietitianSessions(List<Session> dietitianSessions) {
        this.dietitianSessions = dietitianSessions;
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
