package com.changeplusplus.survivorfitness.backendapi.entity;

import javax.persistence.*;
import java.util.Date;

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

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", referencedColumnName = "program_id")
    private Program treatmentProgram;

    private String formsOfTreatment;
    private String surgeries;
    private String physicianNotes;

//    @ManyToOne
//    @JoinColumn(name="dietitian_location_id")
//    private Location dietitianOffice;
//
//    @ManyToOne
//    @JoinColumn(name="gym_location_id")
//    private Location trainerGym;
//
//    @ManyToOne
//    @JoinColumn(name="dietitian_user_id")
//    private User dietitian;
//
//    @ManyToOne
//    @JoinColumn(name="trainer_user_id")
//    private User trainer;


    public Participant() {
        super();
    }

    public Participant(String firstName, String lastName, Integer age, String email, String phoneNumber, Date startDate, String goals, String typeOfCancer, String formsOfTreatment, String surgeries, String physicianNotes) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.goals = goals;
        this.typeOfCancer = typeOfCancer;
        this.formsOfTreatment = formsOfTreatment;
        this.surgeries = surgeries;
        this.physicianNotes = physicianNotes;
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

//    public Location getDietitianOffice() {
//        return dietitianOffice;
//    }
//
//    public void setDietitianOffice(Location dietitianOfficeAssigned) {
//        this.dietitianOffice = dietitianOfficeAssigned;
//    }
//
//    public Location getTrainerGym() {
//        return trainerGym;
//    }
//
//    public void setTrainerGym(Location trainerGymAssigned) {
//        this.trainerGym = trainerGymAssigned;
//    }
//
//    public User getDietitian() {
//        return dietitian;
//    }
//
//    public void setDietitian(User dietitianAssigned) {
//        this.dietitian = dietitianAssigned;
//    }
//
//    public User getTrainer() {
//        return trainer;
//    }
//
//    public void setTrainer(User trainerAssigned) {
//        this.trainer = trainerAssigned;
//    }


    public Program getTreatmentProgram() {
        return treatmentProgram;
    }

    public void setTreatmentProgram(Program treatmentProgram) {
        this.treatmentProgram = treatmentProgram;
    }
}
