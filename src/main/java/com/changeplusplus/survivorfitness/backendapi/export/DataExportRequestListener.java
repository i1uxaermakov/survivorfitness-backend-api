package com.changeplusplus.survivorfitness.backendapi.export;

import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DataExportRequestListener implements ApplicationListener<OnDataExportRequestedEvent> {

    private final Logger logger = LoggerFactory.getLogger(DataExportRequestListener.class);

    @Autowired
    private ParticipantRepository participantRepository;

    //GoogleDriveFileUploadService

    private static final String BASE_DIR = "/Users/ilya_ermakov/sff-output";

    @Override
    @Transactional
    public void onApplicationEvent(OnDataExportRequestedEvent event) {
        logger.info("OnDataExportRequestedEvent received, begin processing.");
        try {
            doExportData(event.getEmailsToSendResultsTo());
        }
        catch (Exception e) {
            sendFailureMessage(e);
        }
        logger.info("Finished processing of data export.");
    }


    /**
     *
     * @param emailsToNotify
     */
    private void doExportData(List<String> emailsToNotify) throws Exception { //TODO change exception handling
        List<Participant> participants = participantRepository.findAll();

        // Get all unique measurement names
        List<String> uniqueMeasurementNames = new ArrayList<>(getUniqueMeasurementNames(participants));

        // Create a workbook, a sheet, and a default
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SFF Data Export");
        Row headerRow = createHeaderRow(sheet, uniqueMeasurementNames);



        for(Participant participant: participants) {
            addRowAboutParticipant(sheet, participant, uniqueMeasurementNames);
        }

        // export the workbook into a file
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
        String fileName = "DataExport_" + dateFormat.format(new Date());
        File outputFile = File.createTempFile(fileName, ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            workbook.write(fos);
        }

        //write to local file
        writeWorkbookIntoLocalFile(outputFile);

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
//        return participants.stream()
//
//                // Convert to a stream of Program objects
//                .map(Participant::getTreatmentProgram)
//
//                // Get Trainer sessions from each program. Convert to a stream of List<Session>
//                .map(Program::getTrainerSessions)
//
//                // Convert a stream of List<Session> into a stream of Session
//                .flatMap(List::stream)
//
//                //
//                .map(Session::getMeasurements)
//                .filter(measurements -> !measurements.isEmpty())
//                .flatMap(List::stream)
//                .map(Measurement::getName)
//                .collect(Collectors.toSet());

        Set<String> uniqueMeasurements = new HashSet<>();
        for(Participant participant: participants) {
            Program program = participant.getTreatmentProgram();
            List<Session> trainerSessions = program.getTrainerSessions();

            for(Session session: trainerSessions) {
                List<Measurement> measurements = session.getMeasurements();

                for(Measurement m: measurements) {
                    uniqueMeasurements.add(m.getName());
                }
            }
        }

        return uniqueMeasurements;
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
        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        // Create cells about Participant
        addCellToRow(headerRow, "Participant Full Name", headerStyle);
        addCellToRow(headerRow, "Age", headerStyle);
        addCellToRow(headerRow, "Email", headerStyle);
        addCellToRow(headerRow, "Phone Number", headerStyle);
        addCellToRow(headerRow, "Start Date", headerStyle);
        addCellToRow(headerRow, "Goals", headerStyle);
        addCellToRow(headerRow, "Type of Cancer", headerStyle);
        addCellToRow(headerRow, "Forms of treatment", headerStyle);
        addCellToRow(headerRow, "Surgeries", headerStyle);
        addCellToRow(headerRow, "Physician notes", headerStyle);
        addCellToRow(headerRow, "Trainer Full Name", headerStyle);
        addCellToRow(headerRow, "Gym Name", headerStyle);
        addCellToRow(headerRow, "Dietitian Full Name", headerStyle);
        addCellToRow(headerRow, "Dietitian Office Name", headerStyle);

        // Add columns for trainer session notes
        for(int i=1; i<=24; ++i) {// TODO adjust the number of trainer sessions to use the max possible
            addCellToRow(headerRow, "Trainer Session " + i + " Specialist Notes", headerStyle);
            addCellToRow(headerRow, "Trainer Session " + i + " Admin Notes", headerStyle);
        }

        // Add columns for trainer session notes
        for(int i=1; i<=24; ++i) {// TODO adjust the number of dietitian sessions to use the max possible
            addCellToRow(headerRow, "Dietitian Session " + i + " Specialist Notes", headerStyle);
            addCellToRow(headerRow, "Dietitian Session " + i + " Admin Notes", headerStyle);
        }

        // Add columns for measurements
        for(String measurementName: uniqueMeasurementNames) {
            addCellToRow(headerRow, "Session " + 1 + " " + measurementName, headerStyle);
            addCellToRow(headerRow, "Session " + 12 + " " + measurementName, headerStyle);
            addCellToRow(headerRow, "Session " + 24 + " " + measurementName, headerStyle);
        }

        return headerRow;
    }


    /**
     *
     * @param sheet
     * @param participant
     * @return
     */
    private Row addRowAboutParticipant(Sheet sheet, Participant participant, List<String> uniqueMeasurementNames) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
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


        // Adding measurements
        Session session1 = trainerSessions.get(0);
        Session session12 = trainerSessions.get(11);
        Session session24 = trainerSessions.get(23);

        for(String measurementName: uniqueMeasurementNames) {
            String valueSession1 = Optional.of(session1.getValueOfMeasurement(measurementName)).orElse("");
            String valueSession12 = Optional.of(session12.getValueOfMeasurement(measurementName)).orElse("");
            String valueSession24 = Optional.of(session24.getValueOfMeasurement(measurementName)).orElse("");

            addCellToRow(row, valueSession1);
            addCellToRow(row, valueSession12);
            addCellToRow(row, valueSession24);
        }

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




    private int addCellToRow(Row row, String value, CellStyle cellStyle) {
        int nextCellIndex = addCellToRow(row, value);
        Cell cell = row.getCell(row.getLastCellNum() - 1);
        cell.setCellStyle(cellStyle);

        return nextCellIndex;
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
            return 0;
        }
        else {
            return newRowIndex + 1;
        }


    }




    private void writeWorkbookIntoLocalFile(File file) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(BASE_DIR + File.separator + file.getName());
            FileInputStream fis = new FileInputStream(file)) {
            byte[] buf = new byte[1024];
            int hasRead = 0;
            while((hasRead = fis.read(buf)) > 0){
                fos.write(buf, 0, hasRead);
            }
        }
    }


    private void sendFailureMessage(Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
    }


    private CellStyle getDefaultCellStyle(Workbook workbook) {
        return null;
    }

    private CellStyle getHeaderCellStyle(Workbook workbook) {
        return null;
    }



}
