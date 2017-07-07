package com.sysgears.simplecalculator.computer.everythingisfunction;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;

import java.util.LinkedList;
import java.util.List;
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
public class FunctionComputer implements Computer {
    /**
     * A pattern for the opening of a parentheses expression.
     * Cannot contain more than one symbol
     */
    private final String OPEN_EXP = "(";

    /**
     * A pattern for the closing of a parentheses expression
     * Cannot contain more than one symbol
     */
    private final String CLOSE_EXP = ")";

    /**
     * A pattern for the delimiter of function's arguments
     * Cannot contain more than one symbol
     */
    private final String ARGUMENTS_DELIMITER = ",";

    /**
     * A pattern for a valid number
     */
    private final String NUMBER_EXP = "-?\\d+([.]\\d+)?";

    /**
     * A constant for the left direction
     */
    private final int LEFT = -1;

    /**
     * A constant for the right direction
     */
    private final int RIGHT = 1;

    /**
     * A compiled pattern for all the functions
     */
    private final Pattern FUNCTIONS_PATTERN = Pattern.compile(Functions.getRegExp());

    /**
     * Validates an incoming string. Removes all unnecessary characters.
     * Computes the expression.
     *
     * @param expression The string contains a math expression. Can be empty
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    @Override
    public String compute(final String expression) throws InvalidInputExpressionException {
        if (expression == null) {
            throw new InvalidInputExpressionException("Incoming string cannot be null");
        }

        String result = computeFunction(convertOperatorsToFunctions(expression));

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
     *                   functions. Can be empty
     * @return The string contains the calculated expression
     */
    String computeFunction(final String expression) throws InvalidInputExpressionException {
        String result = expression;

        for (Matcher m = FUNCTIONS_PATTERN.matcher(result); m.find(); m = FUNCTIONS_PATTERN.matcher(result)) {
            String arguments = result.substring(m.group().length(), result.length() - 1);

            try {
                Double[] args = Stream.of(splitArgumentsByDelimiter(arguments, ARGUMENTS_DELIMITER)).
                                            map(e -> computeFunction(removeEnclosingSymbols(e))).
                                            map(Double::valueOf).
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

    /**
     * Breaks an argument string by delimiter and combines arguments into a
     * String array.
     *
     * @param arguments The string contains arguments
     * @return The String array with arguments
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    String[] splitArgumentsByDelimiter(final String arguments, final String delimiter)
            throws InvalidInputExpressionException {

        List<String> list = new LinkedList<>();
        String[] result;

        if (arguments.contains(OPEN_EXP) && arguments.contains(delimiter)) {
            int leftBound;
            int rightBound;

            for (leftBound = 0, rightBound = 0; rightBound < arguments.length(); rightBound++) {
                if (arguments.charAt(rightBound) == OPEN_EXP.charAt(0)) {
                    // skip enclosed expression
                    rightBound += getEnclosedExpression(arguments.substring(rightBound),
                                                        OPEN_EXP, CLOSE_EXP, 0).length() + 1;

                } else if (arguments.charAt(rightBound) == delimiter.charAt(0)) {
                    list.add(arguments.substring(leftBound, rightBound));
                    leftBound = rightBound + 1;
                }
            }
            list.add(arguments.substring(leftBound, rightBound));

            result = list.toArray(new String[0]);

        } else {
            result = arguments.split(Pattern.quote(delimiter));
        }

        return result;
    }

    /**
     * Replaces all the {@code Operators} by their corresponding functions
     * according to Operator's precedence.
     *
     * @param expression The string contains a math expression. Can be empty
     * @return The string contains a math expression with only functions
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    String convertOperatorsToFunctions(final String expression) throws InvalidInputExpressionException {
        String result = expression;

        for (Operators operator : Operators.getOperatorsByPrecedence()) {
            while (result.contains(operator.getImage())) {
                result = removeEnclosingSymbols(result);
                String rawArguments = result.substring(getBound(result, operator, LEFT),
                                                result.indexOf(operator.getImage()) + getBound(result, operator, RIGHT));
                String function = operator.getFunction(splitArgumentsByDelimiter(rawArguments, operator.getImage()),
                                                        ARGUMENTS_DELIMITER);
                result = result.replace(rawArguments, function);
            }
        }

        return result;
    }

    /**
     * Finds a bound of operand of the incoming expression. The side of the operand is
     * set by {@code direction}
     *
     * @param expression The math expression
     * @param operator The math operator
     * @param direction The search direction
     * @return The last or first index of the operand
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    int getBound(final String expression, final Operators operator, final int direction)
            throws InvalidInputExpressionException {

        int start = expression.indexOf(operator.getImage());
        StringBuilder result = new StringBuilder(expression.substring((direction == RIGHT ? start + 1 : 0),
                                                                   (direction == RIGHT ? expression.length() : start)));
        result = (direction == RIGHT ? result : result.reverse());

        String openExp = (direction == RIGHT ? OPEN_EXP : CLOSE_EXP);
        String closeExp = (direction == RIGHT ? CLOSE_EXP : OPEN_EXP);
        String constraints = Stream.of(Operators.values()).
                                    filter(v -> v != operator).
                                    map(Operators::getImage).
                                    collect(Collectors.joining("", closeExp, ARGUMENTS_DELIMITER));

        int end;
        for (end = 0; end < result.length() && constraints.indexOf(result.charAt(end)) == -1; end++) {
            if (result.charAt(end) == openExp.charAt(0)) {
                // skip enclosed expression
                end += getEnclosedExpression(result.toString(), openExp, closeExp, end).length() + 1;
            }
        }

        if (direction != RIGHT) {
            end = result.length() - end;
        } else {
            end++;
        }

        return end;
    }

    /**
     * Finds and returns an enclosed part of the expression. Searching starts
     * from the left side of the expression.
     *
     * @param expression The string contains a math expression
     * @param openExp    The string contains the open expression representation
     * @param closeExp   The string contains the close expression representation
     * @param start      The starting point for searching
     * @return The string contains the enclosed expression that can be empty
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    String getEnclosedExpression(final String expression, final String openExp, final String closeExp, final int start)
            throws InvalidInputExpressionException {

        int leftBound = expression.indexOf(openExp, start) + openExp.length();
        int rightBound = leftBound;

        try {
            for (int counter = 1; counter > 0; rightBound++) {
                if (expression.substring(rightBound, rightBound + 1).equals(closeExp)) {
                    counter--;

                } else if (expression.substring(rightBound, rightBound + 1).equals(openExp)) {
                    counter++;
                }
            }

        } catch (StringIndexOutOfBoundsException e) {
            throw new InvalidInputExpressionException(String.format("Input data is invalid because of " +
                                                                    "this part of expression: '%s'", expression));
        }

        return expression.substring(leftBound, rightBound - 1);
    }

    /**
     * Removes enclosing symbols, i.e. '(cos(2))' => 'cos(2)'
     *
     * @param expression The math expression
     * @return The open expression
     */
    String removeEnclosingSymbols(final String expression) {
        String result = expression;

        while (result.charAt(0) == OPEN_EXP.charAt(0)) {
            int endIndex = getEnclosedExpression(result, OPEN_EXP, CLOSE_EXP, 0).length() + 1;
            result = result.substring(1, endIndex) + result.substring(endIndex + 1, result.length());
        }

        return result;
    }
}