package com.littlepay.demo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;

public class CsvFileWriter implements Closeable {
    private static final String[] header = new String[] {"Started","Finished","DurationSecs","FromStopId","ToStopId","ChargeAmount","CompanyId","BusID","PAN","Status"};
    private FileWriter tripsFile;
    private BufferedWriter tripsWriter;
    private CSVPrinter csvPrinter;

    public CsvFileWriter(String fileName) throws IOException {
        this.tripsFile = new FileWriter(fileName);
        this.tripsWriter = new BufferedWriter(tripsFile);
        this.csvPrinter = new CSVPrinter(tripsWriter, CSVFormat.DEFAULT.withHeader(header));
    }

    public void writeTripFile(Trip tripsRecord) throws IOException {
        try{
            csvPrinter.printRecord(
                    tripsRecord.getStarted(),
                    tripsRecord.getFinished(),
                    tripsRecord.getDuration() + "s",
                    tripsRecord.getFromStopId(),
                    tripsRecord.getToStopId(),
                    "$" + tripsRecord.getChargeAmount(),
                    tripsRecord.getCompanyId(),
                    tripsRecord.getBusId(),
                    tripsRecord.getPAN(),
                    tripsRecord.getStatus()
            );
            csvPrinter.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Errors in writing trips file, please check the data");
        }
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeQuietly(this.tripsWriter);
    }
}