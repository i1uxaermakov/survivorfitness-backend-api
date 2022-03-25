package com.changeplusplus.survivorfitness.backendapi.export;

import com.changeplusplus.survivorfitness.backendapi.entity.Measurement;
import com.changeplusplus.survivorfitness.backendapi.entity.Participant;
import com.changeplusplus.survivorfitness.backendapi.entity.Program;
import com.changeplusplus.survivorfitness.backendapi.entity.Session;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
import com.changeplusplus.survivorfitness.backendapi.repository.UserRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataExportRequestListener implements ApplicationListener<OnDataExportRequestedEvent> {

    @Autowired
    private ParticipantRepository participantRepository;

    //GoogleDriveFileUploadService

    @Override
    public void onApplicationEvent(OnDataExportRequestedEvent event) {
        doExportData(event.getEmailsToSendResultsTo());
    }

    private void doExportData(List<String> emailsToNotify) {
        List<Participant> participants = participantRepository.findAll();

        // Get all unique measurement names
        List<String> uniqueMeasurementNames = new ArrayList<>(getUniqueMeasurementNames(participants));

        // Create a header
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SFF Data Export");




        for(Participant p: participants) {
            Program program = p.getTreatmentProgram();
        }
    }


    private Collection<String> getUniqueMeasurementNames(List<Participant> participants) {
        // Convert a list of participants into a stream of participants
        return participants.stream()

                // Convert to a stream of Program objects
                .map(Participant::getTreatmentProgram)

                // Get Trainer sessions from each program. Convert to a stream of List<Session>
                .map(Program::getTrainerSessions)

                // Convert a stream of List<Session> into a stream of Session
                .flatMap(List::stream)

                //
                .map(Session::getMeasurements)
                .filter(measurements -> !measurements.isEmpty())
                .flatMap(List::stream)
                .map(Measurement::getName)
                .collect(Collectors.toSet());
    }


    private Row createHeaderRow(Sheet sheet, Participant typicalParticipant, List<String> uniqueMeasurementNames) {
        Row headerRow = sheet.createRow(0);
        int columnCount = 0;

        Cell cell = headerRow.createCell(columnCount);
        cell.setCellValue("Participant Full Name");
        columnCount++;

        return null;

    }
}
