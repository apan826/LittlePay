package com.littlepay.demo;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilityTest {

    @Test
    public void maxChargeofStop2is55() {
        double maxCharge = Utility.MaxChargeofIncompleteTrip("Stop2");
        assertEquals(5.5, maxCharge, 0.0);
    }

    @Test
    public void maxChargeofStop3is73() {
        double maxCharge = Utility.MaxChargeofIncompleteTrip("Stop3");
        assertEquals(7.3, maxCharge,0.0);
    }

    @Test
    public void maxChargeofStop1is73() {
        double maxCharge = Utility.MaxChargeofIncompleteTrip("Stop1");
        assertEquals(7.3, maxCharge,0.0);
    }

    @Test
    public void ChargeofStop3toStop1is73() {
        double completeTripCharge = Utility.ChargeofCompleteTrip("Stop3","Stop1");
        assertEquals(7.3, completeTripCharge);
    }

    @Test
    public void ChargeofStop2toStop1is325() {
        double completeTripCharge = Utility.ChargeofCompleteTrip("Stop2","Stop1");
        assertEquals(3.25, completeTripCharge,0.0);
    }
}