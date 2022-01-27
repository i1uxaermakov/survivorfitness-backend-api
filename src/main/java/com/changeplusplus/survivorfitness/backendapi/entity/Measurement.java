package com.changeplusplus.survivorfitness.backendapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "m_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="s_id", nullable=false)
    private Session session;

    private String name;
    private String value;
    private String category;
    private String unit;

    public Measurement(Session session, String name, String value, String category, String unit) {
        this.session = session;
        this.name = name;
        this.value = value;
        this.category = category;
        this.unit = unit;
    }
}
