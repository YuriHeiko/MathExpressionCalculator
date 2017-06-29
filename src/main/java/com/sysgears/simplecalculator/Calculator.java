package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.ui.ConsoleController;
import com.sysgears.simplecalculator.ui.UIController;

import java.io.IOException;

public class Calculator {
    public static void main(String[] args) {
        try {
            final String PROMPT_STRING = "Type an expression to calculate or 'exit' to exit";
            final String VALUE_STRING = "Calculated value is ";
            UIController uiController = new ConsoleController();

            String expression;
            while (!(expression = uiController.read(PROMPT_STRING)).equals("exit")) {
                String value = new Computer().evaluate(expression);
                uiController.write(VALUE_STRING, value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
