package com.changeplusplus.survivorfitness.backendapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
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
}
