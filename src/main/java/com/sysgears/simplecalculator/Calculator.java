package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.parser.ExpressionCalculator;
import com.sysgears.simplecalculator.ui.ConsoleController;
import com.sysgears.simplecalculator.ui.UIController;

import java.io.IOException;

public class Calculator {
    public static void main(String[] args) {
        try {
            final String PROMPT_STRING = "Enter an expression to evaluate or 'exit' to exit";
            final String VALUE_STRING = "Evaluated value is ";
            UIController uiController = new ConsoleController();
            // \-?\d+(\.{1}\d+)?[\/\*\-\+]{1}\-?\d+(\.{1}\d+)?
            // \({1}((\-?\d+(\.{1}\d+)?)|((\-?\d+(\.{1}\d+)?[\/\*\-\+]{1})+\-?\d+(\.{1}\d+)?)){1}\){1}
            // (-(12-7)*(6-2)+4/(7-3))*9
            // ((12-7)*(6-2)+4/(7-3))*9-1000*2+(10-10)
            String line;
            while (!(line = uiController.read(PROMPT_STRING)).equals("exit")) {
                Double value = new ExpressionCalculator(line).evaluate();
                uiController.write(VALUE_STRING, value.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
