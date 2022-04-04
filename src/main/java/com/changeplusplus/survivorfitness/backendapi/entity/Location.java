package com.changeplusplus.survivorfitness.backendapi.entity;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "locations")
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lid")
    private Integer id;
    private String name;
    private String address;

    @Column(name = "location_type")
    @Enumerated(EnumType.STRING)
    private LocationType type;

    @ManyToOne(optional = false)
    @JoinColumn(name="admin_user_id")
    private User administrator;

    public Location(LocationDTO locationDTO) {
        this.address = locationDTO.getAddress();
        this.name = locationDTO.getName();
        this.type = LocationType.valueOf(locationDTO.getType());
    }
}
