package com.sysgears.simplecalculator.ui;

import java.io.Closeable;
import java.io.IOException;

/**
 * Common UI controller interface
 */
public interface UIController extends Closeable {

    /**
     * Reads the line
     *
     * @param promptString The prompt string
     * @return the incoming line
     * @throws IOException If an I/O error occurs
     */
    String readLine(String promptString) throws IOException;

    /**
     * Prints a line
     *
     * @param description  The line description
     * @param outputString The output string
     */
    void printLine(String description, String outputString);

    /**
     * Prints a line
     *
     * @param line The output string
     */
    void printLine(String line);

    /**
     * Closes open connections
     *
     * @throws IOException If an I/O error occurs
     */
    void close() throws IOException;
}
