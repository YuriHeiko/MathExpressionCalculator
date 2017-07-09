package com.sysgears.simplecalculator.computer.everythingisfunction2;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Calculates a received math expression. An incoming string cannot
 * contain white spaces. The ideas lie behind the algorithm are next:
 * <p>
 *     <ul>
 *         <li>convert all {@link Operators} into corresponding
 *         {@link Functions}</li>
 *         <li>recursively computes all the functions</li>
 *     </ul>
 * </p>
 */
@SuppressWarnings("Duplicates")
public class FunctionComputer implements Computer {
    /**
     * A pattern for the opening of a parentheses expression.
     * Cannot contain more than one symbol
     */
    static final String OPEN_EXP = "(";

    /**
     * A pattern for the closing of a parentheses expression
     * Cannot contain more than one symbol
     */
    static final String CLOSE_EXP = ")";

    /**
     * A pattern for the delimiter of function's arguments
     * Cannot contain more than one symbol
     */
    static final String DELIMITER = ",";

    /**
     * A pattern for a valid number
     */
    private final String NUMBER_EXP = "-?\\d+([.]\\d+)?";

    /**
     * A compiled pattern for all the functions
     */
    private final Pattern FUNCTIONS_PATTERN = Pattern.compile(Functions.getRegExp());

    /**
     * Validates an incoming string. Removes all unnecessary characters.
     * Computes the expression.
     *
     * @param expression The string contains a math expression
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    @Override
    public String compute(final String expression) throws InvalidInputExpressionException {
        if (expression == null) {
            throw new InvalidInputExpressionException("Incoming string cannot be null");
        }

        String result = computeFunction(Operators.convertOperatorsToFunctions(expression));

        if (!(result.isEmpty() || result.matches(NUMBER_EXP))) {
            throw new InvalidInputExpressionException(String.format("Input data is invalid cause " +
                    "the result of calculation: '%s' is not a number.", result));

        } else if (result.equals("-∞") || result.equals("∞")) {
            throw new InvalidInputExpressionException(String.format("Input data is invalid cause " +
                    "the result of calculation is Infinity(%s)", result));
        }

        return result;
    }

    /**
     * Recursively computes all {@code Functions}. Starts from left.
     *
     * @param expression The string contains a math expression with only
     *                   functions
     * @return The string contains the calculated expression
     */
    //TODO reverse + infinity check
    String computeFunction(final String expression) throws InvalidInputExpressionException {
        String result = expression;

        for (Matcher m = FUNCTIONS_PATTERN.matcher(result); m.find(); m = FUNCTIONS_PATTERN.matcher(result)) {
            String arguments = result.substring(m.group().length(), result.length() - 1);

            try {
                Double[] args = Stream.of(arguments.split(Pattern.quote(DELIMITER))).
                                          map(e -> Double.valueOf(computeFunction(Operators.removeEnclosingSymbols(e)))).
                                          collect(Collectors.toList()).
                                          toArray(new Double[0]);

                result = result.replaceAll(Pattern.quote(m.group() + arguments + CLOSE_EXP),
                                            Functions.valueOf(m.group(1).toUpperCase()).calculate(args).toString());

            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                throw new InvalidInputExpressionException(String.format("Input data is invalid cause this part " +
                                                                        "'%s' has a wrong argument", arguments));

            } catch (ArithmeticException e) {
                throw new InvalidInputExpressionException(String.format("Input data is invalid cause this part " +
                                                                        "'%s' tries to divide by zero", arguments));
            }
        }

        return result;
    }

}