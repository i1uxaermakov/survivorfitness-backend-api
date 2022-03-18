package com.changeplusplus.survivorfitness.backendapi.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name="location_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString
public class LocationAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "la_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="location_id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private UserRoleType userRoleType;

    public LocationAssignment(User user, Location location, UserRoleType userRoleType) {
        this.user = user;
        this.location = location;
        this.userRoleType = userRoleType;
    }
}
