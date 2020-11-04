package com.parkinglot12.service;

import java.util.HashMap;
import java.util.Map;

import com.parkinglot12.service.exceptions.ParkingLotException;

class ParkingLot {
    private static ParkingLot parkingLot;
    private Map<Integer, Slot> slots;

    /**
     * VisibleForTesting(otherwise = PRIVATE)
     */
    protected ParkingLot(int numberOfSlots) {
        slots = new HashMap<Integer, Slot>();
        for (int i = 1; i <= numberOfSlots; i++) {
            slots.put(i, new Slot(i));
        }
    }

    /**
     * Singleton Class => Returns a single instance of the class
     *
     * @param numberOfSlots => number of slots in the parking lot
     * @return ParkingLot instance
     */
    static ParkingLot getInstance(int numberOfSlots) {
        if (parkingLot == null) {
            parkingLot = new ParkingLot(numberOfSlots);
        }
        return parkingLot;
    }

    /**
     * Finds the next available slot and marks it unavailable
     *
     * @return slot number which was marked unavailable
     */
    int fillAvailableSlot() {
        int nextAvailableSlotNumber = -1;
        for (int i = 1; i <= slots.size(); i++) {
            Slot s = slots.get(i);
            if (s.status) {
                nextAvailableSlotNumber = s.slotNumber;
                s.status = false;
                break;
            }
        }
        if (nextAvailableSlotNumber != -1) {
            return nextAvailableSlotNumber;
        } else {
            throw new ParkingLotException("Sorry, parking lot is full");
        }
    }

    /**
     * Empties the Slot => marks the slot available
     *
     * @param slotNumber => the slot number to be made empty
     */
    void emptySlot(int slotNumber) {
        if (slots.containsKey(slotNumber)) {
            if (slots.get(slotNumber).status) {
                throw new IllegalStateException("The slot is already empty");
            } else {
                slots.get(slotNumber).status = true;
            }
        } else {
            throw new IllegalStateException("The slot number is invalid");
        }
    }

    /**
     * private Class => Slot is an entity known only to parking lot.
     *
     */
    private class Slot {
        // unique slot identifier
        private int slotNumber;
        // boolean status to maintain isAvailable => true=available, false=not available
        private boolean status;

        Slot(int slotNumber) {
            this.slotNumber = slotNumber;
            this.status = true;
        }
    }
}