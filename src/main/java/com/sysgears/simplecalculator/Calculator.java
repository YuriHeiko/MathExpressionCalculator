package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.computer.Expression;
import com.sysgears.simplecalculator.computer.Operators;
import com.sysgears.simplecalculator.ui.ConsoleReader;
import com.sysgears.simplecalculator.ui.InputStringReader;

public class Calculator {
    public static void main(String[] args) {
        final String PROMPT_STRING = "Enter an expression to evaluate or 'exit' to exit";
        InputStringReader consoleReader = new ConsoleReader();

        String line;
        while (!(line = consoleReader.read(PROMPT_STRING)).equals("exit")) {
//            System.out.println(new Expression().evaluate(line));
            System.out.println("---------------------------------------------------------");
        }
    }
}
