package org.example;

import java.util.Date;

public class BookingRequest {
    private String bookingUUID;
    private String userUUID;
    private MachineData machine;
    private Date requestTime;
    private Date processedOn;

    public BookingRequest(String bookingUUID, String userUUID, MachineData machine, Date requestTime) {
        this.bookingUUID = bookingUUID;
        this.userUUID = userUUID;
        this.machine = machine;
        this.requestTime = requestTime; 
    }

    public String getBookingUUID() {
        return bookingUUID;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public MachineData getMachine() {
        return machine;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public Date getProcessedOn() {
        return processedOn;
    }

    public void setProcessedOn() {
        this.processedOn = new Date();
    }
}
