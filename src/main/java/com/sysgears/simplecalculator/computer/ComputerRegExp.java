package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class ComputerRegExp extends Computer {
    private final String NUMBER = "\\-?\\d+([.,]{1}\\d+)?";
    private final String EXPRESSION = NUMBER + Operators.getRegExp() + NUMBER;
    private final String PARENTHESES = "\\({1}((" + NUMBER + ")|((" + EXPRESSION + ")){1}\\){1}";

    private final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER);
    private final Pattern EXPRESSION_PATTERN = Pattern.compile(EXPRESSION);
    private final Pattern PARENTHESES_PATTERN = Pattern.compile(PARENTHESES);

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
    @Override
    String openParentheses(String expression) throws InvalidInputExpressionException {
        Matcher matcher = PARENTHESES_PATTERN.matcher(expression);
        while (matcher.find()) {
            String parenthesesExpression = matcher.group(0);

            expression = expression.replace(
                    "(" + parenthesesExpression + ")",
                    openParentheses(parenthesesExpression));
        }

        return calculateExpression(expression);
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
    @Override
    String calculateExpression(String expression) throws InvalidInputExpressionException {
        Matcher matcher = EXPRESSION_PATTERN.matcher(expression);
        while (matcher.find()) {

        }

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
}