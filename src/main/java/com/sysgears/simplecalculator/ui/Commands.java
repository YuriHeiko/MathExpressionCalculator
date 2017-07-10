package com.sysgears.simplecalculator.ui;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains all the application commands.
 */
public enum Commands {
    /**
     * A command to end application work
     */
    EXIT("exit", "", "\t\t\t- to close the application"),

    /**
     * A command to show help
     */
    HELP("help", "You can use next commands:" + System.lineSeparator(), "\t\t\t- to get this description"),

    /**
     * A command to show the list of history events
     */
    HISTORY("history", "History:" + System.lineSeparator(), "\t\t\t- to see the calculation history"),

    /**
     * A command to show the list of history events without duplicates
     */
    HISTORY_UNIQUE("history unique", "Unique history:" + System.lineSeparator(),
            "\t- to see the calculation history without duplicates"),

    /**
     * A command to show the list of allowed math operators
     */
    OPERATORS("operators", "The operators list, sorted in ascending order by their precedence:" + System.lineSeparator(),
            "\t\t- to see the operators list"),

    /**
     * A command to show the list of allowed math operators
     */
    USER_FUNCTIONS("user functions", "The User's functions list:" + System.lineSeparator(),"\t- to see User's functions list"),

    /**
     * A command to show the list of allowed math operators
     */
    FUNCTIONS("functions", "The Math's functions list:" + System.lineSeparator(),"\t\t- to see Math's functions list"),

    /**
     * Additional information
     */
    ATTENTION(System.lineSeparator() + "ADDITIONAL INFO: ", "",
            "You must NOT use either '+-' or '-+' combinations in your expressions." + System.lineSeparator() +
                    "You should bear in mind that all the calculations can have some ROUND-OFF errors.");

    /**
     * A command string representation
     */
    private final String COMMAND;

    /**
     * A command header string representation
     */
    public final String HEADER;

    /**
     * A command description string representation
     */
    private final String DESCRIPTION;

    /**
     * Constructs an object
     *
     * @param command     The string representation of the command
     * @param header      The command header for UI
     * @param description The command description
     */
    Commands(final String command, final String header, final String description) {
        this.COMMAND = command;
        this.HEADER = header;
        this.DESCRIPTION = description;
    }

    /**
     * Builds and returns the string representation of the command list
     *
     * @return The string with the description of all the commands
     */
    public static String getHelp() {
        return Stream.of(values()).
                map(e -> "\t" + e.COMMAND + e.DESCRIPTION).
                collect(Collectors.joining(System.lineSeparator()));
    }

    public static boolean isCommand(final String line) {
        return Stream.of(values()).anyMatch(v -> v.COMMAND.equals(line));
    }
}