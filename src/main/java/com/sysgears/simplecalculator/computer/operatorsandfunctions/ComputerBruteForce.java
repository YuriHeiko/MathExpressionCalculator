package com.sysgears.simplecalculator.computer.operatorsandfunctions;


import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;

import java.util.regex.Pattern;

/**
 * Calculates a received math expression according to the {@link Operators}
 * precedence. The ideas that lie behind the algorithm are next:
 * <p>
 *     <ul>
 *         <li>recursively opens all the parentheses by calculating the
 *         enclosed expressions</li>
 *         <li>calculates the remaining parts of the expression according
 *         to operators precedence</li>
 *         <li>all possible operators are stored in {@code Operators}</li>
 *     </ul>
 * </p>
 */
public class ComputerBruteForce extends OperatorsComputer {
    /**
     * Finds recursively all parts of the expression which are enclosed in
     * parentheses. Computes such parts and puts the value instead of the
     * corresponding enclosed part. Removes parentheses respectively.
     *
     * @param expression The string contains a math expression
     * @return The string contains the expression with open parentheses
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    String openEnclosedExpression(final String expression) throws InvalidInputExpressionException {
        String result = expression;

        while (hasEnclosedExpression(result)) {
            String parenthesesExpression = getEnclosedExpression(result, OPEN_EXP);

            result = normalizeExpression(result.replace(OPEN_EXP + parenthesesExpression + CLOSE_EXP,
                                                        computeArithmeticExpression(parenthesesExpression)));
        }

        return result;
    }

    /**
     * Computes an expression according to the {@code Operators} precedence.
     * The ideas that lie behind the algorithm are next:
     * <p>
     *     <ul>
     *         <li>if the expression contains parentheses calls {@code
     *         openEnclosedExpression()} to open them</li>
     *         <li>iterates by all possible operators that exist in
     *         {@code Operators} class</li>
     *         <li>finds a binary expression that uses such an operator</li>
     *         <li>computes the expression</li>
     *         <li>puts the value instead of the corresponding parts
     *         <li>continues until all the possible parts are computed</li>
     *     </ul>
     * </p>
     *
     * @param expression The string contains a math expression
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    String computeArithmeticExpression(final String expression) throws InvalidInputExpressionException {
        String result = expression;

        if (hasFunction(result)) {
            result = compute(result);
        }

        if (hasEnclosedExpression(result)) {
            result = openEnclosedExpression(result);
        }
        
        for (Operators operator : Operators.values()) {
            while (containsOperator(result, operator)) {
                String binaryExpression = getBinaryExpression(result, operator);

                result = normalizeExpression(result.replaceAll(NO_MINUS_BEFORE_EXP + Pattern.quote(binaryExpression),
                                                                computeBinaryExpression(binaryExpression, operator)));
            }
        }

        return result;
    }

    /**
     * Checks an incoming string whether it contains the required operator. This check
     * function correctly process the situation when there is a minus at the first
     * place of the string and the operator representation is '-' cause searching
     * starts from the end and it doesn't look at the first string position.
     *
     * @param expression The string contains a math expression
     * @param operator   The required operator
     * @return true If the operator is found
     */
    boolean containsOperator(final String expression, final Operators operator) {
        return expression.lastIndexOf(operator.getRepresentation()) > 0;
    }

    /**
     * Returns the first encountered binary expression with the specified operator
     *
     * @param expression The string contains a math expression without
     *                   parentheses.
     * @param operator   The required operator
     * @return The string contains the binary expression
     */
    String getBinaryExpression(final String expression, final Operators operator) {
        int operatorIndex = expression.indexOf(operator.getRepresentation(), 1);
        int leftBound = operatorIndex - 1;
        // step on two indexes further to solve situations like: 10*-2
        int rightBound = operatorIndex + 2;

        while (leftBound > 0 &&
                (Character.isDigit(expression.charAt(leftBound)) || expression.charAt(leftBound) == '.')) {

            leftBound--;
        }

        // step back if it is not zero index and the minus sign index, i.e.
        // operator = '*', '3+2*10' -> step back -> '2*10'
        //                 '3-2*10' ->     ok    -> '-2*10'
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