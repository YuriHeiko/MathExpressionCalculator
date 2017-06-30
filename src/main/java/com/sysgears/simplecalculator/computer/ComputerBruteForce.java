package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

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
public class ComputerBruteForce extends Computer {
    /**
     * Finds all parts of the expression which are enclosed in parentheses.
     * Computes such parts and put the value instead of the corresponding
     * enclosed part. Removes parentheses respectively. Computes the remaining
     * arithmetic expression
     *
     * @param expression The string contains a valid math expression
     * @return The string contains an expression with open parentheses
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    String openParentheses(String expression) throws InvalidInputExpressionException {
        while (expression.contains("(")) {
            String parenthesesExpression = getParenthesesExpression(expression);
            expression = normalizeExpression(
                    expression.replace("(" + parenthesesExpression + ")", openParentheses(parenthesesExpression)));
        }

        return computeArithmeticExpression(expression);
    }

    /**
     * Finds and returns a part of the expression which is enclosed in
     * parentheses. Searching starts from the left side of the expression.
     *
     * @param expression The string contains a valid math expression
     * @return The string contains an enclosed expression that can be empty
     */
    String getParenthesesExpression(final String expression) {
        int startIndex = expression.indexOf('(');
        int endIndex = startIndex;

        for (int counter = 1; counter > 0; ) {
            if (expression.charAt(++endIndex) == '(') {
                counter++;

            } else if (expression.charAt(endIndex) == ')') {
                counter--;
            }
        }

        return expression.substring(startIndex + 1, endIndex);
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
     * @param expression The string contains a valid math expression without
     *                   parentheses
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    String computeArithmeticExpression(String expression) throws InvalidInputExpressionException {
        for (Operators operator : Operators.values()) {
            while (containsOperator(expression, operator)) {
                String binaryExpression = getBinaryExpression(expression, operator);
                expression =
                        normalizeExpression(
                                expression.replaceAll("(?<![-])" + Pattern.quote(binaryExpression),
                                        computeBinaryExpression(binaryExpression, operator)));
            }
        }

        return expression;
    }

    /**
     * Checks incoming string whether it contains the required operator.
     *
     * @param expression The string contains a valid math expression without
     *                   parentheses
     * @param operator   The required operator
     * @return true if such the operator is found
     */
    boolean containsOperator(final String expression, final Operators operator) {
        return expression.charAt(0) == '-'
                ? expression.indexOf(operator.getRepresentation(), 1) != -1
                : expression.contains(operator.getRepresentation());
    }

    /**
     * Returns the first encountered binary expression with the specified operator
     *
     * @param expression The string contains a valid math expression without
     *                   parentheses.
     * @param operator   The required operator
     * @return The string contains the binary expression
     */
    String getBinaryExpression(final String expression, final Operators operator) {
        int operatorIndex = expression.indexOf(operator.getRepresentation(), 1);
        int leftBound = operatorIndex - 1;
        int rightBound = operatorIndex + 1 + (Character.isDigit(expression.charAt(operatorIndex)) ? 0 : 1);

        while (leftBound > 0 && leftBound < expression.length() &&
                (Character.isDigit(expression.charAt(leftBound)) || expression.charAt(leftBound) == '.')) {

            leftBound--;
        }

        while (rightBound < expression.length() &&
                (Character.isDigit(expression.charAt(rightBound)) || expression.charAt(rightBound) == '.')) {

            rightBound++;
        }

        if (leftBound > 0 && expression.charAt(leftBound) != '-') {
            leftBound++; // step back if minus before
        }

        return expression.substring(leftBound, rightBound);
    }
}