package org.example;

public class KafkaBookingRequest {
    private String machineBlock;
    private String machineName;
    private String bookingUUID;

    public KafkaBookingRequest(String bookingUUID, String machineBlock, String machineName) {
        this.machineBlock = machineBlock;
        this.machineName = machineName;
        this.bookingUUID = bookingUUID;
    }

    public String getBlock() {
        return machineBlock;
    }

    public String getName() {
        return machineName;
    }

    public String getBookingUUID() {
        return bookingUUID;
    }
}
