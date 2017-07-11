package com.sysgears.simplecalculator.computer.operators;

import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Calculates a received math expression according to the {@link Operators}
 * precedence. It is based on regular expressions. The ideas that lie behind
 * the algorithm are next:
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
public class ComputerRegExp extends OperatorsComputer {
    /**
     * A pattern for an expression enclosed within {@code OPEN_EXP} and
     * {@code CLOSE_EXP}
     */
    private final Pattern ENCLOSED_EXP_PATTERN = Pattern.compile("\\" + OPEN_EXP + "(" + NUMBER_EXP + "|(" + NUMBER_EXP +
                                            Operators.getOperatorsRegExp() + ")+" + NUMBER_EXP + ")" + "\\" + CLOSE_EXP);

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
    @Override
    String openEnclosedExpression(final String expression) throws InvalidInputExpressionException {
        String result = expression;

        for (Matcher matcher = ENCLOSED_EXP_PATTERN.matcher(result); matcher.find();
             matcher = ENCLOSED_EXP_PATTERN.matcher(result)) {

            result = normalizeExpression(result.replace(matcher.group(0),
                                        computeArithmeticExpression(matcher.group(1))));
        }

        return result;
    }

    /**
     * Computes an expression according to the {@code Operators} precedence.
     * The ideas that lie behind the algorithm are next:
     * <p>
     *     <ul>
     *         <li>if the expression contains parentheses, calls {@code
     *         openEnclosedExpression()} to open them</li>
     *         <li>iterates by all possible operators that exist in {@code
     *         Operators} class</li>
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
    @Override
    String computeArithmeticExpression(final String expression) throws InvalidInputExpressionException {
        String result = expression;

        if (hasFunction(result)) {
            result = computeFunctions(result);
        }

        if (hasEnclosedExpression(expression)) {
            result = openEnclosedExpression(result);
        }

        for (Operators operator : Operators.getOperatorsByPrecedence()) {
            Pattern pattern = Pattern.compile(NUMBER_EXP + "[" + operator.getRegExpRepresentation() + "]" + NUMBER_EXP);

            for (Matcher matcher = pattern.matcher(result); matcher.find(); matcher = pattern.matcher(result)) {
                result = result.replaceAll(NO_MINUS_BEFORE_EXP + Pattern.quote(matcher.group()),
                                            computeBinaryExpression(matcher.group(), operator));
            }
        }

        return result.equals(OPEN_EXP + CLOSE_EXP) ? "" : result;
    }
}