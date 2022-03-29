package com.changeplusplus.survivorfitness.backendapi.export;

import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
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


    /**
     *
     * @param emailsToNotify
     */
    private void doExportData(List<String> emailsToNotify) {
        List<Participant> participants = participantRepository.findAll();

        // Get all unique measurement names
        List<String> uniqueMeasurementNames = new ArrayList<>(getUniqueMeasurementNames(participants));

        // Create a header
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SFF Data Export");
        Row headerRow = createHeaderRow(sheet, uniqueMeasurementNames);

        participants.forEach(participant -> addRowAboutParticipant(sheet, participant));

        // export the workbook into a file
        // upload the file to google drive
        // send emails to emailsToNotify about the completed process
    }


    /**
     *
     * @param participants
     * @return
     */
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


    /**
     *
     * @param sheet
     * @param uniqueMeasurementNames
     * @return
     */
    private Row createHeaderRow(Sheet sheet, List<String> uniqueMeasurementNames) {
        Row headerRow = sheet.createRow(getNewRowIndexNumber(sheet));

        // Make all column names bold
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        CellStyle style = headerRow.getRowStyle();
        style.setFont(font);

        // Create cells about Participant
        addCellToRow(headerRow, "Participant Full Name");
        addCellToRow(headerRow, "Age");
        addCellToRow(headerRow, "Email");
        addCellToRow(headerRow, "Phone Number");
        addCellToRow(headerRow, "Start Date");
        addCellToRow(headerRow, "Goals");
        addCellToRow(headerRow, "Type of Cancer");
        addCellToRow(headerRow, "Forms of treatment");
        addCellToRow(headerRow, "Surgeries");
        addCellToRow(headerRow, "Physician notes");
        addCellToRow(headerRow, "Trainer Full Name");
        addCellToRow(headerRow, "Gym Name");
        addCellToRow(headerRow, "Dietitian Full Name");
        addCellToRow(headerRow, "Dietitian Office Name");

        // Add columns for trainer session notes
        for(int i=1; i<=24; ++i) {// TODO adjust the number of trainer sessions to use the max possible
            addCellToRow(headerRow, "Trainer Session " + i + " Specialist Notes");
            addCellToRow(headerRow, "Trainer Session " + i + " Admin Notes");
        }

        // Add columns for trainer session notes
        for(int i=1; i<=24; ++i) {// TODO adjust the number of dietitian sessions to use the max possible
            addCellToRow(headerRow, "Dietitian Session " + i + " Specialist Notes");
            addCellToRow(headerRow, "Dietitian Session " + i + " Admin Notes");
        }

        // Add columns for measurements
        for(String measurementName: uniqueMeasurementNames) {
            addCellToRow(headerRow, "Session " + 1 + " " + measurementName);
            addCellToRow(headerRow, "Session " + 12 + " " + measurementName);
            addCellToRow(headerRow, "Session " + 24 + " " + measurementName);
        }

        return headerRow;
    }


    /**
     *
     * @param sheet
     * @param participant
     * @return
     */
    private Row addRowAboutParticipant(Sheet sheet, Participant participant) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Row row = sheet.createRow(getNewRowIndexNumber(sheet));
        int columnCount = 0;

        // Retrieve information about the program and the specialists
        Program program = participant.getTreatmentProgram();
        User trainer = program.getTrainer();
        Location gym = program.getTrainerGym();
        User dietitian = program.getDietitian();
        Location dietitianOffice = program.getDietitianOffice();

        // Create cells about Participant
        addCellToRow(row, participant.getFullName());
        addCellToRow(row, participant.getAge().toString());
        addCellToRow(row, participant.getEmail());
        addCellToRow(row, participant.getPhoneNumber());
        addCellToRow(row, formatter.format(participant.getStartDate()));
        addCellToRow(row, participant.getGoals());
        addCellToRow(row, participant.getTypeOfCancer());
        addCellToRow(row, participant.getFormsOfTreatment());
        addCellToRow(row, participant.getSurgeries());
        addCellToRow(row, participant.getPhysicianNotes());
        addCellToRow(row, trainer.getFullName());
        addCellToRow(row, gym.getName());
        addCellToRow(row, dietitian.getFullName());
        addCellToRow(row, dietitianOffice.getName());

        // Go over trainer sessions and save session notes
        List<Session> trainerSessions = program.getTrainerSessions();
        for(Session session: trainerSessions) {
            addCellToRow(row, session.getSpecialistNotes());
            addCellToRow(row, session.getAdminNotes());
        }
        // TODO add empty cells if the number of sessions of this participant is lower than max

        // Go over dietitian sessions and save session notes
        List<Session> dietitianSessions = program.getDietitianSessions();
        for(Session session: dietitianSessions) {
            addCellToRow(row, session.getSpecialistNotes());
            addCellToRow(row, session.getAdminNotes());
        }
        // TODO add empty cells if the number of sessions of this participant is lower than max


        // Measurements

        return row;
    }


    /**
     *
     * @param row
     * @param value
     * @return
     */
    private int addCellToRow(Row row, String value) {
        int newCellIndex = getNewCellIndexNumber(row);

        Cell cell = row.createCell(newCellIndex);
        cell.setCellValue(value);

        return newCellIndex + 1;
    }


    /**
     *
     * @param row
     * @return
     */
    private int getNewCellIndexNumber(Row row) {
        int newCellIndex = row.getLastCellNum();
        if(newCellIndex == -1) {
            newCellIndex = 0;
        }
        else {
            newCellIndex -= 1;
        }
        return newCellIndex;
    }


    /**
     *
     * @param sheet
     * @return
     */
    private int getNewRowIndexNumber(Sheet sheet) {
        int newRowIndex = sheet.getLastRowNum();
        if(newRowIndex == -1) {
            newRowIndex = 0;
        }

        return newRowIndex;
    }
}
