package com.sysgears.simplecalculator;

import com.sysgears.simplecalculator.computer.Expression;
import com.sysgears.simplecalculator.computer.Operators;

public class Calculator {
    public static void main(String[] args) {
        System.out.println(Operators.DIVIDE.evaluate(new Expression(222, 24, Operators.DIVIDE)));

        System.out.println(Operators.DIVIDE.getPriority());
    }
}
