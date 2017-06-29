package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

/**
 * Attempts to calculate a received math expression according to the
 * math precedence. The ideas behind the algorithm are next:
 * <p><ul>
 * <li>recursively open all the parentheses by calculating
 * the enclosed expressions</li>
 * <li>calculate the remaining parts of the expression according
 * to operators precedence</li>
 * <li>all possible operators are stored in {@link Operators} class</li>
 * </ul></p>
 */
public class Computer {
    private static final int LEFT = -1;
    private static final int RIGHT = 1;

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

    /**
     * Finds all parts of the expression which are enclosed in parentheses.
     * Computes such parts and put the value instead of the respective
     * enclosed part. Removes parentheses respectively.
     *
     * @param expression String contains a valid math expression
     * @return String contains an expression with open parentheses
     * @throws InvalidInputExpressionException if the incoming string has an
     *                                         invalid format
     */
    String openParentheses(String expression) throws InvalidInputExpressionException {
        while (expression.contains("(")) {
            String parenthesesExpression = getParenthesesExpression(expression);
            expression = expression.replace(
                    "(" + parenthesesExpression + ")",
                    openParentheses(parenthesesExpression));
        }

        return calculateExpression(expression);
    }

    /**
     * Finds and returns a part of the expression which is enclosed in
     * parentheses. Searching starts from the left side of the expression.
     *
     * @param expression String contains a valid math expression
     * @return String contains an enclosed expression that can be empty
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
     * The ideas behind the algorithm are next:
     * <p><ul>
     * <li>iterates by all possible operators that exist in {@code Operators}
     * class</li>
     * <li>finds parts of expression that use such an operator</li>
     * <li>split the found part into operands</li>
     * <li>computes the expression</li>
     * <li>put the value instead of the respective part until all the
     * possible parts are computed</li>
     * </ul></p>
     *
     * @param expression String contains a valid math expression without parentheses
     * @return String contains the calculated expression
     * @throws InvalidInputExpressionException if the incoming string has an
     *                                         invalid format
     */
    String calculateExpression(String expression) throws InvalidInputExpressionException {
        for (Operators operator : Operators.values()) {
            while (containOperator(expression, operator)) {
                String leftOperand = getExpressionOperand(expression, operator, LEFT);
                String rightOperand = getExpressionOperand(expression, operator, RIGHT);

                String calculatedValue;
                try {
                    calculatedValue = operator.calculate(Double.parseDouble(leftOperand),
                            Double.parseDouble(rightOperand)).toString();

                } catch (NumberFormatException e) {
                    throw new InvalidInputExpressionException(String.format("Input data is probably invalid cause " +
                            "this part of expression: \"%s\" is invalid", expression));
                }

                expression =
                        expression.
                                replace(leftOperand + operator.getDepiction() + rightOperand, calculatedValue).
                                replace("+-", "-");
            }
        }

        return expression;
    }

    /**
     * Checks incoming string whether it contains the required operator.
     *
     * @param expression String contains a valid math expression without parentheses
     * @param operator the required operator
     * @return true if such the operator is found
     */
    boolean containOperator(final String expression, final Operators operator) {
        return expression.charAt(0) == '-'
                ? expression.indexOf(operator.getDepiction(), 1) != -1
                : expression.contains(operator.getDepiction());
    }

    /**
     * Get the operand of the received expression according its side and the operator
     *
     * @param expression String contains a valid math expression with only two operands
     *                   and received operator, without parentheses.
     * @param operator  the required operator
     * @param SIDE stipulates the side of the operand
     * @return String contains the according operand
     */
    String getExpressionOperand(final String expression, final Operators operator, final int SIDE) {
        int operatorIndex = expression.indexOf(operator.getDepiction(), 1);
        int index = operatorIndex + SIDE;

        while (index > 0 && index < expression.length() &&
                (Character.isDigit(expression.charAt(index)) || expression.charAt(index) == '.')) {

            index += SIDE;
        }

        if (SIDE == LEFT) {
            if (index > 0 && expression.charAt(index) != '-') {
                index++; // step back
            }
            return expression.substring(index, operatorIndex);

        } else {
            return expression.substring(operatorIndex + 1, index);
        }
    }
}