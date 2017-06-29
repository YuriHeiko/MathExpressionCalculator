package com.sysgears.simplecalculator.ui;

import java.io.IOException;

/**
 * Common UI controller interface
 */
public interface UIController {

    /**
     * Read the line
     *
     * @param promptString the prompt string
     * @return the incoming line
     * @throws IOException If an I/O error occurs
     */
    String readLine(String promptString) throws IOException;

    /**
     * Prints a line into a system console
     *
     * @param description  the line description
     * @param outputString the output string
     */
    void printLine(String description, String outputString);

    /**
     * Prints a line into a system console
     *
     * @param line the output string
     */
    void printLine(String line);

    /**
     * Closes open connections
     */
    void close() throws IOException;
}
