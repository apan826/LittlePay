package com.littlepay.demo;

import org.junit.Test;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.Assert.*;

public class TripProcessorTest {

    @Test
    public void returnEmptyTrip() throws Exception {
        final TripProcessor processor = new TripProcessor();
        Tap tap = new Tap("1", LocalDateTime.parse("2018-01-22T13:00:00"),  Tap.TapType.ON, "Stop1", "Company1", "Bus37", "5500005555555559");
        final Optional<Trip> trip = processor.buildTrip(tap);
        assertFalse(trip.isPresent());
    }

    @Test
    public void testCompletedTrip() throws Exception {
        Map<String, Tap> recordMap = new HashMap<>();
        Tap tap1 = new Tap("1", LocalDateTime.parse("2018-01-22T13:00:00"), Tap.TapType.ON, "Stop1", "Company1", "Bus37", "5500005555555559");
        recordMap.put("5500005555555559", tap1);
        final TripProcessor processor = new TripProcessor(recordMap);

        Tap tap2 = new Tap("2", LocalDateTime.parse("2018-01-22T13:06:00"), Tap.TapType.OFF, "Stop3", "Company1", "Bus37", "5500005555555559");
        Optional<Trip> optional = processor.buildTrip(tap2);
        assertTrue(optional.isPresent());
        Trip trip = optional.get();
        assertEquals(Trip.TripStatus.COMPLETED, trip.getStatus());
        assertEquals(360L, trip.getDuration().longValue());
    }

    @Test
    public void testIncompletedTrip() throws Exception {
        Map<String, Tap> recordMap = new HashMap<>();
        Tap tap1 = new Tap("1", LocalDateTime.parse("2018-01-22T13:00:00"), Tap.TapType.OFF, "Stop1", "Company1", "Bus37", "5500005555555559");
        //recordMap.put("5500005555555559", tap1);
        final TripProcessor processor = new TripProcessor(recordMap);

        Optional<Trip> optional = processor.buildTrip(tap1);
        assertTrue(optional.isPresent());
        Trip trip = optional.get();
        assertEquals(Trip.TripStatus.INCOMPLETE, trip.getStatus());
    }

    @Test
    public void testCancelledTrip() throws Exception {
        Map<String, Tap> recordMap = new HashMap<>();
        Tap tap1 = new Tap("1", LocalDateTime.parse("2018-01-22T13:00:00"), Tap.TapType.ON, "Stop1", "Company1", "Bus37", "5500005555555559");
        recordMap.put("5500005555555559", tap1);
        final TripProcessor processor = new TripProcessor(recordMap);

        Tap tap2 = new Tap("2", LocalDateTime.parse("2018-01-22T13:06:00"), Tap.TapType.OFF, "Stop1", "Company1", "Bus37", "5500005555555559");
        Optional<Trip> optional = processor.buildTrip(tap2);
        assertTrue(optional.isPresent());
        Trip trip = optional.get();
        assertEquals(Trip.TripStatus.CANCELLED, trip.getStatus());
        assertEquals(0L, trip.getDuration().longValue());
    }
}