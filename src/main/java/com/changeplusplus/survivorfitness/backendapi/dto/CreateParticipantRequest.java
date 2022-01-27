package com.changeplusplus.survivorfitness.backendapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateParticipantRequest {
    private ParticipantDTO participant;
    private Integer numberOfTrainerSessions;
    private Integer numberOfDietitianSessions;
    private List<Integer> sessionsIndicesWhenMeasurementsAreTaken;
    private List<MeasurementDTO> measurements;
}
