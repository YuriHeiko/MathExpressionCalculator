package com.sysgears.simplecalculator.computer.everythingisfunction2;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
     * Validates an incoming string. Computes the expression.
     *
     * @param expression The string contains a math expression
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    @Override
    public String compute(final String expression) throws InvalidInputExpressionException {
        String result = "";

        if (expression == null) {
            throw new InvalidInputExpressionException("Incoming string cannot be null");

        } else if (!(expression.contains("++") || expression.contains("--"))) {
            throw new InvalidInputExpressionException("Incoming string cannot contain either '++' or '--'");

        } else if (!expression.isEmpty()) {
            result = computeFunction(Operators.convertToFunctions(expression));

            if (!(result.isEmpty() || result.matches(NUMBER_EXP))) {
                throw new InvalidInputExpressionException("Input data is invalid cause the result of calculation: " +
                        result + " is not a number.");

            } else if (result.equals("-∞") || result.equals("∞")) {
                throw new InvalidInputExpressionException("Input data is invalid cause the result of calculation is " +
                        "Infinity." + result);
            }
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
    String computeFunction(final String expression) throws InvalidInputExpressionException {
        String result = removeEnclosingSymbols(expression);

        for (Matcher m = FUNCTIONS_PATTERN.matcher(result); m.find(); m = FUNCTIONS_PATTERN.matcher(result)) {
            String arguments = result.substring(m.group().length(), result.length() - 1);

            try {
                // Take arguments. Compute arguments if they are functions.
                Double[] argsValues = Operators.splitByDelimiter(arguments, DELIMITER).
                                            map(argument -> Double.valueOf(computeFunction(argument))).
                                            collect(Collectors.toList()).
                                            toArray(new Double[0]);

                result = result.replaceAll(Pattern.quote(m.group() + arguments + CLOSE_EXP),
                                            Functions.calculate(m.group(1), argsValues));

            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                throw new InvalidInputExpressionException("Input data is invalid cause this part " + m.group() +
                                    arguments + CLOSE_EXP + " has a wrong argument.", m.group() + arguments + CLOSE_EXP);

            } catch (ArithmeticException e) {
                throw new InvalidInputExpressionException("Input data is invalid cause this part " + m.group() +
                                arguments + CLOSE_EXP + " tries to divide by zero.", m.group() + arguments + CLOSE_EXP);
            }
        }

        return result;
    }

    /**
     * Removes enclosing symbols, i.e. '(cos(2))' => 'cos(2)'
     *
     * @param expression The math expression
     * @return The open expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    String removeEnclosingSymbols(final String expression) throws InvalidInputExpressionException {
        String result = expression;

        if (result.charAt(result.length() - 1) != CLOSE_EXP.charAt(0)) {
            throw new InvalidInputExpressionException("Input data is invalid cause this part " + result +
                    " does not have the closing symbol: '" + CLOSE_EXP + "'.", result);
        }

        while (result.charAt(0) == OPEN_EXP.charAt(0)) {
            int endIndex = Operators.getEnclosedExpressionBound(result, OPEN_EXP, CLOSE_EXP, 0);
            result = result.substring(1, endIndex) + result.substring(endIndex + 1, result.length());
        }

        return result;
    }
}