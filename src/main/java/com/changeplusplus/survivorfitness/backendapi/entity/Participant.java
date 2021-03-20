package com.changeplusplus.survivorfitness.backendapi.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pid")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    private Integer age;
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "start_date")
    private Date startDate;

    private String goals;

    @Column(name = "type_of_cancer")
    private String typeOfCancer;

    @Column(name = "treatment_facility")
    private String formsOfTreatment;
    private String surgeries;
    private String physicianNotes;

    @OneToMany(mappedBy = "participant")
    private List<ParticipantAssignment> assignments;


    public Participant() {
        super();
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

    public List<ParticipantAssignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<ParticipantAssignment> assignments) {
        this.assignments = assignments;
    }
}
