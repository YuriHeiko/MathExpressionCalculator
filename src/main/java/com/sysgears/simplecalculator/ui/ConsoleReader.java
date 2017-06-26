package com.sysgears.simplecalculator.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader implements InputStringReader {

    public String read(String promptString) {
        String line = null;

        System.out.println(promptString);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            line = reader.readLine();
//            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();


        return line;
    }
}
