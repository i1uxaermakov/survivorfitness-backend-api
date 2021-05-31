package com.changeplusplus.survivorfitness.backendapi.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private UserRoleType name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<User> usersWithRole = new ArrayList<>();

    public UserRole() {
        super();
    }

    public UserRole(UserRoleType name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserRoleType getName() {
        return name;
    }

    public void setName(UserRoleType name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return name == userRole.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public List<User> getUsersWithRole() {
        return usersWithRole;
    }

    public void setUsersWithRole(List<User> usersWithRole) {
        this.usersWithRole = usersWithRole;
    }
}
