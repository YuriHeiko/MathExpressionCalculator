package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Attempts to calculate a received math expression according to the
 * math precedence. The ideas behind the algorithm are next:
 * <p><ul>
 * <li>recursively opens all the parentheses by calculating the
 * enclosed expressions</li>
 * <li>calculates the remaining parts of the expression according
 * to operators precedence</li>
 * <li>all possible operators are stored in {@link Operators} class</li>
 * </ul></p>
 */
public class ComputerRegExp extends Computer {
    private final String NUMBER_EXP = "\\-?\\d+([.,]{1}\\d+)?";
    private final Pattern PARENTHESES = Pattern.compile(
            "\\({1}((" + NUMBER_EXP + ")|((" + NUMBER_EXP + Operators.getRegExp() + ")+" + NUMBER_EXP + ")){1}\\){1}");

    /**
     * Finds all parts of the expression which are enclosed in parentheses.
     * Computes such parts and put the value instead of the respective
     * enclosed part. Removes parentheses respectively. Computes the
     * remaining arithmetic expression.
     *
     * @param expression String contains a valid math expression
     * @return String contains an expression with open parentheses
     * @throws InvalidInputExpressionException if the incoming string has an
     *                                         invalid format
     */
    @Override
    String openParentheses(String expression) throws InvalidInputExpressionException {
        for (Matcher matcher = PARENTHESES.matcher(expression);
             matcher.find();
             matcher = PARENTHESES.matcher(expression)) {
            // TODO replace '--' with RegExp
            expression =
                    expression.
                            replace(matcher.group(0), openParentheses(matcher.group(1))).
                            replace("--", "+");
        }

        return computeArithmeticExpression(expression);
    }

    /**
     * Computes the received expression according to the math rules. The ideas
     * lie behind the algorithm are next:
     * <p><ul>
     * <li>iterates by all possible operators that exist in {@code Operators}
     * class</li>
     * <li>finds a binary expression that uses such an operator</li>
     * <li>computes the expression</li>
     * <li>puts the value instead of the corresponding parts until all the
     * possible parts are computed</li>
     * </ul></p>
     * <p>
     * RegExp "(?<![-])" helps replace expressions that don't have a minus
     * before, i.e. expression = 1-1-1-1+1-1       binary one = 1-1
     * computed one = 0.0  and the result after replacement is 0.0-1-1+0.0
     * </p>
     *
     * @param expression String contains a valid math expression without parentheses
     * @return String contains the calculated expression
     * @throws InvalidInputExpressionException if the incoming string has an
     *                                         invalid format
     */
    @Override
    String computeArithmeticExpression(String expression) throws InvalidInputExpressionException {
        for (Operators operator : Operators.values()) {
            String arithmeticExpression = NUMBER_EXP + "[" + operator.getRegExpRepresentation() + "]{1}" + NUMBER_EXP;
            Pattern pattern = Pattern.compile(arithmeticExpression);

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