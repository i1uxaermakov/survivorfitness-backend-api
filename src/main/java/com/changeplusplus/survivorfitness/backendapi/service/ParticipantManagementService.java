package com.changeplusplus.survivorfitness.backendapi.service;

import com.changeplusplus.survivorfitness.backendapi.dto.LocationDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.ParticipantDTO;
import com.changeplusplus.survivorfitness.backendapi.dto.UserDTO;
import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.entity.projection.ParticipantGeneralInfoProjection;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParticipantManagementService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private UserRepository userRepository;

    public ParticipantDTO getParticipantInfoById(Integer participantId) {
        Participant participantEntity = participantRepository.findParticipantById(participantId);
        if(participantEntity == null) {
            return null;
        }

        ParticipantDTO participantDTO = new ParticipantDTO();
        participantDTO.setId(participantEntity.getId());
        participantDTO.setFirstName(participantEntity.getFirstName());
        participantDTO.setLastName(participantEntity.getLastName());
        participantDTO.setAge(participantEntity.getAge());
        participantDTO.setEmail(participantEntity.getEmail());
        participantDTO.setPhoneNumber(participantEntity.getPhoneNumber());
        participantDTO.setStartDate(participantEntity.getStartDate());
        participantDTO.setGoals(participantEntity.getGoals());
        participantDTO.setTypeOfCancer(participantEntity.getTypeOfCancer());
        participantDTO.setFormsOfTreatment(participantEntity.getFormsOfTreatment());
        participantDTO.setSurgeries(participantEntity.getSurgeries());
        participantDTO.setPhysicianNotes(participantEntity.getPhysicianNotes());

        setInformationAboutAssignedSpecialistsInParticipantDTO(participantDTO, participantEntity.getAssignments());

        return participantDTO;
    }

    public List<ParticipantDTO> getGeneralInfoAboutAllParticipants() {
        //Get general info (name, id, specialists they are assigned to) about all participants
        List<ParticipantGeneralInfoProjection> participantsRawInfo = participantRepository.findAllProjectedBy();

        //Convert data received from the database into the format expected by the client (list of ParticpantDTOs)
        List<ParticipantDTO> participantsPreparedInfo = new ArrayList<>();
        for(ParticipantGeneralInfoProjection projection: participantsRawInfo) {
            ParticipantDTO participantDTO = new ParticipantDTO();
            participantDTO.setId(projection.getId());
            participantDTO.setFirstName(projection.getFirstName());
            participantDTO.setLastName(projection.getLastName());

            setInformationAboutAssignedSpecialistsInParticipantDTO(participantDTO, projection.getAssignments());

            participantsPreparedInfo.add(participantDTO);
        }

        return participantsPreparedInfo;
    }


    private void setInformationAboutAssignedSpecialistsInParticipantDTO(ParticipantDTO participantDTO, List<ParticipantAssignment> assignments) {
        //Go over Assignments to find info about the trainer and the dietician of the participant
        for(ParticipantAssignment assignment: assignments) {
            User specialistEntity = assignment.getSpecialist();
            Location location = assignment.getLocation();

            UserDTO specialistDTO = new UserDTO();
            specialistDTO.setFirstName(specialistEntity.getFirstName());
            specialistDTO.setLastName(specialistEntity.getLastName());
            specialistDTO.setId(specialistEntity.getId());
            participantDTO.setDietician(specialistDTO);

            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(location.getId());
            locationDTO.setName(location.getName());

            if(assignment.getSpecialistType() == SpecialistType.DIETICIAN) {
                participantDTO.setDietician(specialistDTO);
                participantDTO.setDieticianLocation(locationDTO);
            }
            else if(assignment.getSpecialistType() == SpecialistType.TRAINER){
                participantDTO.setTrainer(specialistDTO);
                participantDTO.setTrainerLocation(locationDTO);
            }
        }
    }
}
