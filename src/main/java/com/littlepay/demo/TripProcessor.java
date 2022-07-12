package com.littlepay.demo;

import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class TripProcessor {
    private static String PROP_FILE = "config.properties";
    private final Map<String, Tap> recordMap;

    public TripProcessor() throws IOException {
        this(new HashMap<>());
    }

    public TripProcessor(Map<String, Tap> recordMap) throws IOException {
        this.recordMap = recordMap;
    }

    public static void main(String[] args) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties props = new Properties();

        try (InputStream resourceStream = loader.getResourceAsStream(PROP_FILE)) {
            props.load(resourceStream);
        }

        String tapsFile = props.getProperty("tapsFile");
        String tripsFile = props.getProperty("tripsFile");

        new TripProcessor().process(tapsFile, tripsFile);
        System.out.println("Trip file has been successfully created");
    }

    private void process(String tapsFile, String tripsFile) throws IOException {
        final CsvFileReader csvFileReader = new CsvFileReader(tapsFile);
        final CsvFileWriter csvFileWriter = new CsvFileWriter(tripsFile);
        try {
            while (csvFileReader.hasNext()) {
                Tap tap = csvFileReader.next();
                buildTrip(tap).ifPresent(trip -> {
                    try {
                        csvFileWriter.writeTripFile(trip);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } finally {
            IOUtils.closeQuietly(csvFileReader);
        }
        //Write all the Map records left into trips file
        try {
            this.recordMap.values().stream()
                    .map(tap -> new Trip(tap.getDateTime(),null,0L, tap.getStopId(), null, Utility.MaxChargeofIncompleteTrip(tap.getStopId()), tap.getCompanyId(), tap.getBusId(), tap.getPAN(), Trip.TripStatus.INCOMPLETE))
                    .forEach(trip -> {
                        try {
                            csvFileWriter.writeTripFile(trip);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } finally {
            IOUtils.closeQuietly(csvFileWriter);
        }
    }

    public Optional<Trip> buildTrip(Tap tap) {
        final Tap old = this.recordMap.get(tap.getPAN());
        //Type = ON
        if (Tap.TapType.ON.equals(tap.getTapType())) {
            this.recordMap.put(tap.getPAN(), tap);
            if (old == null) {
                return Optional.empty();
            }
            System.out.println(old.getPAN());
            return Optional.of(new Trip(old.getDateTime(), null, 0L,
                    old.getStopId(), null, Utility.MaxChargeofIncompleteTrip(old.getStopId()),
                    old.getCompanyId(), old.getBusId(), old.getPAN(), Trip.TripStatus.INCOMPLETE));
        } else {
            // Type = OFF
            if (old == null) {
                return Optional.of((new Trip(null, tap.getDateTime(), 0L,
                        null, tap.getStopId(), Utility.MaxChargeofIncompleteTrip(tap.getStopId()),
                        tap.getCompanyId(), tap.getBusId(), tap.getPAN(), Trip.TripStatus.INCOMPLETE)));
            }
            if (old.getBusId().equals(tap.getBusId())) {
                if (old.getStopId().equals(tap.getStopId())) {
                    recordMap.remove(old.getPAN());
                    return Optional.of((new Trip(old.getDateTime(), tap.getDateTime(), 0L,
                            old.getStopId(), old.getStopId(), 0.00,
                            old.getCompanyId(), old.getBusId(), old.getPAN(), Trip.TripStatus.CANCELLED)));

                } else {
                    recordMap.remove(old.getPAN());
                    return Optional.of((new Trip(old.getDateTime(), tap.getDateTime(), Utility.DurationSec(old.getDateTime(), tap.getDateTime()),
                            old.getStopId(), tap.getStopId(), Utility.ChargeofCompleteTrip(old.getStopId(), tap.getStopId()),
                            old.getCompanyId(), old.getBusId(), old.getPAN(), Trip.TripStatus.COMPLETED)));
                }
            }
        }
        return Optional.empty();
    }
}
