package com.sysgears.simplecalculator.ui;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Uses a system console to get and show information.
 */
public class ConsoleController implements UIController, Closeable {
    /**
     * The system console reader
     */
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Reads the line from the console. Shows the prompt string.
     *
     * @param promptString The prompt string
     * @return The incoming line
     * @throws IOException If an I/O error occurs
     */
    @Override
    public String readLine(final String promptString) throws IOException {
        System.out.println(promptString);

        return reader.readLine();
    }

    /**
     * Prints a line into the system console
     *
     * @param description  The line description
     * @param outputString The output string
     */
    @Override
    public void printLine(final String description, final String outputString) {
        System.out.println(new String(new byte[75]).replaceAll(".", "-"));
        System.out.printf("%s%s%n%n", description, outputString);
    }

    /**
     * Prints a line into the system console
     *
     * @param line The output string
     */
    @Override
    public void printLine(final String line) {
        System.out.println(line);
    }

    /**
     * Closes open connections
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void close() throws IOException {
        reader.close();
    }
}