package com.sysgears.simplecalculator.ui;

import com.sysgears.simplecalculator.computer.Operators;
import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;
import com.sysgears.simplecalculator.history.HistoryHolder;

import static com.sysgears.simplecalculator.ui.Commands.*;

/**
 * Handles {@link Commands}
 */
public class CommandsHandler {

    /**
     * Handles commands received from UI
     *
     * @param command The command
     * @param controller The {@link UIController} implementation
     * @param history The {@link HistoryHolder}
     */
    public static void handle(Commands command, final UIController controller, HistoryHolder history) {
        switch (command) {
            case EXIT:
                controller.printLine(System.lineSeparator() + "Good bye!");
                break;

            case HELP:
                controller.printLine(HELP.HEADER, Commands.getHelp());
                break;

            case HISTORY:
                controller.printLine(HISTORY.HEADER, history.toString());
                break;

            case HISTORY_UNIQUE:
                controller.printLine(HISTORY_UNIQUE.HEADER, history.getUniqueHistory());
                break;

            case OPERATORS:
                controller.printLine(OPERATORS.HEADER, Operators.getList());
                break;

            default:
                throw new InvalidInputExpressionException("There is no handler for " + command);
        }
    }
}
