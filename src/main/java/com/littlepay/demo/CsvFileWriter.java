package com.littlepay.demo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.IOUtils;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CsvFileWriter implements Closeable {
    private static final String[] header = new String[] {"Started","Finished","DurationSecs","FromStopId","ToStopId","ChargeAmount","CompanyId","BusID","PAN","Status"};
    private final BufferedWriter tripsWriter;
    private final CSVPrinter csvPrinter;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public CsvFileWriter(String fileName) throws IOException {
        this.tripsWriter = new BufferedWriter(new FileWriter(fileName));
        this.csvPrinter = new CSVPrinter(tripsWriter, CSVFormat.DEFAULT.withHeader(header));
    }

    public void writeTripFile(Trip tripsRecord) throws IOException {
        csvPrinter.printRecord(
                Optional.ofNullable(tripsRecord.getStarted()).map(s -> dtf.format(s)).orElse("NA"),
                Optional.ofNullable(tripsRecord.getFinished()).map(s -> dtf.format(s)).orElse("NA"),
                tripsRecord.getDuration() + "s",
                Optional.ofNullable(tripsRecord.getFromStopId()).orElse("NA"),
                Optional.ofNullable(tripsRecord.getToStopId()).orElse("NA"),
                "$" + tripsRecord.getChargeAmount(),
                tripsRecord.getCompanyId(),
                tripsRecord.getBusId(),
                tripsRecord.getPAN(),
                tripsRecord.getStatus()
        );
    }

    @Override
    public void close() throws IOException {
        this.csvPrinter.flush();
        IOUtils.closeQuietly(this.csvPrinter);
        IOUtils.closeQuietly(this.tripsWriter);
    }
}