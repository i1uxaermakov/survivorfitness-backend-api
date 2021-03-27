package com.changeplusplus.survivorfitness.backendapi.application;

import com.changeplusplus.survivorfitness.backendapi.entity.Participant;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
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
    }
}