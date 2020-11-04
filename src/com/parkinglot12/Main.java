
package com.parkinglot12;

import java.io.*;

import com.parkinglot12.service.CommandExecutor;

public class Main {
    public static void main(String[] args) throws Exception {

        CommandExecutor commandExecutor = CommandExecutor.getInstance();

        File file = new File("./resource/input.txt");
        FileWriter outpt = new FileWriter("output.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            outpt.write(String.valueOf(commandExecutor.execute(st)));
            outpt.write("\n");

        }
        outpt.close();


    }

}

