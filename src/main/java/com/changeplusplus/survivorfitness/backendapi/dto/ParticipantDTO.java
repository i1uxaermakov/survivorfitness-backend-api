package com.changeplusplus.survivorfitness.backendapi.dto;

import com.changeplusplus.survivorfitness.backendapi.entity.Participant;
import com.changeplusplus.survivorfitness.backendapi.entity.ParticipantAssignment;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;


public class ParticipantDTO {
    private Integer id;
    private String firstName;
    private String lastName;

    public ParticipantDTO() {
        super();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer age;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phoneNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date startDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String goals;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String typeOfCancer;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String formsOfTreatment;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String surgeries;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String physicianNotes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDTO dietician;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocationDTO dieticianLocation;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDTO trainer;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocationDTO trainerLocation;

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

    public UserDTO getDietician() {
        return dietician;
    }

    public void setDietician(UserDTO dietician) {
        this.dietician = dietician;
    }

    public LocationDTO getDieticianLocation() {
        return dieticianLocation;
    }

    public void setDieticianLocation(LocationDTO dieticianLocation) {
        this.dieticianLocation = dieticianLocation;
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
