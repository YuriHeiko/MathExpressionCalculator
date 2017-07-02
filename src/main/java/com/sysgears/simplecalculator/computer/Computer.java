package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

import java.util.Objects;

/**
 * Contains common logic and interface contract for computing algorithms
 */
public abstract class Computer {
    /**
     * A pattern for a valid number
     */
    final String NUMBER_EXP = "-?\\d+([.]\\d+)?";


    /**
     * Validates the incoming string. Removes all unnecessary characters.
     * Alters all ',' to '.'. Computes the expression.
     *
     * @param expression The string contains a valid math expression which
     *                   can be empty
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    public String compute(String expression) throws InvalidInputExpressionException {

        expression = Objects.requireNonNull(expression, "Incoming string cannot be null");
        expression = expression.
                                replaceAll("\\s", "").
                                replaceAll(",", ".").
                                replaceAll("\\(\\)", "");

        if (!expression.isEmpty()) {

            expression = openParentheses(expression);

            if (!expression.matches(NUMBER_EXP)) {
                throw new InvalidInputExpressionException(String.format("Input data is probably invalid cause " +
                        "the result of calculation: \"%s\" is not a number.", expression));
            }
        }

        return expression;
    }

    /**
     * Finds all parts of the expression which are enclosed in parentheses.
     * Computes such parts and put the value instead of the respective
     * enclosed part. Removes parentheses respectively. Computes the remaining
     * expression
     *
     * @param expression The string contains a valid math expression
     * @return The string contains an expression with open parentheses
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    abstract String openParentheses(String expression);

    /**
     * Computes the received expression according to the math rules.
     *
     * @param expression The string contains a valid math expression without
     *                   parentheses
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    abstract String computeArithmeticExpression(String expression);

    /**
     * Computes the binary expression
     *
     * @param expression The string contains a binary expression
     * @return The string contains computed value
     */
    String computeBinaryExpression(String expression, final Operators operator) {
        try {
            String leftOperand = expression.substring(0, expression.lastIndexOf(operator.getRepresentation()));
            String rightOperand = expression.substring(expression.lastIndexOf((operator.getRepresentation())) + 1);

            expression = operator.calculate(Double.parseDouble(leftOperand),
                    Double.parseDouble(rightOperand)).toString();

        } catch (NumberFormatException | StringIndexOutOfBoundsException | ArithmeticException e) {
            throw new InvalidInputExpressionException(String.format("Input data is probably invalid cause " +
                    "this part of expression: \"%s\" is invalid", expression));
        }

        return expression;
    }

    /**
     * Normalizes an expression according to common math rules
     * <p>
     *     <ul>
     *         <li> if there is '--' after '(', it is altered by ""</li>
     *         <li>remaining "--" is altered by "+"</li>
     *         <li>if the string starts with '+', it is altered by ""</li>
     *         <li>finally, all the "+-" are altered by "-"</li>
     *     </ul>
     * </p>
     *
     * @param expression The string contains a math expression
     * @return The normalized expression
     */
    String normalizeExpression(final String expression) {
        return expression.
                replaceAll("(?<=[(])--", "").
                replaceAll("--", "+").
                replaceAll("^\\+", "").
                replaceAll("\\+-|-\\+", "-");
    }
}
