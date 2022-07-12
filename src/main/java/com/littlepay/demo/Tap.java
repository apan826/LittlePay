package com.littlepay.demo;

import java.time.LocalDateTime;

public class Tap {

    private final String id;
    private final LocalDateTime dateTime;
    private final TapType tapType;
    private final String stopId;
    private final String companyId;
    private final String busId;
    private final String PAN;
    public static enum TapType {
        ON, OFF
    }

    public Tap(String id, LocalDateTime dateTime, TapType tapType, String stopId, String companyId, String busId, String PAN) {
        this.id = id;
        this.dateTime = dateTime;
        this.tapType = tapType;
        this.stopId = stopId;
        this.companyId = companyId;
        this.busId = busId;
        this.PAN = PAN;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public TapType getTapType() {
        return tapType;
    }

    public String getStopId() {
        return stopId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getBusId() {
        return busId;
    }

    public String getPAN() {
        return PAN;
    }

}
