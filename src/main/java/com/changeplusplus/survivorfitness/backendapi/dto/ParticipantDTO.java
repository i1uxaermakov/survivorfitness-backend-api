package com.changeplusplus.survivorfitness.backendapi.dto;

import com.changeplusplus.survivorfitness.backendapi.entity.Participant;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipantDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String phoneNumber;
    private Date startDate;
    private String goals;
    private String typeOfCancer;
    private String formsOfTreatment;
    private String surgeries;
    private String physicianNotes;
    private UserDTO dietitian;
    private LocationDTO dietitianLocation;
    private UserDTO trainer;
    private LocationDTO trainerLocation;

    public ParticipantDTO() {
        super();
    }

    // doesn't set the fields related to trainers and dietitians
    public ParticipantDTO(Participant participant) {
        super();
        this.id = participant.getId();
        this.firstName = participant.getFirstName();
        this.lastName = participant.getLastName();
        this.age = participant.getAge();
        this.email = participant.getEmail();
        this.phoneNumber = participant.getPhoneNumber();
        this.startDate = participant.getStartDate();
        this.goals = participant.getGoals();
        this.typeOfCancer = participant.getTypeOfCancer();
        this.formsOfTreatment = participant.getFormsOfTreatment();
        this.surgeries = participant.getSurgeries();
        this.physicianNotes = participant.getPhysicianNotes();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getTypeOfCancer() {
        return typeOfCancer;
    }

    public void setTypeOfCancer(String typeOfCancer) {
        this.typeOfCancer = typeOfCancer;
    }

    public String getFormsOfTreatment() {
        return formsOfTreatment;
    }

    public void setFormsOfTreatment(String formsOfTreatment) {
        this.formsOfTreatment = formsOfTreatment;
    }

    public String getSurgeries() {
        return surgeries;
    }

    public void setSurgeries(String surgeries) {
        this.surgeries = surgeries;
    }

    public String getPhysicianNotes() {
        return physicianNotes;
    }

    public void setPhysicianNotes(String physicianNotes) {
        this.physicianNotes = physicianNotes;
    }

    public UserDTO getDietitian() {
        return dietitian;
    }

    public void setDietitian(UserDTO dietitian) {
        this.dietitian = dietitian;
    }

    public LocationDTO getDietitianLocation() {
        return dietitianLocation;
    }

    public void setDietitianLocation(LocationDTO dietitianLocation) {
        this.dietitianLocation = dietitianLocation;
    }

    public UserDTO getTrainer() {
        return trainer;
    }

    public void setTrainer(UserDTO trainer) {
        this.trainer = trainer;
    }

    public LocationDTO getTrainerLocation() {
        return trainerLocation;
    }

    public void setTrainerLocation(LocationDTO trainerLocation) {
        this.trainerLocation = trainerLocation;
    }
}
