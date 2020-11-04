package com.parkinglot12.service;

import java.util.List;

import com.parkinglot12.domain.Car;
import com.parkinglot12.domain.StatusResponse;
import com.parkinglot12.service.exceptions.ParkingLotException;
import java.io.*;
public class CommandExecutor {
    private static CommandExecutor commandExecutor;

    private CommandExecutor() {

    }

    /**
     * Singleton Class => Returns a single instance of the class
     *
     * @return CommandExecutor instance
     */
    public static CommandExecutor getInstance() {
        if (commandExecutor == null) {
            commandExecutor = new CommandExecutor();
        }
        return commandExecutor;
    }

    private CommandName getCommandName(String commandString) {

        CommandName commandName = null;

        if (commandString == null) {
            System.out.println("Not a valid input");
        } else {
            String[] commandStringArray = commandString.split(" ");
            if ("".equals(commandStringArray[0])) {
                System.out.println("Not a valid input");
            } else {
                try {
                    commandName = CommandName.valueOf(commandStringArray[0]);
                } catch (Exception e) {
                    System.out.println("Unknown Command");
                }
            }
        }
        return commandName;

    }

    /**
     * the main function to execute the commands
     * @param commandString
     * @return boolean if the execution is success or not
     */
    public String execute(String commandString) {

        CommandName commandName = getCommandName(commandString);

        if (commandName == null) {
            return  "Not a valid Command";
        }
        String[] commandStringArray = commandString.split(" ");
        Command command;

        switch (commandName) {
            case Create_parking_lot:
                command = new CreateParkingLotCommand(commandStringArray);
                break;
            case Park:
                command = new ParkCommand(commandStringArray);
                break;
            case Leave:
                command = new LeaveCommand(commandStringArray);
                break;
            case Vehicle_registration_number_for_driver_of_age:
                command = new RegistrationNumbersForDriverAgeCommand(commandStringArray);
                break;
            case Slot_numbers_for_driver_of_age:
                command = new SlotNumbersForDriverAgeCommand(commandStringArray);
                break;
            case Slot_number_for_car_with_number:
                command = new SlotNumberCommand(commandStringArray);
                break;
            default:
                System.out.println("Unknown Command");
                return "Not a valid Command";
        }

        try {
            command.validate();
        } catch (IllegalArgumentException e) {
            System.out.println("Please provide a valid argument");
            return "Not a valid Command";
        }

        String output = "";
        try {
            output = command.execute();
        } catch (ParkingLotException e) {
//            System.out.print(e.getMessage());
            return e.getMessage();
        } catch(Exception e) {
            System.out.println("Unknown System Issue");
            e.printStackTrace();
            return "Not a valid Command";
        }

        return output;
    }

    /**
     * All CommandNames
     *
     */
    private enum CommandName {
        Create_parking_lot, Park, Leave, Vehicle_registration_number_for_driver_of_age,
        Slot_numbers_for_driver_of_age, Slot_number_for_car_with_number
    }

    /**
     * Command Interface which validates & executes the command
     *
     */
    private interface Command {
        public void validate();

        public String execute();
    }

    /**
     * Command Implementing create_parking_lot
     *
     */
    private class CreateParkingLotCommand implements Command {
        private String[] commandStringArray;

        CreateParkingLotCommand(String[] s) {
            commandStringArray = s;
        }

        public void validate() {
            if (commandStringArray.length != 2) {
                throw new IllegalArgumentException("create_parking_lot command should have exactly 1 argument");
            }
        }

        public String execute() {
            int numberOfSlots = Integer.parseInt(commandStringArray[1]);
            TicketingSystem.createInstance(numberOfSlots);
            return "Created parking of " + commandStringArray[1] + " slots";
        }
    }

    /**
     * holds the responsibility of implementing park command
     *
     */
    private class ParkCommand implements Command {

        private String[] commandStringArray;

        ParkCommand(String[] s) {
            commandStringArray = s;
        }

        public void validate() {
            if (commandStringArray.length != 4) {
                throw new IllegalArgumentException("park command should have exactly 4 arguments");
            }
        }

        public String execute() {
            TicketingSystem ticketingSystem = TicketingSystem.getInstance();
            /**
             * checking whether we are giving two car the same ragistration number.
             */
            boolean carPresentAlready=ticketingSystem.ragistrationNumberAvailable(commandStringArray[1]);
            if(carPresentAlready){
                throw new IllegalArgumentException("Two car can't have same ragistration number");
            }
            else {
                int allocatedSlotNumber = ticketingSystem
                        .issueParkingTicket(new Car(commandStringArray[1], Integer.parseInt(commandStringArray[3])));
                return "Car with vehicle registration number \"" + commandStringArray[1] + "\" has been parked at slot number " + allocatedSlotNumber;
            }
        }
    }

    /**
     * holds the responsibility of implementing leave command
     *
     */
    private class LeaveCommand implements Command {
        private String[] commandStringArray;

        LeaveCommand(String[] s) {
            commandStringArray = s;
        }

        public void validate() {
            if (commandStringArray.length != 2) {
                throw new IllegalArgumentException("leave command should have exactly 1 argument");
            }
        }

        public String execute() {
            TicketingSystem ticketingSystem = TicketingSystem.getInstance();
            String car_registration_number=ticketingSystem.getVehicleRegistrationFromSlotNumber(Integer.parseInt(commandStringArray[1]));
            int driver_age=ticketingSystem.getDriverAge(Integer.parseInt(commandStringArray[1]));
            ticketingSystem.exitVehicle(Integer.parseInt(commandStringArray[1]));
            return "Slot number " + commandStringArray[1] + " vacated, the car with vehicle registration number \""+ car_registration_number + "\" left the space, the driver of the car was of age "+driver_age;
        }
    }

    /**
     * holds the responsibility of implementing
     * registration_numbers_for_cars_with_colour command
     *
     */
    private class RegistrationNumbersForDriverAgeCommand implements Command {
        private String[] commandStringArray;

        RegistrationNumbersForDriverAgeCommand(String[] s) {
            commandStringArray = s;
        }

        public void validate() {
            if (commandStringArray.length != 2) {
                throw new IllegalArgumentException(
                        "registration_numbers_for_cars_with_colour command should have exactly 1 argument");
            }
        }

        public String execute() {
            TicketingSystem ticketingSystem = TicketingSystem.getInstance();
            List<String> registrationNumbersList = ticketingSystem
                    .getRegistrationNumbersFromDriverAge(Integer.parseInt(commandStringArray[1]));
            StringBuilder outputStringBuilder = new StringBuilder();
            for (String registrationNumber : registrationNumbersList) {
                if (outputStringBuilder.length() > 0) {
                    outputStringBuilder.append(", ");
                }
                outputStringBuilder.append(registrationNumber);
            }
            if(outputStringBuilder.toString().length()==0)
                return "null";
            return outputStringBuilder.toString();
        }
    }

    /**
     * holds the responsibility of implementing slot_numbers_for_cars_with_colour
     * command
     *
     */
    private class SlotNumbersForDriverAgeCommand implements Command {
        private String[] commandStringArray;

        SlotNumbersForDriverAgeCommand(String[] s) {
            commandStringArray = s;
        }

        public void validate() {
            if (commandStringArray.length != 2) {
                throw new IllegalArgumentException(
                        "slot_numbers_for_cars_with_colour command should have exactly 1 argument");
            }
        }

        public String execute() {
            TicketingSystem ticketingSystem = TicketingSystem.getInstance();
            List<Integer> slotNumbersList = ticketingSystem.getSlotNumbersFromDriverAge(Integer.parseInt(commandStringArray[1]));
            StringBuilder outputStringBuilder = new StringBuilder();
            for (int slotNumber : slotNumbersList) {
                if (outputStringBuilder.length() > 0) {
                    outputStringBuilder.append(", ");
                }
                outputStringBuilder.append(slotNumber);
            }
            return outputStringBuilder.toString();
        }
    }

    /**
     * holds the responsibility of implementing slot_number_for_registration_number
     * command
     *
     */
    private class SlotNumberCommand implements Command {
        private String[] commandStringArray;

        SlotNumberCommand(String[] s) {
            commandStringArray = s;
        }

        public void validate() {
            if (commandStringArray.length != 2) {
                throw new IllegalArgumentException(
                        "slot_number_for_registration_number command should have exactly 1 argument");
            }
        }

        public String execute() {
            TicketingSystem ticketingSystem = TicketingSystem.getInstance();
            int slotNumber = ticketingSystem.getSlotNumberFromRegistrationNumber(commandStringArray[1]);
            return "" + slotNumber;
        }
    }

}
