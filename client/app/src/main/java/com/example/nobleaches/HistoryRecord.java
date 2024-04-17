package com.example.nobleaches;

import java.util.Date;

public class HistoryRecord {
    private String bookingUUID;
    private String userUUID;
    private MachineData machine;
    private Date requestTime;
    private Date processedOn;

    HistoryRecord () {
    }

    public String getBookingUUID() {
        return bookingUUID;
    }

    public void setBookingUUID(String bookingUUID) {
        this.bookingUUID = bookingUUID;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public MachineData getMachine() {
        return machine;
    }

    public void setMachine(MachineData machine) {
        this.machine = machine;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getProcessedOn() {
        return processedOn;
    }

    public void setProcessedOn(Date processedOn) {
        this.processedOn = processedOn;
    }
}
