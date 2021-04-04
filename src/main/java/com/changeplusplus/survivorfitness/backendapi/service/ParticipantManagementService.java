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

        assignLocationsAndSpecialistsOfUserToDTO(
                participantEntity.getDietitian(), participantEntity.getTrainer(),
                participantEntity.getDietitianOffice(), participantEntity.getTrainerGym(),
                participantDTO);

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

            assignLocationsAndSpecialistsOfUserToDTO(
                    projection.getDietitian(), projection.getTrainer(),
                    projection.getDietitianOffice(), projection.getTrainerGym(),
                    participantDTO);

            participantsPreparedInfo.add(participantDTO);
        }

        return participantsPreparedInfo;
    }


    private void assignLocationsAndSpecialistsOfUserToDTO(User dietitian, User trainer,
                                                          Location dietitianOffice, Location trainerGym,
                                                          ParticipantDTO participantDTO) {
        participantDTO.setDietitianLocation(getConciseLocationDTOBasedOnLocationEntity(dietitianOffice));
        participantDTO.setTrainerLocation(getConciseLocationDTOBasedOnLocationEntity(trainerGym));

        participantDTO.setTrainer(getConciseUserDTOBasedOnUserEntity(trainer));
        participantDTO.setDietitian(getConciseUserDTOBasedOnUserEntity(dietitian));
    }


    private LocationDTO getConciseLocationDTOBasedOnLocationEntity(Location locationEntity) {
        if(locationEntity == null) {
            return null;
        }

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(locationEntity.getId());
        locationDTO.setName(locationEntity.getName());
        locationDTO.setType(locationEntity.getType().toString());

        return locationDTO;
    }


    private UserDTO getConciseUserDTOBasedOnUserEntity(User specialistEntity) {
        if(specialistEntity == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(specialistEntity.getId());
        userDTO.setFirstName(specialistEntity.getFirstName());
        userDTO.setLastName(specialistEntity.getLastName());

        return userDTO;
    }
}
