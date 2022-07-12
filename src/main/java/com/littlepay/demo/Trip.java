package com.littlepay.demo;

import java.time.LocalDateTime;

public class Trip {
    private final LocalDateTime started;
    private final LocalDateTime finished;
    private final Long duration;
    private final String fromStopId;
    private final String toStopId;
    private final Double chargeAmount;
    private final String companyId;
    private final String busId;
    private final String PAN;
    private final TripStatus status;
    public static enum TripStatus {
        COMPLETED, INCOMPLETE, CANCELLED
    }

    public Trip(LocalDateTime started, LocalDateTime finished, Long duration, String fromStopId, String toStopId, Double chargeAmount, String companyId, String busId, String PAN, TripStatus status) {
        this.started = started;
        this.finished = finished;
        this.duration = duration;
        this.fromStopId = fromStopId;
        this.toStopId = toStopId;
        this.chargeAmount = chargeAmount;
        this.companyId = companyId;
        this.busId = busId;
        this.PAN = PAN;
        this.status = status;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public LocalDateTime getFinished() {
        return finished;
    }

    public Long getDuration() {
        return duration;
    }

    public String getFromStopId() {
        return fromStopId;
    }

    public String getToStopId() {
        return toStopId;
    }

    public Double getChargeAmount() {
        return chargeAmount;
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

    public TripStatus getStatus() {
        return status;
    }

}
