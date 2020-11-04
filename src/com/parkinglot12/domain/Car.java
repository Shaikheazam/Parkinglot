package com.parkinglot12.domain;



public class Car implements Vehicle {

    private String registrationNumber;
    private int driverAge;


    public Car(String registrationNumber, int driverAge) {
        if(registrationNumber == null || driverAge == 0) {
            throw new IllegalArgumentException("Both registrationNumber & driver Age should not be 0");
        }
        this.registrationNumber = registrationNumber;
        this.driverAge = driverAge;
    }

    public int getDriverAge() {
        return this.driverAge;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

}
