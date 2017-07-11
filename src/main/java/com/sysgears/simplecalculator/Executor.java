package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.computer.function2.FunctionComputer;
import com.sysgears.simplecalculator.history.HistoryHolder;
import com.sysgears.simplecalculator.ui.ConsoleController;
import com.sysgears.simplecalculator.ui.UIController;

/**
 * Is used to run a demo of Simple Calculator application
 */
public class Executor {
    /**
     * The starting point of the UI interface demo
     *
     * @param args the string array with command line parameters
     */
    public static void main(String[] args) {
        try (UIController controller = new ConsoleController()) {
            HistoryHolder history = new HistoryHolder();
            Computer computer = new FunctionComputer();

            new Calculator(controller, history, computer).run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}