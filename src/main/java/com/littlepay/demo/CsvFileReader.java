package com.littlepay.demo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class CsvFileReader implements Iterator<Tap>, Closeable {

    private static final String COL_ID = "ID";
    private static final String COL_DATETIME = "DateTimeUTC";
    private static final String COL_PAN = "PAN";
    private static final String COL_BUS_ID = "BusID";
    private static final String COL_STOP_ID = "StopId";
    private static final String COL_COMPANY_ID = "CompanyId";
    private static final String COL_TAP_TYPE = "TapType";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final CSVParser csvParser;
    private final Reader reader;
    private final Iterator<CSVRecord> iterator;

    public CsvFileReader(String fileName) throws IOException {
        this.reader = new BufferedReader(new FileReader(fileName));
        this.csvParser = new CSVParser(reader,
                CSVFormat.DEFAULT.withHeader(new String[]{COL_ID, COL_DATETIME, COL_TAP_TYPE, COL_STOP_ID, COL_COMPANY_ID, COL_BUS_ID, COL_PAN})
                .withIgnoreEmptyLines()
                .withSkipHeaderRecord(true)
                .withTrim()
                .withDelimiter(','));
        this.iterator = this.csvParser.iterator();
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public Tap next() {
        final CSVRecord record = this.iterator.next();
        return new Tap(record.get(COL_ID),
                LocalDateTime.parse(record.get(COL_DATETIME), this.formatter),
                Tap.TapType.valueOf(record.get(COL_TAP_TYPE)),
                record.get(COL_STOP_ID),
                record.get(COL_COMPANY_ID),
                record.get(COL_BUS_ID),
                record.get(COL_PAN));
    }

    @Override
    public void close() {
        IOUtils.closeQuietly(this.reader);
        IOUtils.closeQuietly(this.csvParser);
    }
}