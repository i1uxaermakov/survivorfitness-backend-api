package com.changeplusplus.survivorfitness.backendapi.entity.projection;

public interface UserOnlyNameProjection {

    /*
    @Column(name = "uid")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
     */

    Integer getId();
    String getFirstName();
    String getLastName();
}
