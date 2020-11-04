package com.parkinglot12.domain;


public class StatusResponse {
    private int slotNumber;
    private String registrationNumber;
    private int driverAge;

    public StatusResponse(int slotNumber, String registrationNumber, int driverAge) {
        this.slotNumber = slotNumber;
        this.registrationNumber = registrationNumber;
        this.driverAge = driverAge;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public int getDriverAge() {
        return driverAge;
    }

    @Override
    public String toString() {
        return slotNumber + "           " + registrationNumber + "      " + driverAge;
    }

}
