package com.changeplusplus.survivorfitness.backendapi.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer sessionOrderNumber;
    private Date logDate;

    @ManyToOne
    @JoinColumn(name="program_id", nullable=false)
    private Program program;

    @OneToOne(optional = true)
    private SessionMeasurements sessionMeasurements;

    private String specialistNotes;
    private String adminNotes;
    private long sessionIndexNumber;

    public Session() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSessionOrderNumber() {
        return sessionOrderNumber;
    }

    public void setSessionOrderNumber(Integer sessionOrderNumber) {
        this.sessionOrderNumber = sessionOrderNumber;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public SessionMeasurements getSessionMeasurements() {
        return sessionMeasurements;
    }

    public void setSessionMeasurements(SessionMeasurements sessionMeasurements) {
        this.sessionMeasurements = sessionMeasurements;
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

    public long getSessionIndexNumber() {
        return sessionIndexNumber;
    }

    public void setSessionIndexNumber(long sessionIndexNumber) {
        this.sessionIndexNumber = sessionIndexNumber;
    }
}
