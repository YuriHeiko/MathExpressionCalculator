package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Contains common logic and interface contract for computing algorithms
 */
public abstract class Computer {

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
        String result = expression.isEmpty()
                ? expression
                : openParentheses(expression.replaceAll("\\s", "").replace(',', '.'));

        if (!result.isEmpty()) {
            try {
                if (isStringInvalid(expression)) {
                    throw new NumberFormatException();

                } else {
                    result = Double.valueOf(result).toString();
                }

            } catch (NumberFormatException e) {
                throw new InvalidInputExpressionException(String.format("Input data is probably invalid cause " +
                        "the result of calculation: \"%s\" is not a number.", result));
            }
        }

        return result;
    }

    /**
     * Checks the incoming string whether it contains symbols(f/F/d/D) that
     * are used in Java to show the number format (float/double). However,
     * it is undesirable behaviour according to the common math rules.
     *
     * @param expression The string contains a valid math expression
     * @return true if there are such numbers
     */
    boolean isStringInvalid(final String expression) {
        return Pattern.compile("\\-?\\d+([.,]{1}\\d+)?[dDfF]{1}").matcher(expression).find();
    }

    /**
     * Finds all parts of the expression which are enclosed in parentheses.
     * Computes such parts and put the value instead of the respective
     * enclosed part. Removes parentheses respectively. Computes the remaining
     * arithmetic expression
     *
     * @param expression The string contains a valid math expression
     * @return The string contains an expression with open parentheses
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    abstract String openParentheses(String expression);

    /**
     * Computes the received expression according to the math rules.
     * The ideas lie behind the algorithm are next:
     * <p><ul>
     * <li>iterates by all possible operators that exist in {@code Operators}
     * class</li>
     * <li>finds a binary expression that uses such an operator</li>
     * <li>computes the expression</li>
     * <li>puts the value instead of the corresponding part
     * <li>continues until all the possible parts are computed</li>
     * </ul></p>
     *
     * @param expression The string contains a valid math expression without
     *                   parentheses
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    abstract String computeArithmeticExpression(String expression);

    /**
     * Computes the math binary expression
     *
     * @param expression The string contains a valid binary expression
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
}
