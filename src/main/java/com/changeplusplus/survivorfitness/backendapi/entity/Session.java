package com.changeplusplus.survivorfitness.backendapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
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
}
