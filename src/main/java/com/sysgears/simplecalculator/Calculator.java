package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;
import com.sysgears.simplecalculator.history.HistoryHolder;
import com.sysgears.simplecalculator.ui.Commands;
import com.sysgears.simplecalculator.ui.CommandsHandler;
import com.sysgears.simplecalculator.ui.UIController;

import java.util.Objects;

/**
 * Uses {@link UIController} to lead dialog with a user, {@link Computer}
 * to calculate user's math expression and {@link HistoryHolder} to keep
 * and show history.
 * <p>
 *     If a new expression has been already computed it will the result
 *     will be gotten from the history holder, and no calculations will
 *     be done.
 * </p>
 */
public final class Calculator {
    /**
     * A UI controller
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
            controller.printLine("This application can solve simple math expressions according to the math precedence");

            while (true) {
                String result = "";

                String line = controller.readLine(
                        "Type an expression to calculate or type 'help' to see a commands list:");

                if (Commands.isCommand(line)) {
                    CommandsHandler.handle(Commands.valueOf(line.toUpperCase().replace(" ", "_")), controller, history);

                } else {
                    line = line.replaceAll("\\s", "");
                    result = history.getResult(line);

                    if (result.isEmpty()) {
                        try {
                            result = computer.compute(line);

                        } catch (InvalidInputExpressionException e) {
                            result = e.getMessage() + " Please read the instructions carefully.";
                        }
                    }

                    controller.printLine("", result);
                }

                history.addEvent(line, result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}