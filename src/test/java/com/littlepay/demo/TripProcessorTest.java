package com.littlepay.demo;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.Assert.*;

public class TripProcessorTest {

    private final Map<String, Tap> recordMap;
    //private DateTimeFormatter df;

    public TripProcessorTest() {
        this.recordMap = new HashMap<>();

    }

    @Test
    public void returnEmptyTrip() {
       // DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        Tap tap = new Tap("1", LocalDateTime.parse("2018-01-22 13:00:00"),  Tap.TapType.ON, "Stop1", "Conpany1", "Bus37", "5500005555555559");
        //this.recordMap.put("5500005555555559", tap);

        assertEquals(Optional.empty(), buildTrip(tap));
    }

    @Test
    public void testCompletedTrip() {

       // DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        Tap tap = new Tap("1", LocalDateTime.parse("2018-01-22 13:00:00"), Tap.TapType.ON, "Stop1", "Conpany1", "Bus37", "5500005555555559");
        this.recordMap.put("5500005555555559", tap);

        Tap tap1 = new Tap("2", LocalDateTime.parse("2018-01-22 13:06:00"), Tap.TapType.OFF, "Stop3", "Conpany1", "Bus37", "5500005555555559");
        //this.recordMap.put("5500005555555559", tap);

        Optional<Trip> trip = buildTrip(tap1);

        assertEquals(trip.get().getDuration(), "360s");
    }

    public Optional<Trip> buildTrip(Tap tap) {

        final Tap old = recordMap.get(tap.getPAN());
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
                    return Optional.of((new Trip(old.getDateTime(), tap.getDateTime(), 0L,
                            old.getStopId(), old.getStopId(), 0.00,
                            old.getCompanyId(), old.getBusId(), old.getPAN(), Trip.TripStatus.CANCELLED)));

                } else {
                    return Optional.of((new Trip(old.getDateTime(), tap.getDateTime(), Utility.DurationSec(old.getDateTime(), tap.getDateTime()),
                            old.getStopId(), tap.getStopId(), Utility.ChargeofCompleteTrip(old.getStopId(), tap.getStopId()),
                            old.getCompanyId(), old.getBusId(), old.getPAN(), Trip.TripStatus.COMPLETED)));
                }
            }
        }
        return Optional.empty();
    }
}