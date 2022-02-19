package com.changeplusplus.survivorfitness.backendapi.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name="location_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LocationAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "la_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)//todo mapping
    @JoinColumn(name="location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.EAGER)//todo mapping
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private UserRoleType userRoleType;

}
