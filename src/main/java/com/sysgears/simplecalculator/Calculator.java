package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.ui.ConsoleController;
import com.sysgears.simplecalculator.ui.UIController;

import java.io.IOException;

public class Calculator {
    public static void main(String[] args) {
        try {
            final String PROMPT_STRING = "Enter an expression to evaluate or 'exit' to exit";
            final String VALUE_STRING = "Evaluated value is ";
            UIController uiController = new ConsoleController();

            String line;
            while (!(line = uiController.read(PROMPT_STRING)).equals("exit")) {
                uiController.write(VALUE_STRING, line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
