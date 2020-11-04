
package com.parkinglot12.service;

import java.util.*;
import java.io.*;


import com.parkinglot12.domain.StatusResponse;
import com.parkinglot12.domain.Vehicle;
import com.parkinglot12.service.exceptions.ParkingLotException;


class TicketingSystem {
    private static TicketingSystem ticketingSystem;
    private ParkingLot parkingLot;
    private Map<Integer, Ticket> tickets;



    /**
     * VisibleForTesting(otherwise = PRIVATE)
     */
    TicketingSystem(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
        tickets = new HashMap<Integer, Ticket>();
    }

    /**
     * Singleton Class => Returns a single instance of the class
     *
     * @param numberOfSlots => Number of slots of the parking lot that this
     *                      ticketing system is managing
     * @return TicketingSystem instance
     */
    static TicketingSystem createInstance(int numberOfSlots) {
        if(numberOfSlots < 1) {
            throw new ParkingLotException("Number of slots cannot be less than 1");
        }
        if (ticketingSystem == null) {
            ParkingLot parkingLot = ParkingLot.getInstance(numberOfSlots);
            ticketingSystem = new TicketingSystem(parkingLot);
        }
        return ticketingSystem;
    }

    /**
     *
     * @return TicketingSystem instance
     */
    static TicketingSystem getInstance() {
        if(ticketingSystem == null) {
            throw new IllegalStateException("Parking Lot is not initialized");
        }
        return ticketingSystem;
    }

    /**
     * Parks a vehicle
     *
     * @return slotNumber => slot number at which the vehicle needs to be parked
     */
    int issueParkingTicket(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        int assignedSlotNumber = parkingLot.fillAvailableSlot();
        Ticket ticket = new Ticket(assignedSlotNumber, vehicle);
        tickets.put(assignedSlotNumber, ticket);
        return assignedSlotNumber;
    }

    /**
     * Exits a vehicle from the parking lot
     *
     * @param registrationNumber
     * @return slotNumber => the slot from the car has exited.
     */
    void exitVehicle(int slotNumber) {
        if (tickets.containsKey(slotNumber)) {
            parkingLot.emptySlot(slotNumber);
            tickets.remove(slotNumber);
            return;
        } else {
            throw new ParkingLotException("No vehicle found at given slot. Incorrect input");
        }
    }

    /**
     * returns all the registration numbers of the vehicles with the given driverAge
     *
     * @param driverAge => age of the driver of the Vehicle
     * @return List of all the registration numbers of the vehicles with the given
     *         color
     */
    List<String> getRegistrationNumbersFromDriverAge(int driverAge) {
        if (driverAge == 0) {
            throw new IllegalArgumentException("driver Age cannot be null");
        }
        List<String> registrationNumbers = new ArrayList<String>();
        for (Ticket ticket : tickets.values()) {
            if (Objects.equals(driverAge, ticket.vehicle.getDriverAge())) {
                registrationNumbers.add(ticket.vehicle.getRegistrationNumber());
            }
        }
        return registrationNumbers;
    }

    /**
     * for checking that the given ragistration number of the car is already there in the parkinglot. so given input is invalid. two cars can't have same ragistration number.
     * @param registration_number
     * @return
     */
    boolean ragistrationNumberAvailable(String registration_number){
        String string1 = new String(registration_number);

        for(Ticket ticket:tickets.values()){
            String string2 = new String(ticket.vehicle.getRegistrationNumber());
            if(string1.equals(string2))
                return true;
        }
        return false;
    }

    /**
     * returns the slot number at which the Vehicle with given registrationNumber is
     * parked
     *
     * @param registrationNumber => Registration Number of the Vehicle
     * @return slot number at which the Vehicle with given registrationNumber is
     *         parked
     */
    int getSlotNumberFromRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null) {
            throw new IllegalArgumentException("registrationNumber cannot be null");
        }
        for (Ticket ticket : tickets.values()) {
            if (registrationNumber.equals(ticket.vehicle.getRegistrationNumber())) {
                return ticket.slotNumber;
            }
        }

        throw new ParkingLotException("Not found");
    }

    /**
     * returns all the slot numbers of the vehicles with the given color
     *
     * @param driverAge => age of the driver of the Vehicle
     * @return List of all the slot numbers of the vehicles with the given color
     */
    List<Integer> getSlotNumbersFromDriverAge(int driverAge) {
        if (driverAge == 0) {
            throw new IllegalArgumentException("Driver Age cannot be 0");
        }
        List<Integer> registrationNumbers = new ArrayList<Integer>();
        for (Ticket ticket : tickets.values()) {
            if (Objects.equals(driverAge, ticket.vehicle.getDriverAge())) {
                registrationNumbers.add(ticket.slotNumber);
            }
        }
        return registrationNumbers;
    }


    /**
     * returns the registration number of a car on a particular slot number
     * @param slotNumber
     * @return
     */

    String getVehicleRegistrationFromSlotNumber(int slotNumber){
        if(tickets.containsKey(slotNumber)){
            return tickets.get(slotNumber).vehicle.getRegistrationNumber();

        }
        else
            throw new ParkingLotException("Car is not present at this slot number");

    }

    /**
     *  return the age of a driver of the car present on a particular slot.
     * @param slotNumber
     * @return
     */

    int getDriverAge(int slotNumber){

        if(tickets.containsKey(slotNumber)){
            return tickets.get(slotNumber).vehicle.getDriverAge();
        }
        else
            throw new ParkingLotException("Car is not present at this slot number");
    }




    /**
     * returns the status of the ticketing system, a list of all the tickets
     * converted to status objects
     *
     * @return List of StatusResponse => List of (slotNumber, registrationNumber,
     *         driverAge)
     */
    List<StatusResponse> getStatus() {
        List<StatusResponse> statusResponseList = new ArrayList<StatusResponse>();
        for (Ticket ticket : tickets.values()) {
            statusResponseList.add(new StatusResponse(ticket.slotNumber, ticket.vehicle.getRegistrationNumber(),
                    ticket.vehicle.getDriverAge()));
        }
        return statusResponseList;
    }



    /**
     * Ticketing System issues a ticket => an object known only to Ticketing System
     *
     */
    private class Ticket {
        int slotNumber;
        Vehicle vehicle;

        Ticket(int slotNumber, Vehicle vehicle) {
            this.slotNumber = slotNumber;
            this.vehicle = vehicle;
        }
    }
}
