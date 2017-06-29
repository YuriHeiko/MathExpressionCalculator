package com.sysgears.simplecalculator.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleController implements UIController {

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public String readLine(final String promptString) throws IOException {

        System.out.println(promptString);

        return reader.readLine();
    }

    @Override
    public void printLine(final String description, final String outputString) {
        System.out.println(new String(new byte[45]).replaceAll(".", "-"));
        System.out.printf("%s%s%n%n%n", description, outputString);
    }

    @Override
    public void printLine(final String line) {
        System.out.println(line);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
