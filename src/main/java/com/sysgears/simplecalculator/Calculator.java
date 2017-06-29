package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;
import com.sysgears.simplecalculator.history.HistoryHolder;
import com.sysgears.simplecalculator.ui.Constants;
import com.sysgears.simplecalculator.ui.UIController;

public class Calculator {
    private final UIController controller;
    private final HistoryHolder history;
    private final Computer computer;

    public Calculator(final UIController controller, final HistoryHolder history, final Computer computer) {
        this.controller = controller;
        this.history = history;
        this.computer = computer;
    }

    public void run() {
        String result = "";

        try {
            controller.printLine(Constants.DESCRIPTION);

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

    String CalculateExpression(final String expression) {
        String result = history.getResult(expression);

        if (result == null) {
            try {
                result = computer.calculate(expression);

            } catch (InvalidInputExpressionException | NullPointerException e) {
                result = e.getMessage() + Constants.ERROR;
            }
        }

        return result;
    }
}
