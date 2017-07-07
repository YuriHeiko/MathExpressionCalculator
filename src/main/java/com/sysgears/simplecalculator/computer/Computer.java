package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;

public interface Computer {
    /**
     * Computes the expression.
     *
     * @param expression The string contains a math expression. Can be empty
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    String compute(String expression) throws InvalidInputExpressionException;

}
