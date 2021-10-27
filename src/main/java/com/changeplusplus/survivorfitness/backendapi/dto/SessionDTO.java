package com.changeplusplus.survivorfitness.backendapi.dto;

import com.changeplusplus.survivorfitness.backendapi.entity.Measurement;
import com.changeplusplus.survivorfitness.backendapi.entity.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SessionDTO {
    /*
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer sessionIndexNumber;
    private Date logDate;

    @Enumerated(EnumType.STRING)
    private SpecialistType whoseNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="program_id", nullable=false)
    private Program program;

    @ManyToOne(fetch = FetchType.LAZY)
    private Participant participant;

    private String specialistNotes;
    private String adminNotes;
     */

    private Integer id;
    private Long initialLogDate;
    private Long lastUpdatedDate;
    private String specialistNotes;
    private String adminNotes;
    private Integer sessionIndexNumber;
    private String whoseNotes;
    private Integer participantId;
    private List<MeasurementDTO> measurements;

    public SessionDTO(Session sessionEntity) {
        this.id = sessionEntity.getId();
        this.sessionIndexNumber = sessionEntity.getSessionIndexNumber();
        this.specialistNotes = sessionEntity.getSpecialistNotes();
        this.adminNotes = sessionEntity.getAdminNotes();
        this.initialLogDate = sessionEntity.getInitialLogDate().getTime();
        this.whoseNotes = sessionEntity.getWhoseNotes().toString();
        this.participantId = sessionEntity.getParticipant().getId();
        this.lastUpdatedDate = sessionEntity.getLastUpdatedDate().getTime();

        this.measurements = new ArrayList<>();
        for(Measurement measurementEntity: sessionEntity.getMeasurements()) {
            this.measurements.add(new MeasurementDTO(measurementEntity));
        }
    }

    public SessionDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getInitialLogDate() {
        return initialLogDate;
    }

    public void setInitialLogDate(Long initialLogDate) {
        this.initialLogDate = initialLogDate;
    }

    public void setLastUpdatedDate(Long lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
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

    public String getWhoseNotes() {
        return whoseNotes;
    }

    public void setWhoseNotes(String whoseNotes) {
        this.whoseNotes = whoseNotes;
    }

    public Integer getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Integer participantId) {
        this.participantId = participantId;
    }

    public Long getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public List<MeasurementDTO> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<MeasurementDTO> measurements) {
        this.measurements = measurements;
    }
}
