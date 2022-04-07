package com.changeplusplus.survivorfitness.backendapi.export;

import com.changeplusplus.survivorfitness.backendapi.entity.*;
import com.changeplusplus.survivorfitness.backendapi.repository.ParticipantRepository;
import com.changeplusplus.survivorfitness.backendapi.service.EmailService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A class that listens to OnDataExportRequestedEvent events and handles them. These events are emitted in the
 * DataExportController class.
 */
@Component
public class DataExportRequestListener implements ApplicationListener<OnDataExportRequestedEvent> {

    private final Logger logger = LoggerFactory.getLogger(DataExportRequestListener.class);

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private EmailService emailService;


    /**
     * Handles the OnDataExportRequestedEvent. This method is called by the Spring
     * framework to process the event.
     * @param event The data structure that contains the information needed to
     *              process the event
     */
    @Override
    @Transactional
    public void onApplicationEvent(OnDataExportRequestedEvent event) {
        logger.info("OnDataExportRequestedEvent received, begin processing.");
        try {
            doExportData(event.getEmailsToSendResultsTo());
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            sendFailureMessage(e, event.getEmailsToSendResultsTo());
        }
        logger.info("Finished processing of data export.");
    }


    /**
     * Exports the data about participants into an Excel workbook and sends
     * it to @param emailsToNotify via email.
     * @param emailsToNotify A list of emails to send the export file to
     */
    private void doExportData(List<String> emailsToNotify) throws Exception {
        List<Participant> participants = participantRepository.findAll();

        // Get all unique measurement names
        List<String> uniqueMeasurementNames = new ArrayList<>(getUniqueMeasurementNames(participants));

        // Create a workbook, a sheet, and a default
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SFF Data Export");
        Row headerRow = createHeaderRow(sheet, uniqueMeasurementNames);

        // Export data about all participants into the workbook
        for(Participant participant: participants) {
            addRowAboutParticipant(sheet, participant, uniqueMeasurementNames);
        }

        // Export the workbook into a file
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
        String fileName = "DataExport_" + dateFormat.format(new Date());
        File outputFile = File.createTempFile(fileName, ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            workbook.write(fos);
        }

        // Send emails to emailsToNotify about the completed process with the file attached
        emailsToNotify.forEach(email -> emailService.sendEmailWithAttachment(
                email,
                "SFF - Data Export Completed",
                "Hello,\n\n" +
                        "The export of data about participants has been completed. " +
                        "Please find the generated spreadsheet attached to this email.\n\n" +
                        "Thanks,\n" +
                        "Survivor Fitness Team",
                outputFile));

        // Delete the file from RAM
        outputFile.delete();
    }


    /**
     * Finds the names of all measurements taken for all participants. The results
     * of this method are used to create the header row for the Excel file and then
     * retrieve measurement values by measurement name for each participant
     * @param participants All participants in the database
     * @return a collection of strings that are the measurement names
     */
    private Collection<String> getUniqueMeasurementNames(List<Participant> participants) {
        // Each Participant has a Program associated with them. Each Program maintains
        // a list of Sessions. Sessions, in turn, have a list of Measurements. We go over
        // all Measurements and add names to the uniqueMeasurements set to find out
        // unique Measurement names. Those unique names will be used to set up the
        // columns in the Excel sheet.
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
     * Creates the header row in the @param sheet.
     * @param sheet The sheet to add the header row to
     * @param uniqueMeasurementNames a list of measurement names that have been
     *                               taken at any point of time for any participant
     * @return a Row that is already in the @param sheet
     */
    private Row createHeaderRow(Sheet sheet, List<String> uniqueMeasurementNames) {
        Row headerRow = sheet.createRow(getNewRowIndexNumber(sheet));

        // Get the style for the cells in the header
        CellStyle headerStyle = getHeaderCellStyle(sheet.getWorkbook());

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
        for(int i=1; i<=3; ++i) {// TODO adjust the number of dietitian sessions to use the max possible
            addCellToRow(headerRow, "Dietitian Session " + i + " Specialist Notes", headerStyle);
            addCellToRow(headerRow, "Dietitian Session " + i + " Admin Notes", headerStyle);
        }

        // Add columns for measurements
        // For each measurement, we are adding the measurements taken on 1st, 12th,
        // and 24th sessions
        for(String measurementName: uniqueMeasurementNames) {
            addCellToRow(headerRow, "Session " + 1 + " " + measurementName, headerStyle);
            addCellToRow(headerRow, "Session " + 12 + " " + measurementName, headerStyle);
            addCellToRow(headerRow, "Session " + 24 + " " + measurementName, headerStyle);
        }

        return headerRow;
    }


    /**
     * Creates a row in the @param sheet and exports data about the participant into it.
     * @param sheet Sheet object where the row will be created
     * @param participant Participant whose info has to be exported
     * @param uniqueMeasurementNames measurements names for creating columns
     * @return the populated Row object that is already in the sheet
     */
    private Row addRowAboutParticipant(Sheet sheet, Participant participant, List<String> uniqueMeasurementNames) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Row row = sheet.createRow(getNewRowIndexNumber(sheet));
        int columnCount = 0;

        // Get the default style for the cells
        CellStyle style = getDefaultCellStyle(sheet.getWorkbook());

        // Retrieve information about the program and the specialists
        Program program = participant.getTreatmentProgram();
        User trainer = program.getTrainer();
        Location gym = program.getTrainerGym();
        User dietitian = program.getDietitian();
        Location dietitianOffice = program.getDietitianOffice();

        // Create cells about Participant
        addCellToRow(row, participant.getFullName(), style);
        addCellToRow(row, participant.getAge().toString(), style);
        addCellToRow(row, participant.getEmail(), style);
        addCellToRow(row, participant.getPhoneNumber(), style);
        addCellToRow(row, formatter.format(participant.getStartDate()), style);
        addCellToRow(row, participant.getGoals(), style);
        addCellToRow(row, participant.getTypeOfCancer(), style);
        addCellToRow(row, participant.getFormsOfTreatment(), style);
        addCellToRow(row, participant.getSurgeries(), style);
        addCellToRow(row, participant.getPhysicianNotes(), style);
        addCellToRow(row, (!Objects.isNull(trainer)) ? trainer.getFullName() : "", style);
        addCellToRow(row, (!Objects.isNull(gym)) ? gym.getName() : "", style);
        addCellToRow(row, (!Objects.isNull(dietitian)) ? dietitian.getFullName() : "", style);
        addCellToRow(row, (!Objects.isNull(dietitianOffice)) ? dietitianOffice.getName() : "", style);

        // Go over trainer sessions and save session notes
        List<Session> trainerSessions = program.getTrainerSessions();
        for(Session session: trainerSessions) {
            addCellToRow(row, session.getSpecialistNotes(), style);
            addCellToRow(row, session.getAdminNotes(), style);
        }
        // TODO add empty cells if the number of sessions of this participant is lower than max

        // Go over dietitian sessions and save session notes
        List<Session> dietitianSessions = program.getDietitianSessions();
        for(Session session: dietitianSessions) {
            addCellToRow(row, session.getSpecialistNotes(), style);
            addCellToRow(row, session.getAdminNotes(), style);
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

            addCellToRow(row, valueSession1, style);
            addCellToRow(row, valueSession12, style);
            addCellToRow(row, valueSession24, style);
        }

        return row;
    }


    /**
     * Adds a cell to the row and sets its value to @param value
     * @param row The row where new cell has to be created
     * @param value The value that has to be in the cell
     * @return index number of the NEXT cell
     */
    private int addCellToRow(Row row, String value) {
        int newCellIndex = getNewCellIndexNumber(row);

        Cell cell = row.createCell(newCellIndex);
        cell.setCellValue(value);

        return newCellIndex + 1;
    }


    /**
     * Adds a cell to the row and allows to set the style of the cell.
     * @param row the row where a new cell has to be created
     * @param value the value of the new cell
     * @param cellStyle the style of the new cell
     * @return index number of the NEXT cell
     */
    private int addCellToRow(Row row, String value, CellStyle cellStyle) {
        int nextCellIndex = addCellToRow(row, value);
        Cell cell = row.getCell(row.getLastCellNum() - 1);
        cell.setCellStyle(cellStyle);

        return nextCellIndex;
    }


    /**
     * Gets the index number for a new (right-most) cell in @param row.
     * @param row Row where new cell will be created
     * @return the index number of the new cell
     */
    private int getNewCellIndexNumber(Row row) {
        int newCellIndex = row.getLastCellNum();
        if(newCellIndex == -1) {
            newCellIndex = 0;
        }
        return newCellIndex;
    }


    /**
     * Gets the index number for a new row in @param sheet.
     * @param sheet Sheet where new row will be created
     * @return the index number of the new row
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


    /**
     * Writes the file into a local file at the @param path/filename.xlsx
     * @param path The path to where the new file has to be saved
     * @param file The file that has to be written locally
     * @throws IOException if there are any problems with the file or the path does not exist
     */
    private void writeWorkbookIntoLocalFile(String path, File file) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(path + File.separator + file.getName());
            FileInputStream fis = new FileInputStream(file)) {
            byte[] buf = new byte[1024];
            int hasRead = 0;
            while((hasRead = fis.read(buf)) > 0){
                fos.write(buf, 0, hasRead);
            }
        }
    }


    /**
     * Notifies people at @param emails about the failure that happened.
     * @param e The exception that has just occurred
     * @param emails The emails to notify about the error
     */
    private void sendFailureMessage(Exception e, List<String> emails) {
        // Send emails to emailsToNotify about the completed process with the file attached
        emails.forEach(email -> emailService.sendEmail(
                email,
                "SFF - Data Export Failed",
                "Hello,\n\n" +
                        "The export of data about participants has failed with the following message: " + e.getMessage() + "\n\n" +
                        "Thanks,\n" +
                        "Survivor Fitness Team"));
    }


    /**
     * Creates the default style for cells in the resulting Excel sheet
     * @param workbook The Excel workbook in which the style will have to be created
     * @return The new default style object
     */
    private CellStyle getDefaultCellStyle(Workbook workbook) {
        // Make text wrap if it doesn't fit into the column
        CellStyle defaultStyle = workbook.createCellStyle();
        defaultStyle.setWrapText(true);
        return defaultStyle;
    }


    /**
     * Creates a style for a header row (bold font)
     * @param workbook The workbook the style has to be created for
     * @return the new style object that can be applied to cells
     */
    private CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle headerStyle = getDefaultCellStyle(workbook);

        // Make text bold
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        return headerStyle;
    }
}
