package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.computer.Operators;
import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;
import com.sysgears.simplecalculator.history.HistoryHolder;
import com.sysgears.simplecalculator.ui.Commands;
import com.sysgears.simplecalculator.ui.UIController;

import java.io.IOException;
import java.util.Objects;

import static com.sysgears.simplecalculator.ui.Commands.*;

/**
 * Uses {@code UIController} to lead dialog with a user, {@code Computer}
 * to calculate user's math expression and {@code HistoryHolder} to keep
 * and show history.
 * <p>
 *     If a new expression has been already computed it will the result
 *     will be gotten from the history holder, and no calculations will
 *     be done.
 * </p>
 */
public final class Calculator {
    /**
     * A UI controller object
     */
    private final UIController controller;

    /**
     * An event history holder
     */
    private final HistoryHolder history;

    /**
     * A math expression computer
     */
    private final Computer computer;

    /**
     * Constructs an object
     *
     * @param controller The UI controller
     * @param history    The history holder
     * @param computer   The computer
     */
    public Calculator(final UIController controller, final HistoryHolder history, final Computer computer) {
        this.controller = Objects.requireNonNull(controller);
        this.history = history;
        this.computer = computer;
    }

    /**
     * Uses received objects to conduct dialogue with a user and compute
     * math expressions
     */
    public void run() {
        try {
            controller.printLine("This application can solve simple math expressions according to their math precedence");

            while (true) {
                String result = "";

                String line = controller.readLine(
                        "Type an expression to calculate or type 'help' to see a commands list:");

                if (line.equals(EXIT.COMMAND)) {
                    controller.printLine(System.lineSeparator() + "Good bye!");
                    return;

                } else if (line.equals(HELP.COMMAND)) {
                    controller.printLine(HELP.HEADER, Commands.getHelp());

                } else if (line.equals(HISTORY.COMMAND)) {
                    controller.printLine(HISTORY.HEADER, history.toString());

                } else if (line.equals(UNIQUE_HISTORY.COMMAND)) {
                    controller.printLine(UNIQUE_HISTORY.HEADER, history.getUniqueHistory());

                } else if (line.equals(OPERATORS.COMMAND)) {
                    controller.printLine(OPERATORS.HEADER, Operators.getList());

                } else {
                    result = CalculateExpression(line);
                    controller.printLine("", result);
                }

                history.addEvent(line, result);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                controller.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Uses the computer to calculate the expression.
     * <p>
     *     If the expression has been already computed the result will be gotten
     *     from the history holder, and no calculations will be done.
     * </p>
     *
     * @param expression The math expression string
     * @return The string with the computed expression or an error description
     */
    String CalculateExpression(final String expression) {
        String result = history.getResult(expression);

        if (result == null) {
            try {
                result = computer.compute(expression);

            } catch (InvalidInputExpressionException | NullPointerException e) {
                result = e.getMessage() + System.lineSeparator() + "\tPlease read the instructions carefully.";
            }
        }

        return result;
    }
}