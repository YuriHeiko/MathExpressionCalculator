package com.sysgears.simplecalculator.ui;

public final class Constants {
    public static final String COMMAND_EXIT = "exit";
    public static final String COMMAND_HELP = "help";
    public static final String COMMAND_HISTORY = "history";
    public static final String COMMAND_UNIQUE_HISTORY = "history unique";

    public static final String DESCRIPTION = "This application can solve simple math expressions according to " +
                                            "their math precedence";
    public static final String PROMPT_STRING = "Type an expression to calculate (or type 'help' for allowed commands):";
//    public static final String VALUE_STRING = "Calculated value is ";
    public static final String VALUE_STRING = "";
    public static final String ERROR = System.lineSeparator() + "\tPlease read the instructions carefully.";
    public static final String HELP_DESCRIPTION = System.lineSeparator() +
                    "\t" + COMMAND_HELP + " - to get this description" + System.lineSeparator() +
                    "\t" + COMMAND_EXIT + " - to close the application" + System.lineSeparator() +
                    "\t" + COMMAND_HISTORY + " - to see the calculation history" + System.lineSeparator() +
                    "\t" + COMMAND_UNIQUE_HISTORY + " - to see the calculation history without duplicates";
}
