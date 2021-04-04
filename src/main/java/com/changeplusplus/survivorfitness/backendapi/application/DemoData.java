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
        User dietician1 = new User();
        dietician1.setFirstName("DieticianName1");
        dietician1.setLastName("DieticianLastName1");

        User trainer1 = new User();
        trainer1.setFirstName("TrainerName1");
        trainer1.setLastName("TrainerLastName1");


        //Data for the Assignment of the second Client
        User dietician2 = new User();
        dietician2.setFirstName("DieticianName2");
        dietician2.setLastName("DieticianLastName2");

        User trainer2 = new User();
        trainer2.setFirstName("TrainerName2");
        trainer2.setLastName("TrainerLastName2");

        userRepository.saveAll(Arrays.asList(dietician1, dietician2, trainer1, trainer2));


        Location trainerOffice = new Location();
        trainerOffice.setName("Effects Fitness");
        trainerOffice.setType(LocationType.TRAINER_GYM);
        trainerOffice.setAddress("4793 Rio Vista Ave Nashville, TN 37203");
        trainerOffice.setAdministrator(trainer2);


        Location dieticianOffice = new Location();
        dieticianOffice.setName("Balance Nutrition");
        dieticianOffice.setType(LocationType.DIETICIAN_OFFICE);
        dieticianOffice.setAddress("101 Fairmont Place Nashville, TN 37203");
        dieticianOffice.setAdministrator(dietician1);

        locationRepository.saveAll(Arrays.asList(trainerOffice, dieticianOffice));

        //creating assignments
        ParticipantAssignment assignmentParticipant1dietician = new ParticipantAssignment();
        assignmentParticipant1dietician.setSpecialistType(SpecialistType.DIETICIAN);
        assignmentParticipant1dietician.setSpecialist(dietician1);
        assignmentParticipant1dietician.setParticipant(participant1);
        assignmentParticipant1dietician.setLocation(dieticianOffice);

        ParticipantAssignment assignmentParticipant2dietician = new ParticipantAssignment();
        assignmentParticipant2dietician.setSpecialistType(SpecialistType.DIETICIAN);
        assignmentParticipant2dietician.setSpecialist(dietician2);
        assignmentParticipant2dietician.setParticipant(participant2);
        assignmentParticipant2dietician.setLocation(dieticianOffice);


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
                assignmentParticipant2dietician, assignmentParticipant1dietician));
    }
}