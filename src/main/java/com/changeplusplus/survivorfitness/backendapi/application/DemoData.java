package com.changeplusplus.survivorfitness.backendapi.application;

import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class DemoData {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @EventListener
    @Transactional
    public void appReady(ApplicationReadyEvent event) {
        //Creating Roles within the system
        UserRole dietitianRole =  new UserRole(UserRoleType.DIETITIAN);
        UserRole trainerRole =  new UserRole(UserRoleType.TRAINER);
        UserRole locationAdministratorRole =  new UserRole(UserRoleType.LOCATION_ADMINISTRATOR);
        UserRole superAdminRole = new UserRole(UserRoleType.SUPER_ADMIN);
        //userRoleRepository.saveAll(Arrays.asList(dietitianRole,trainerRole,locationAdministratorRole,superAdminRole));


        // --------------------- BEGIN USER CREATION ---------------------

        //Creating 4 users: 2 dietitians and 2 trainers
        List<User> userList = new ArrayList<>();

        // Dietitians 1-2 will be assigned to dietitianOffice1
        User dietitian1 = new User();
        dietitian1.setFirstName("Sri");
        dietitian1.setLastName("Karolyn");
        dietitian1.setEmail("sri.karolyn@gmail.olala");
        dietitian1.setPassword("$2y$10$t.bDbsrV4zHcjd4vp1mJeeZpqGkvVXnA0oxndf87GXx8.Ss2Ik6bW"); //passwordSri
        dietitian1.setPhoneNumber("123 456 7890");
        dietitian1.setEnabled(true);
        dietitian1.getRoles().add(dietitianRole);
        userList.add(dietitian1);

        User dietitian2 = new User();
        dietitian2.setFirstName("Decima");
        dietitian2.setLastName("Arjuna");
        dietitian2.setEmail("decima.arjuna@gmail.olala");
        dietitian2.setPassword("$2y$10$s4v/XFDayJUh7P9CjhE/BeOC4Ah0JJ4ApZmMbXQgE2JVc8N6EGoqS"); //passwordDecima
        dietitian2.setPhoneNumber("123 456 7890");
        dietitian2.setEnabled(true);
        dietitian2.getRoles().add(dietitianRole);
        userList.add(dietitian2);

        // Dietitians 3-5 will be assigned to dietitianOffice2
        User dietitian3 = new User();
        dietitian3.setFirstName("Kapil");
        dietitian3.setLastName("Mirjami");
        dietitian3.setEmail("Kapil.Mirjami@gmail.olala");
        dietitian3.setPassword("$2y$10$3WTba/10y9wrYNrvc1gKfe4xgSUCEWJpqEDZjp1jEpo4gH15djwxK"); //passwordKapil
        dietitian3.setPhoneNumber("123 456 7890");
        dietitian3.setEnabled(true);
        dietitian3.getRoles().add(dietitianRole);
        userList.add(dietitian3);

        User dietitian4 = new User();
        dietitian4.setFirstName("Aleksanteri");
        dietitian4.setLastName("Vera");
        dietitian4.setEmail("Aleksanteri.Vera@gmail.olala");
        dietitian4.setPassword("$2y$10$SlB9vI8L2fQXPam9dgOJw.J1fkLYe8xgOGNbNu72Qtpxbr9ql6ywm"); //passwordAleksanteri
        dietitian4.setPhoneNumber("123 456 7890");
        dietitian4.setEnabled(true);
        dietitian4.getRoles().add(dietitianRole);
        userList.add(dietitian4);

        User dietitian5 = new User();
        dietitian5.setFirstName("Maple");
        dietitian5.setLastName("Tina");
        dietitian5.setEmail("Maple.Tina@gmail.olala");
        dietitian5.setPassword("$2y$10$p8HdMCeILpg9.nPbH.qJc.lsuLZjNEoSFE3nV7ZoOc8cmcZHFIbd2"); //passwordMaple
        dietitian5.setPhoneNumber("123 456 7890");
        dietitian5.setEnabled(true);
        dietitian5.getRoles().add(dietitianRole);
        userList.add(dietitian5);

        // trainers 1-3 will be assigned to gym1
        User trainer1 = new User();
        trainer1.setFirstName("Nikitha");
        trainer1.setLastName("Shantel");
        trainer1.setEmail("nikitha.shantel@gmail.olala");
        trainer1.setPassword("$2y$10$ndQfmq0yOtv.9YlufxwrSeBLttr4UIBhnqxn5nNtgypRif1uXKlZi"); //passwordNikitha
        trainer1.setPhoneNumber("123 456 7890");
        trainer1.setEnabled(true);
        trainer1.getRoles().add(trainerRole);
        userList.add(trainer1);

        User trainer2 = new User();
        trainer2.setFirstName("Iris");
        trainer2.setLastName("Kismet");
        trainer2.setEmail("iris.kismet@gmail.olala");
        trainer2.setPassword("$2y$10$UBskGYSibAH99d.43757/OjSRAJyj/8ZxscpA9xHm3RM0.cKXb4uy"); //passwordIris
        trainer2.setPhoneNumber("123 456 7890");
        trainer2.setEnabled(true);
        trainer2.getRoles().add(trainerRole);
        userList.add(trainer2);

        User trainer3 = new User();
        trainer3.setFirstName("Marita");
        trainer3.setLastName("Dominik");
        trainer3.setEmail("Marita.Dominik@gmail.olala");
        trainer3.setPassword("$2y$10$K.vpt9cccteMabQLp43m3.pAnRlDk9ct4iLPgrUCE3J3aw8PE9LgG"); //passwordMarita
        trainer3.setPhoneNumber("123 456 7890");
        trainer3.setEnabled(true);
        trainer3.getRoles().add(trainerRole);
        userList.add(trainer3);

        // trainers 4-6 will be assigned to gym2
        User trainer4 = new User();
        trainer4.setFirstName("Herbert");
        trainer4.setLastName("Adomas");
        trainer4.setEmail("Herbert.Adomas@gmail.olala");
        trainer4.setPassword("$2y$10$.yUgLxVpwaVoXGPekyWae.to2IxWT4D8boBY.xlbiSuHiuRmkjsKO"); //passwordHerbert
        trainer4.setPhoneNumber("123 456 7890");
        trainer4.setEnabled(true);
        trainer4.getRoles().add(trainerRole);
        userList.add(trainer4);

        User trainer5 = new User();
        trainer5.setFirstName("Marciana");
        trainer5.setLastName("Magne");
        trainer5.setEmail("Marciana.Magne@gmail.olala");
        trainer5.setPassword("$2y$10$mvx.kwSUuKj.cKv0WvUhNeWQ9o4LbKUmjTkptBCSrHi6BivOhsFga"); //passwordMarciana
        trainer5.setPhoneNumber("123 456 7890");
        trainer5.setEnabled(true);
        trainer5.getRoles().add(trainerRole);
        userList.add(trainer5);

        User trainer6 = new User();
        trainer6.setFirstName("Frieda");
        trainer6.setLastName("Witold");
        trainer6.setEmail("Frieda.Witold@gmail.olala");
        trainer6.setPassword("$2y$10$E2A4WEsRxQtRfMD/lUUV7.Bmnwlb3fCCsJ8AburohZAkMcmzwFs4y"); //passwordFrieda
        trainer6.setPhoneNumber("123 456 7890");
        trainer6.setEnabled(true);
        trainer6.getRoles().add(trainerRole);
        userList.add(trainer6);

        //Creating a super admin
        User super_admin1 = new User();
        super_admin1.setFirstName("Theo");
        super_admin1.setLastName("Justin");
        super_admin1.setEmail("theo.justin@gmail.olala");
        super_admin1.setPassword("$2y$10$V3MWloOYB/Gu3.a9dltBquAqLTq/VshYXesr8p4UiYdTtMiPTXxtq"); //passwordTheo
        super_admin1.setPhoneNumber("123 456 7890");
        super_admin1.setEnabled(true);
        super_admin1.getRoles().add(superAdminRole);
        userList.add(super_admin1);


        userRepository.saveAll(userList);

        // --------------------- END USER CREATION ---------------------


        // --------------------- BEGIN LOCATION CREATION ---------------------
        List<Location> locationList = new ArrayList<>();

        Location gym1 = new Location();
        gym1.setName("Effects Fitness");
        gym1.setType(LocationType.TRAINER_GYM);
        gym1.setAddress("4793 Rio Vista Ave Nashville, TN 37203");
        gym1.setAdministrator(trainer1);
        trainer1.getRoles().add(locationAdministratorRole);
        trainer1.getLocationsAssignedTo().add(gym1);
        trainer2.getLocationsAssignedTo().add(gym1);
        trainer3.getLocationsAssignedTo().add(gym1);
        locationList.add(gym1);

        Location gym2 = new Location();
        gym2.setName("Life Fitness Academy");
        gym2.setType(LocationType.TRAINER_GYM);
        gym2.setAddress("1200 Villa Pl, Nashville, TN 37212");
        gym2.setAdministrator(trainer4);
        trainer4.getRoles().add(locationAdministratorRole);
        trainer4.getLocationsAssignedTo().add(gym2);
        trainer5.getLocationsAssignedTo().add(gym2);
        trainer6.getLocationsAssignedTo().add(gym2);
        locationList.add(gym2);

        Location dietitianOffice1 = new Location();
        dietitianOffice1.setName("Balance Nutrition");
        dietitianOffice1.setType(LocationType.DIETICIAN_OFFICE);
        dietitianOffice1.setAddress("101 Fairmont Place Nashville, TN 37203");
        dietitianOffice1.setAdministrator(dietitian1);
        dietitian1.getRoles().add(locationAdministratorRole);
        dietitian1.getLocationsAssignedTo().add(dietitianOffice1);
        dietitian2.getLocationsAssignedTo().add(dietitianOffice1);
        locationList.add(dietitianOffice1);

        Location dietitianOffice2 = new Location();
        dietitianOffice2.setName("Healthy Food Laboratory");
        dietitianOffice2.setType(LocationType.DIETICIAN_OFFICE);
        dietitianOffice2.setAddress("114-A 29th Ave N, Nashville, TN 37203");
        dietitianOffice2.setAdministrator(dietitian3);
        dietitian3.getRoles().add(locationAdministratorRole);
        dietitian3.getLocationsAssignedTo().add(dietitianOffice2);
        dietitian4.getLocationsAssignedTo().add(dietitianOffice2);
        dietitian5.getLocationsAssignedTo().add(dietitianOffice2);
        locationList.add(dietitianOffice2);

        locationRepository.saveAll(locationList);
        // --------------------- END LOCATION CREATION ---------------------



        // --------------------- BEGIN PARTICIPANT CREATION ---------------------
        List<Participant> participantList = new ArrayList<>();

        // trainers 1-3 are in gym1; trainers 4-6 are in gym2
        // dietitians 1-2 are in office1, dietitians 3-5 are in office2

        // dietitian1 should have 3 people assigned to them
        // dietitian2 should have 4 people assigned to them
        // trainer1 should have 1 person assigned to them
        // trainer2 should have 2 people assigned to them
        // trainer3 should have 4 people assigned to them
        participantList.add(getSampleParticipant("Jela","Cornelius", 21, dietitianOffice1, dietitian1, gym1, trainer1));
        participantList.add(getSampleParticipant("Erica","Rhode", 22, dietitianOffice1, dietitian1, gym1, trainer2));
        participantList.add(getSampleParticipant("Ilham","Kody", 23, dietitianOffice1, dietitian1, gym1, trainer3));
        participantList.add(getSampleParticipant("Floor","Nelson", 25, dietitianOffice1, dietitian2, gym1, trainer2));
        participantList.add(getSampleParticipant("Syuzanna","Mustapha", 26, dietitianOffice1, dietitian2, gym1, trainer3));
        participantList.add(getSampleParticipant("Lykourgos","Galena", 27, dietitianOffice1, dietitian2, gym1, trainer3));
        participantList.add(getSampleParticipant("Ffransis","Sancha", 28, dietitianOffice1, dietitian2, gym1, trainer3));

        // dietitian3 should have 2 people assigned to them
        // dietitian4 should have 2 people assigned to them
        // dietitian5 should have 3 people assigned to them
        // trainer4 should have 4 person assigned to them
        // trainer5 should have 2 people assigned to them
        // trainer6 should have 1 people assigned to them
        participantList.add(getSampleParticipant("Hugo","Martin", 29, dietitianOffice2, dietitian3, gym2, trainer4));
        participantList.add(getSampleParticipant("Tiras","Manish", 30, dietitianOffice2, dietitian3, gym2, trainer4));
        participantList.add(getSampleParticipant("Nitika","Aynura", 31, dietitianOffice2, dietitian4, gym2, trainer4));
        participantList.add(getSampleParticipant("Vidya","Triana", 32, dietitianOffice2, dietitian4, gym2, trainer4));
        participantList.add(getSampleParticipant("Punita","Septus", 33, dietitianOffice2, dietitian5, gym2, trainer5));
        participantList.add(getSampleParticipant("Celestino","Maureen", 34, dietitianOffice2, dietitian5, gym2, trainer5));
        participantList.add(getSampleParticipant("Joel","Papak", 35, dietitianOffice2, dietitian5, gym2, trainer6));

        participantRepository.saveAll(participantList);
        // --------------------- END PARTICIPANT CREATION ---------------------
    }



    private Participant getSampleParticipant(String firstName, String lastName, Integer age, Location dietitianOffice, User dietitian, Location gym, User trainer) {
        Participant participant = new Participant(
                firstName,
                lastName,
                age,
                firstName + "." + lastName + "@website.com",
                "123 456 7890",
                new Date(),
                "goals",
                "typeOfCancer",
                "forms of treatment",
                "surgeries",
                "loremipsu");
//        participant.setDietitianOffice(dietitianOffice);
//        participant.setDietitian(dietitian);
//        participant.setTrainerGym(gym);
//        participant.setTrainer(trainer);


/*
    private ProgramProgressStatus programProgressStatus;
    private List<Session> sessions = new ArrayList<>();
    private Participant participant;
    private User trainer;
    private Location trainerGym;
    private User dietitian;
    private Location dietitianOffice;
 */

        Program treatmentProgram = new Program();
        treatmentProgram.setProgramProgressStatus(ProgramProgressStatus.IN_PROGRESS);
        treatmentProgram.setTrainer(trainer);
        treatmentProgram.setDietitian(dietitian);
        treatmentProgram.setDietitianOffice(dietitianOffice);
        treatmentProgram.setTrainerGym(gym);
        treatmentProgram.setParticipant(participant);
        treatmentProgram.setDietitianSessions(getDummySessionsForParticipantBySpecialist(participant, SpecialistType.DIETITIAN, treatmentProgram));
        treatmentProgram.setTrainerSessions(getDummySessionsForParticipantBySpecialist(participant, SpecialistType.TRAINER, treatmentProgram));

        participant.setTreatmentProgram(treatmentProgram);
        return participant;
    }


    private List<Session> getDummySessionsForParticipantBySpecialist(Participant participant, SpecialistType type, Program program) {
        Calendar c = Calendar.getInstance();

        int numberOfSessions;
        if(type == SpecialistType.DIETITIAN) {
            numberOfSessions = 3;
        }
        else {//trainer
            numberOfSessions = 24;
        }

        List<Session> sessions = new ArrayList<>();
        for(int i=0; i<numberOfSessions; ++i) {
            if(i< numberOfSessions/2) {
                Session session = getDummySession(participant, type, (i+1),
                        c.getTime(), program,
                        type + " notes for participant " + participant.getFirstName() + " on session # " + (i+1),
                        "Hi from an admin on session # " + (i+1) + " for participant " + participant.getFirstName());
                sessions.add(session);
            }
            else {
                Session session = getDummySession(participant, type, i+1, null, program, "", "");
                sessions.add(session);
            }
            c.add(Calendar.DATE, 1);
        }

        return sessions;
    }

    private Session getDummySession(Participant participant, SpecialistType specialistType,
                                    int indexNumber, Date logDate, Program program,
                                    String specNotes, String adminNotes) {
        Session session = new Session();
        session.setParticipant(participant);
        session.setWhoseNotes(specialistType);
        session.setSessionIndexNumber(indexNumber);
        session.setInitialLogDate(logDate);
        session.setLastUpdatedDate(logDate);
        session.setProgram(program);
        session.setSpecialistNotes(specNotes);
        session.setAdminNotes(adminNotes);

        return session;
    }
}