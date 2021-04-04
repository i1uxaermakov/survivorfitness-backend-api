package com.changeplusplus.survivorfitness.backendapi.application;

import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;

@Component
public class DemoData {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @EventListener
    @Transactional
    public void appReady(ApplicationReadyEvent event) {
        //Creating Roles within the system
        UserRole dietitianRole =  new UserRole(UserRoleName.DIETITIAN);
        UserRole trainerRole =  new UserRole(UserRoleName.TRAINER);
        UserRole locationAdministratorRole =  new UserRole(UserRoleName.LOCATION_ADMINISTRATOR);

        userRoleRepository.saveAll(Arrays.asList(dietitianRole,trainerRole,locationAdministratorRole));

        //Data for the Assignment of the first Client
        User dietitian1 = new User();
        dietitian1.setFirstName("DietitianName1");
        dietitian1.setLastName("DietitianLastName1");
        dietitian1.getRoles().add(dietitianRole);

        User trainer1 = new User();
        trainer1.setFirstName("TrainerName1");
        trainer1.setLastName("TrainerLastName1");
        trainer1.getRoles().add(trainerRole);


        //Data for the Assignment of the second Client
        User dietitian2 = new User();
        dietitian2.setFirstName("DietitianName2");
        dietitian2.setLastName("DietitianLastName2");
        dietitian2.getRoles().add(dietitianRole);


        User trainer2 = new User();
        trainer2.setFirstName("TrainerName2");
        trainer2.setLastName("TrainerLastName2");
        trainer2.getRoles().add(trainerRole);

        userRepository.saveAll(Arrays.asList(dietitian1, dietitian2, trainer1, trainer2));


        Location trainerOffice = new Location();
        trainerOffice.setName("Effects Fitness");
        trainerOffice.setType(LocationType.TRAINER_GYM);
        trainerOffice.setAddress("4793 Rio Vista Ave Nashville, TN 37203");
        trainerOffice.setAdministrator(trainer2);
        trainer2.getRoles().add(locationAdministratorRole);
        trainer2.getLocationsAssignedTo().add(trainerOffice);
        trainer1.getLocationsAssignedTo().add(trainerOffice);



        Location dietitianOffice = new Location();
        dietitianOffice.setName("Balance Nutrition");
        dietitianOffice.setType(LocationType.DIETICIAN_OFFICE);
        dietitianOffice.setAddress("101 Fairmont Place Nashville, TN 37203");
        dietitianOffice.setAdministrator(dietitian1);
        dietitian1.getRoles().add(locationAdministratorRole);
        dietitian1.getLocationsAssignedTo().add(dietitianOffice);
        dietitian2.getLocationsAssignedTo().add(dietitianOffice);


        //userRepository.saveAll(Arrays.asList(dietitian1, trainer2));
        locationRepository.saveAll(Arrays.asList(trainerOffice, dietitianOffice));


        Participant participant1 = new Participant(
                "Ilya",
                "Ermakov",
                21,
                "ilya@website.com",
                "6154389404",
                new Date(),
                "goals",
                "typeOfCancer",
                "forms of treatment",
                "surgeries",
                "physicianNotes");
        participant1.setDietitianOffice(dietitianOffice);
        participant1.setDietitian(dietitian1);
        participant1.setTrainerGym(trainerOffice);
        participant1.setTrainer(trainer1);

        Participant participant2 = new Participant(
                "Ilya2",
                "Ermakov2",
                21,
                "ilya@website.com2",
                "61543894042",
                new Date(),
                "goals2",
                "typeOfCancer2",
                "forms of treatment2",
                "surgeries2",
                "physicianNotes2");
        participant2.setDietitianOffice(dietitianOffice);
        participant2.setDietitian(dietitian2);
        participant2.setTrainerGym(trainerOffice);
        participant2.setTrainer(trainer2);

        participantRepository.saveAll(Arrays.asList(participant1, participant2));
    }
}