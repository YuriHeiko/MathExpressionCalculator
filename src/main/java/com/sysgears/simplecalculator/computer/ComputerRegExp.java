package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;
import com.sysgears.simplecalculator.utils.RegExpUtils;

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
    private final String PARENTHESES = "\\({1}((" + NUMBER + ")|((" + NUMBER + Operators.getRegExp() + ")+" + NUMBER + ")){1}\\){1}";
    private final Pattern PARENTHESES_PATTERN = Pattern.compile(PARENTHESES);

    /**
     * Finds all parts of the expression which are enclosed in parentheses.
     * Computes such parts and put the value instead of the respective
     * enclosed part. Removes parentheses respectively.
     * Computes the remaining arithmetic expression
     *
     * @param expression String contains a valid math expression
     * @return String contains an expression with open parentheses
     * @throws InvalidInputExpressionException if the incoming string has an
     *                                         invalid format
     */
    @Override
    String openParentheses(String expression) throws InvalidInputExpressionException {
        // TODO refactor this
        for (Matcher matcher = PARENTHESES_PATTERN.matcher(expression);
             matcher.find();
             matcher = PARENTHESES_PATTERN.matcher(expression)) {

            expression = expression.replace(matcher.group(0), openParentheses(matcher.group(1))).replace("--", "+");
        }

        return computeArithmeticExpression(expression);
    }

    /**
     * Computes the received expression according to the math rules.
     * The ideas lie behind the algorithm are next:
     * <p><ul>
     * <li>iterates by all possible operators that exist in {@code Operators}
     * class</li>
     * <li>finds a binary expression that use such an operator</li>
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
    String computeArithmeticExpression(String expression) throws InvalidInputExpressionException {
        for (Operators operator : Operators.values()) {
            String arithmeticExpression = NUMBER + "[" + operator.getDepictionRegExp() + "]{1}" + NUMBER;
            Pattern pattern = Pattern.compile(arithmeticExpression);

            for (Matcher matcher = pattern.matcher(expression); matcher.find(); matcher = pattern.matcher(expression)) {
                String binaryExpression = matcher.group(0);
                expression =
                        expression.
                                replaceAll("(?<![-])" + RegExpUtils.changeToRegExp(binaryExpression),
                                        computeBinaryExpression(binaryExpression, operator));
            }
        }

        return expression;
    }
}