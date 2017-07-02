package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Calculates a received math expression according to the {@code Operators}
 * precedence. It is based on regular expressions. The ideas lie behind the
 * algorithm are next:
 * <p>
 *     <ul>
 *         <li>recursively opens all the parentheses by calculating the
 *         enclosed expressions</li>
 *         <li>calculates the remaining parts of the expression according
 *         to operators precedence</li>
 *         <li>all possible operators are stored in {@link Operators}</li>
 *         </ul>
 * </p>
 * Contains common logic and interface contract for computing algorithms
 */
public abstract class Computer {
    /**
     * A pattern for a valid number
     */
    final String NUMBER_EXP = "-?\\d+([.]\\d+)?";


    /**
     * Validates an incoming string. Removes all unnecessary characters.
     * Replaces all ',' to '.'. Computes the expression.
     *
     * @param expression The string contains a math expression. Can be empty
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    public String compute(String expression) throws InvalidInputExpressionException {

        expression = Objects.requireNonNull(expression, "Incoming string cannot be null");

        expression = expression.replaceAll("\\s", "").
                                replaceAll(",", ".").
                                replace("()", "");

        expression = convertFromScientificNotation(expression);

        if (!expression.isEmpty()) {

            expression = computeArithmeticExpression(expression);

            if (!expression.matches(NUMBER_EXP)) {
                throw new InvalidInputExpressionException(String.format("Input data is invalid cause " +
                        "the result of calculation: '%s' is not a number.", expression));
            }
        }

        return expression;
    }

    /**
     * Finds all parts of the expression which are enclosed in parentheses.
     * Computes such parts and put the value instead of the corresponding
     * parts. Removes parentheses respectively. Computes the remaining
     * expression
     *
     * @param expression The string contains a math expression
     * @return The string contains an expression with open parentheses
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    abstract String openParentheses(String expression) throws InvalidInputExpressionException;

    /**
     * Computes the received expression according to the math rules.
     *
     * @param expression The string contains the math expression
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    abstract String computeArithmeticExpression(String expression) throws InvalidInputExpressionException;

    /**
     * Computes the binary expression.
     *
     * @param expression The string contains a binary expression
     * @return The string contains the computed value
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    String computeBinaryExpression(String expression, final Operators operator) throws InvalidInputExpressionException {
        try {
            String leftOperand = expression.substring(0, expression.lastIndexOf(operator.getRepresentation()));
            String rightOperand = expression.substring(expression.lastIndexOf((operator.getRepresentation())) + 1);

            expression =
                    Operators.convertFromScientificNotation(
                            operator.calculate(Double.parseDouble(leftOperand), Double.parseDouble(rightOperand)));

        } catch (NumberFormatException | StringIndexOutOfBoundsException | ArithmeticException e) {
            throw new InvalidInputExpressionException(String.format("Input data is invalid because of " +
                    "this part of expression: '%s'", expression));
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
        return expression.replaceAll("(?<=[(])--", "").
                        replaceAll("--", "+").
                        replaceAll("^\\+", "").
                        replaceAll("\\+-|-\\+", "-");
    }

    /**
     * Converts all the numbers in the incoming String which are written in
     * E-notation to the same values in decimal notation.
     *
     * @param expression the string contains a math expression
     * @return the converted string
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    String convertFromScientificNotation(String expression) throws InvalidInputExpressionException {
        Pattern pattern = Pattern.compile("\\d+([.,]?\\d+)?[eE]-?\\d+");
        try {
            for (Matcher matcher = pattern.matcher(expression); matcher.find(); matcher = pattern.matcher(expression)) {
                expression =
                        expression.
                                replace(matcher.group(),
                                        Operators.convertFromScientificNotation(Double.parseDouble(matcher.group())));
            }

        } catch (NumberFormatException e) {
            throw new InvalidInputExpressionException("Input data is invalid. Some numbers cannot be converted from " +
                    "E-notation");
        }

        return expression;
    }
}