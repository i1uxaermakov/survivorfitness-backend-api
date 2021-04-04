package com.changeplusplus.survivorfitness.backendapi.entity.projection;

import com.changeplusplus.survivorfitness.backendapi.entity.Location;
import com.changeplusplus.survivorfitness.backendapi.entity.User;

import java.util.List;

public interface ParticipantGeneralInfoProjection {
    Integer getId();
    String getFirstName();
    String getLastName();
    public Location getDietitianOffice();
    public Location getTrainerGym();
    public User getDietitian();
    public User getTrainer();
}
