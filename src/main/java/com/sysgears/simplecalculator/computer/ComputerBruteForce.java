package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

import java.util.regex.Pattern;

/**
 * Calculate a received math expression according to the {@code Operators}
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
     * expression
     *
     * @param expression The string contains a valid math expression
     * @return The string contains the expression with open parentheses
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
        int startIndex = expression.indexOf('(') + 1;
        int endIndex = startIndex;

        try {
            for (int counter = 1; counter > 0; endIndex++) {
                if (expression.charAt(endIndex) == ')') {
                    counter--;

                } else if (expression.charAt(endIndex) == '(') {
                    counter++;
                }
            }

        } catch (StringIndexOutOfBoundsException e) {
            throw new InvalidInputExpressionException(String.format("Input data is probably invalid cause " +
                    "this part of expression: \"%s\" is invalid", expression));
        }

        return expression.substring(startIndex, endIndex - 1);
    }

    /**
     * Computes the received expression according to the math rules. The ideas
     * lie behind the algorithm are next:
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
     *     and the result after replacement = 0.0-1-1+0.0 (NOT 0.0-0.0+0.0 otherwise)
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
     * Checks incoming string whether it contains the required operator. This check
     * function correctly process the situation when there is a minus at the first
     * place of the string and the operator representation is '-' cause searching
     * starts from the end and it doesn't consider the first string position as valid.
     *
     * @param expression The string contains a valid math expression
     * @param operator   The required operator
     * @return true if such the operator is found
     */
    boolean containsOperator(final String expression, final Operators operator) {
        return expression.lastIndexOf(operator.getRepresentation()) > 0;
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
        // step on two indexes further to solve such '10*-2' situations
        int rightBound = operatorIndex + 2;

        while (leftBound > 0 &&
                (Character.isDigit(expression.charAt(leftBound)) || expression.charAt(leftBound) == '.')) {

            leftBound--;
        }

        // step back if it is not both zero index and the minus sign index,
        // i.e. operator = '*', '3+2*10' -> step back -> '2*10'
        //                      '3-2*10' ->     ok    -> '-2*10'
        if (leftBound > 0 && expression.charAt(leftBound) != '-') {
            leftBound++;
        }

        while (rightBound < expression.length() &&
                (Character.isDigit(expression.charAt(rightBound)) || expression.charAt(rightBound) == '.')) {

            rightBound++;
        }


        return expression.substring(leftBound, rightBound);
    }
}