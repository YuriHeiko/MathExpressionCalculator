package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;
import com.sysgears.simplecalculator.history.HistoryHolder;
import com.sysgears.simplecalculator.ui.Constants;
import com.sysgears.simplecalculator.ui.UIController;

/**
 * Uses {@code UIController} to lead dialog with a user, {@code Computer}
 * to calculate user's math expression and {@code HistoryHolder} to keep
 * and show history
 */
public class Calculator {
    private final UIController controller;
    private final HistoryHolder history;
    private final Computer computer;

    /**
     * Constructs an object
     *
     * @param controller the UI controller
     * @param history    the history holder
     * @param computer   the computer
     */
    public Calculator(final UIController controller, final HistoryHolder history, final Computer computer) {
        this.controller = controller;
        this.history = history;
        this.computer = computer;
    }

    /**
     * Uses the controller to lead dialog with a user.
     */
    public void run() {
        String result = "";

        try {

/*
            Properties prop = new Properties();
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("SimpleCalculator.properties"));
*/

            controller.printLine(Constants.DESCRIPTION);

            // TODO add a possible operators option
            while (true) {
                String line = controller.readLine(Constants.PROMPT_STRING);

                switch (line.toLowerCase()) {
                    case Constants.COMMAND_EXIT:
                        controller.printLine(System.lineSeparator() + "Good bye!");
                        return;

                    case Constants.COMMAND_HELP:
                        controller.printLine("You can use next commands:", Constants.HELP_DESCRIPTION);
                        break;

                    case Constants.COMMAND_HISTORY:
                        controller.printLine("History:" + System.lineSeparator(), history.toString());
                        break;

                    case Constants.COMMAND_UNIQUE_HISTORY:
                        controller.printLine("History:" + System.lineSeparator(), history.getUniqueHistory());
                        break;

                    default:
                        result = CalculateExpression(line);
                        controller.printLine(Constants.VALUE_STRING, result);
                }

                history.addEvent(line, result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Uses the computer to calculate the expression and history holder to store
     * events
     *
     * @param expression the math expression string
     * @return a string with the computed expression or error description
     */
    String CalculateExpression(final String expression) {
        String result = history.getResult(expression);

        if (result == null) {
            try {
                result = computer.compute(expression);

            } catch (InvalidInputExpressionException | NullPointerException e) {
                result = e.getMessage() + Constants.ERROR;
            }
        }

        return result;
    }
}
