package com.changeplusplus.survivorfitness.backendapi.entity.projection;

import com.changeplusplus.survivorfitness.backendapi.entity.ParticipantAssignment;

import java.util.List;

public interface ParticipantGeneralInfoProjection {
    Integer getId();
    String getFirstName();
    String getLastName();
    List<ParticipantAssignment> getAssignments();
}
