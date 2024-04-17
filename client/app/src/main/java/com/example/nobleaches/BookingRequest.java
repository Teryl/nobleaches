package com.example.nobleaches;

import java.util.Date;
import java.util.UUID;

import com.example.nobleaches.GlobalConfig;

public class BookingRequest {
    private String bookingUUID;
    private String userUUID;
    private MachineData machine;
    private Date requestTime;

    BookingRequest(MachineData machine) {
        this.bookingUUID = UUID.randomUUID().toString();
        this.userUUID = GlobalConfig.getInstance().currentUser.getUid();
        this.machine = machine;
        this.requestTime = new Date();
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
}
