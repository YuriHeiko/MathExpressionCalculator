package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.history.HistoryHolder;
import com.sysgears.simplecalculator.ui.ConsoleController;
import com.sysgears.simplecalculator.ui.UIController;

import java.io.IOException;

public class Executor {
    public static void main(String[] args) throws IOException {
        UIController controller = new ConsoleController();
        HistoryHolder history = new HistoryHolder();
        Computer computer = new Computer();

        new Calculator(controller, history, computer).run();

        controller.close();
    }
}
