package com.sysgears.simplecalculator.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleReader implements InputStringReader {

    public String read(String promptString) {
        String line = null;

        System.out.println(promptString);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){

            line = reader.readLine();
            System.out.println();

        } catch(Exception e){
            e.printStackTrace();
        }

        return line;
    }
}
