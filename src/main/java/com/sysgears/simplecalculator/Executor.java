package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.computer.ComputerRegExp;
import com.sysgears.simplecalculator.history.HistoryHolder;
import com.sysgears.simplecalculator.ui.ConsoleController;
import com.sysgears.simplecalculator.ui.UIController;

import java.io.IOException;

/**
 * Is used to run a demo of Simple Calculator application
 */
public class Executor {
    /**
     * The starting point of a test UI interface
     *
     * @param args A string array with command line parameters
     * @throws IOException If an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        UIController controller = new ConsoleController();
        HistoryHolder history = new HistoryHolder();
        Computer computer = new ComputerRegExp();

        new Calculator(controller, history, computer).run();

        controller.close();
    }
}