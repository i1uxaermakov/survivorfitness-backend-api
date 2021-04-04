package com.changeplusplus.survivorfitness.backendapi.application;

import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.LocationRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantAssignmentRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
    private ParticipantAssignmentRepository participantAssignmentRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {

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

        participantRepository.saveAll(Arrays.asList(participant1, participant2));

        //Data for the Assignment of the first Client
        User dietitian1 = new User();
        dietitian1.setFirstName("DietitianName1");
        dietitian1.setLastName("DietitianLastName1");

        User trainer1 = new User();
        trainer1.setFirstName("TrainerName1");
        trainer1.setLastName("TrainerLastName1");


        //Data for the Assignment of the second Client
        User dietitian2 = new User();
        dietitian2.setFirstName("DietitianName2");
        dietitian2.setLastName("DietitianLastName2");

        User trainer2 = new User();
        trainer2.setFirstName("TrainerName2");
        trainer2.setLastName("TrainerLastName2");

        userRepository.saveAll(Arrays.asList(dietitian1, dietitian2, trainer1, trainer2));


        Location trainerOffice = new Location();
        trainerOffice.setName("Effects Fitness");
        trainerOffice.setType(LocationType.TRAINER_GYM);
        trainerOffice.setAddress("4793 Rio Vista Ave Nashville, TN 37203");
        trainerOffice.setAdministrator(trainer2);


        Location dietitianOffice = new Location();
        dietitianOffice.setName("Balance Nutrition");
        dietitianOffice.setType(LocationType.DIETICIAN_OFFICE);
        dietitianOffice.setAddress("101 Fairmont Place Nashville, TN 37203");
        dietitianOffice.setAdministrator(dietitian1);

        locationRepository.saveAll(Arrays.asList(trainerOffice, dietitianOffice));

        //creating assignments
        ParticipantAssignment assignmentParticipant1dietitian = new ParticipantAssignment();
        assignmentParticipant1dietitian.setSpecialistType(SpecialistType.DIETITIAN);
        assignmentParticipant1dietitian.setSpecialist(dietitian1);
        assignmentParticipant1dietitian.setParticipant(participant1);
        assignmentParticipant1dietitian.setLocation(dietitianOffice);

        ParticipantAssignment assignmentParticipant2dietitian = new ParticipantAssignment();
        assignmentParticipant2dietitian.setSpecialistType(SpecialistType.DIETITIAN);
        assignmentParticipant2dietitian.setSpecialist(dietitian2);
        assignmentParticipant2dietitian.setParticipant(participant2);
        assignmentParticipant2dietitian.setLocation(dietitianOffice);


        ParticipantAssignment assignmentParticipant1trainer= new ParticipantAssignment();
        assignmentParticipant1trainer.setSpecialistType(SpecialistType.TRAINER);
        assignmentParticipant1trainer.setSpecialist(trainer1);
        assignmentParticipant1trainer.setParticipant(participant1);
        assignmentParticipant1trainer.setLocation(trainerOffice);

        ParticipantAssignment assignmentParticipant2trainer= new ParticipantAssignment();
        assignmentParticipant2trainer.setSpecialistType(SpecialistType.TRAINER);
        assignmentParticipant2trainer.setSpecialist(trainer2);
        assignmentParticipant2trainer.setParticipant(participant2);
        assignmentParticipant2trainer.setLocation(trainerOffice);

        participantAssignmentRepository.saveAll(Arrays.asList(
                assignmentParticipant2trainer, assignmentParticipant1trainer,
                assignmentParticipant2dietitian, assignmentParticipant1dietitian));
    }
}