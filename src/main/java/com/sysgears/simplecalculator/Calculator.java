package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.computer.Operators;
import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;
import com.sysgears.simplecalculator.history.HistoryHolder;
import com.sysgears.simplecalculator.ui.Commands;
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
     * Uses the controller to conduct dialogue with a user.
     */
    public void run() {
        String result = "";

        try {
            controller.printLine(
                    "This application can solve simple math expressions according to their math precedence");

            while (true) {
                String line = controller.readLine(
                        "Type an expression to calculate (or type 'help' for allowed commands):").toLowerCase();

                if (line.equals(Commands.EXIT.COMMAND)) {
                    controller.printLine(System.lineSeparator() + "Good bye!");
                    return;
                } else if (line.equals(Commands.HELP.COMMAND)) {
                    controller.printLine(Commands.HELP.getHEADER(), Commands.getList());
                } else if (line.equals(Commands.HISTORY.COMMAND)) {
                    controller.printLine(Commands.HISTORY.getHEADER(), history.toString());
                } else if (line.equals(Commands.UNIQUE_HISTORY.COMMAND)) {
                    controller.printLine(Commands.UNIQUE_HISTORY.getHEADER(), history.getUniqueHistory());
                } else if (line.equals(Commands.OPERATORS.COMMAND)) {
                    controller.printLine(Commands.OPERATORS.getHEADER(), Operators.getList());
                } else {
                    result = CalculateExpression(line);
                    controller.printLine("", result);
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
                result = e.getMessage() + System.lineSeparator() + "\t\tPlease read the instructions carefully.";
            }
        }

        return result;
    }
}
