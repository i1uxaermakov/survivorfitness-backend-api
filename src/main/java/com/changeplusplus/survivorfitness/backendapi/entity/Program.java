package com.changeplusplus.survivorfitness.backendapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "program_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ProgramProgressStatus programProgressStatus;

    @OneToMany(mappedBy="program", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Where(clause = "whose_notes = 'TRAINER'")
    private List<Session> trainerSessions = new ArrayList<>();

    @OneToMany(mappedBy="program", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Where(clause = "whose_notes = 'DIETITIAN'")
    private List<Session> dietitianSessions = new ArrayList<>();

    @OneToOne(mappedBy = "treatmentProgram", fetch = FetchType.LAZY)
    private Participant participant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="trainer_user_id")
    private User trainer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="trainer_location_id")
    private Location trainerGym;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="dietitian_user_id")
    private User dietitian;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="dietitian_location_id")
    private Location dietitianOffice;
}
