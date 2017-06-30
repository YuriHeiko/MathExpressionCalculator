package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Attempts to calculate a received math expression according to the math
 * precedence. The ideas lie behind the algorithm are next:
 * <p>
 *     <ul>
 *         <li>recursively opens all the parentheses by calculating the
 *         enclosed expressions</li>
 *         <li>calculates the remaining parts of the expression according
 *         to operators precedence</li>
 *         <li>all possible operators are stored in {@link Operators}</li>
 *         </ul>
 * </p>
 */
public class ComputerRegExp extends Computer {
    /**
     * A pattern for a valid number
     */
    private final String NUMBER_EXP = "\\-?\\d+([.]{1}\\d+)?";

    /**
     * Finds all parts of the expression which are enclosed in parentheses.
     * Computes such parts and put the value instead of the respective
     * enclosed part. Removes parentheses respectively. Computes the
     * remaining arithmetic expression.
     *
     * @param expression The string contains a valid math expression
     * @return The string contains an expression with open parentheses
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    @Override
    String openParentheses(String expression) throws InvalidInputExpressionException {
        Pattern pattern = Pattern.compile(
                "\\({1}((" + NUMBER_EXP + ")|((" + NUMBER_EXP + Operators.getRegExp() + ")+" + NUMBER_EXP + ")){1}\\){1}");

        for (Matcher matcher = pattern.matcher(expression);
             matcher.find();
             matcher = pattern.matcher(expression)) {

            expression = normalizeExpression(expression.replace(matcher.group(0), openParentheses(matcher.group(1))));
        }

        return computeArithmeticExpression(expression);
    }

    /**
     * Computes the received expression according to the math rules.
     * The ideas lie behind the algorithm are next:
     * <p>
     *     <ul>
     *         <li>iterates by all possible operators that exist in
     *         {@code Operators} class</li>
     *         <li>finds a binary expression that uses such an operator</li>
     *         <li>computes the expression</li>
     *         <li>puts the value instead of the corresponding part
     *         <li>continues until all the possible parts are computed</li>
     *         </ul>
     * </p>
     * <p>
     *     "(?<![-])" helps replace expressions that don't have a minus before, i.e.
     *     expression = 1-1-1-1+1-1       binary one = 1-1      computed one = 0.0
     *     and the result after replacement = 0.0-1-1+0.0
     * </p>
     *
     * @param expression The string contains a valid math expression without parentheses
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    @Override
    String computeArithmeticExpression(String expression) throws InvalidInputExpressionException {
        for (Operators operator : Operators.values()) {
            Pattern pattern = Pattern.compile(NUMBER_EXP + "[" + operator.getRegExpRepresentation() + "]{1}" + NUMBER_EXP);

            for (Matcher matcher = pattern.matcher(expression); matcher.find(); matcher = pattern.matcher(expression)) {
                String binaryExpression = matcher.group(0);

                expression =
                        expression.
                                replaceAll("(?<![-])" + Pattern.quote(binaryExpression),
                                        computeBinaryExpression(binaryExpression, operator));
            }
        }

        return expression;
    }
}