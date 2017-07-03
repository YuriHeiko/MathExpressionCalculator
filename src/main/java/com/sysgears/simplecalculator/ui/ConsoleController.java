package com.sysgears.simplecalculator.ui;

import java.io.*;

/**
 * Uses a system console to get and show information.
 */
public class ConsoleController implements UIController, Closeable {
    
    /**
     * The information reader
     */
    private final BufferedReader reader;

    /**
     * The information writer
     */
    private final PrintStream writer; 

    /**
     * Constructs an object 
     * 
     * @param inputStream The {@code InputStream} realization
     * @param outputStream The {@code OutputStream} realization
     */
    public ConsoleController(InputStream inputStream, OutputStream outputStream) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.writer = new PrintStream(outputStream);
    }

    /**
     * Constructs an object. Uses the default system console for
     * input and output information.
     */
    public ConsoleController() {
        this(System.in, System.out);
    }

    /**
     * Reads the line from the console. Shows the prompt string.
     *
     * @param promptString The prompt string
     * @return The incoming line
     * @throws IOException If an I/O error occurs
     */
    @Override
    public String readLine(final String promptString) throws IOException {
        writer.println(promptString);

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
        writer.println("--------------------------------------------------------------------------------");
        writer.printf("%s%s%n%n", description, outputString);
    }

    /**
     * Prints a line into the system console
     *
     * @param line The output string
     */
    @Override
    public void printLine(final String line) {
        writer.println(line);
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