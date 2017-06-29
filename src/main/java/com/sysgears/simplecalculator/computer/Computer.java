package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

public abstract class Computer {

    /**
     * Validates the incoming string. Removes all unnecessary characters.
     * Computes the expression.
     *
     * @param expression String contains a valid math expression
     *                   which can be empty
     * @return String contains the calculated expression
     * @throws InvalidInputExpressionException if the incoming string has an
     *                                         invalid format, or it is null
     */
    public String calculate(final String expression) throws InvalidInputExpressionException {
        if (expression == null) {
            throw new InvalidInputExpressionException("Incoming string cannot be null");

        } else if (expression.isEmpty()) {
            return expression;
        }

        String result = calculateExpression(openParentheses(expression.replaceAll("\\s", "")));

        try {
            if (validateString(expression)) {
                throw new NumberFormatException();

            } else {
                result = Double.valueOf(result).toString();
            }

        } catch (NumberFormatException e) {
            throw new InvalidInputExpressionException(String.format("Input data is probably invalid cause " +
                    "the result of calculation: \"%s\" is not a number.", result));
        }

        return result;
    }


    /**
     * Checks incoming string whether it contains symbols(f/F/d/D) that are
     * used in Java to show the number format (float/double). However, it is
     * undesirable behaviour according to the common math rules.
     *
     * @param expression String contains a valid math expression
     * @return true if there are no such symbols
     */
    boolean validateString(final String expression) {
        return expression.contains("f") || expression.contains("d") || expression.contains("F") || expression.contains("D");
    }

    abstract String openParentheses(String expression);

    abstract String calculateExpression(String expression);
}
