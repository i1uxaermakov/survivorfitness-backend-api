package com.changeplusplus.survivorfitness.backendapi.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "session_id")
    private Integer id;
    private Integer sessionIndexNumber;
    private Date initialLogDate;
    private Date lastUpdatedDate;

    @Enumerated(EnumType.STRING)
    private SpecialistType whoseNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="program_id", nullable=false)
    private Program program;

    @ManyToOne(fetch = FetchType.LAZY)
    private Participant participant;

    @OneToMany(mappedBy="session", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Measurement> measurements;

    private String specialistNotes;
    private String adminNotes;

    public Session() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getInitialLogDate() {
        return initialLogDate;
    }

    public void setInitialLogDate(Date initialLogDate) {
        this.initialLogDate = initialLogDate;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public String getSpecialistNotes() {
        return specialistNotes;
    }

    public void setSpecialistNotes(String specialistNotes) {
        this.specialistNotes = specialistNotes;
    }

    public String getAdminNotes() {
        return adminNotes;
    }

    public void setAdminNotes(String adminNotes) {
        this.adminNotes = adminNotes;
    }

    public Integer getSessionIndexNumber() {
        return sessionIndexNumber;
    }

    public void setSessionIndexNumber(Integer sessionIndexNumber) {
        this.sessionIndexNumber = sessionIndexNumber;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public SpecialistType getWhoseNotes() {
        return whoseNotes;
    }

    public void setWhoseNotes(SpecialistType whoseNotes) {
        this.whoseNotes = whoseNotes;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }
}
