package com.sysgears.simplecalculator.computer.operators;

import com.sysgears.simplecalculator.computer.Computer;
import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Calculates a received math expression according to the {@link Operators}
 * precedence. An incoming string cannot contain white spaces. The ideas lie
 * behind the algorithm are next:
 * <p>
 *     <ul>
 *         <li> recursively computes all the functions</li>
 *         <li>recursively opens all the parentheses by calculating the
 *         enclosed expressions</li>
 *         <li>calculates the remaining parts of the expression according
 *         to operators precedence</li>
 *         <li>all possible operators are stored in {@code Operators}</li>
 *     </ul>
 * </p>
 * Contains common logic and interface contract for computing algorithms
 */
public abstract class OperatorsComputer implements Computer {
    /**
     * A pattern for the opening of a parentheses expression
     * Cannot contain more than one symbol
     */
    final String OPEN_EXP = "(";

    /**
     * A pattern for the closing of a parentheses expression
     * Cannot contain more than one symbol
     */
    final String CLOSE_EXP = ")";

    /**
     * A pattern for the delimiter of function's arguments
     * Cannot contain more than one symbol
     */
    private final String ARGUMENTS_DELIMITER = ",";

    /**
     * A pattern for a '--' only after parentheses
     */
    private final String DOUBLE_MINUS_AFTER_PARENTHESES_EXP = "(?<=[(])--";

    /**
     * A pattern for a valid number
     */
    final String NUMBER_EXP = "-?\\d+([.]\\d+)?";

    /**
     * A part of a pattern for matching an expression without '-' before.
     * It helps replace expressions that don't have a minus before, i.e.
     * expression = 1-1-1-1+1-1    binary one = 1-1   computed one = 0.0
     * and the result after replacement = 0.0-1-1+0.0  (NOT 0.0-0.0+0.0)
     */
    final String NO_MINUS_BEFORE_EXP = "(?<![-])";

    /**
     * A compiled pattern for E-notation numbers
     */
    private final Pattern E_NOTATION_PATTERN = Pattern.compile("\\d+([.,]?\\d+)?[eE]-?\\d+");

    /**
     * A compiled pattern for  functions
     */
    private final Pattern FUNCTIONS_PATTERN = Pattern.compile(Operators.getFunctionsRegExp() + "\\" + OPEN_EXP);

    /**
     * Validates an incoming string. Converts numbers from E-notation to the
     * decimal one. Computes the expression.
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

        String result = computeArithmeticExpression(computeFunctions(convertFromENotation(expression)));

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
     * Finds and computes all functions. Searching starts from the left bound
     * of an expression.
     *
     * @param expression The string contains a math expression. Can be empty
     * @return The string contains the expression with substituted functions'
     * results
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    String computeFunctions(final String expression) throws InvalidInputExpressionException {
        String result = expression;

        for (Matcher matcher = FUNCTIONS_PATTERN.matcher(result); matcher.find();
             matcher = FUNCTIONS_PATTERN.matcher(result)) {
            String enclosedExpression = getEnclosedExpression(result, matcher.group());

            try {
                Double[] functionArguments = Stream.of(splitArgumentsByDelimiter(enclosedExpression)).
                        map(e -> Double.valueOf(computeArithmeticExpression(e))).
                        collect(Collectors.toList()).
                        toArray(new Double[0]);

                Double functionResult = Operators.valueOf(matcher.group(1).toUpperCase()).calculate(functionArguments);

                result = normalizeExpression(
                                    result.replaceAll(Pattern.quote(matcher.group() + enclosedExpression + CLOSE_EXP),
                                                                    convertFromENotation(functionResult.toString())));

            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                throw new InvalidInputExpressionException(String.format("Input data is invalid cause this part " +
                        "'%s' has a wrong argument", enclosedExpression));

            } catch (ArithmeticException e) {
                throw new InvalidInputExpressionException(String.format("Input data is invalid cause this part " +
                        "'%s' tries to divide by zero", enclosedExpression));
            }
        }

        return result;
    }

    /**
     * Breaks an argument string by {@code ARGUMENTS_DELIMITER} and combines
     * arguments into a String array.
     *
     * @param arguments The string contains arguments
     * @return The String array with arguments
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    String[] splitArgumentsByDelimiter(final String arguments) throws InvalidInputExpressionException {
        List<String> list = new LinkedList<>();
        String[] result;

        if (arguments.contains(OPEN_EXP) && arguments.contains(ARGUMENTS_DELIMITER)) {
            int leftBound;
            int rightBound;

            for (leftBound = 0, rightBound = 0; rightBound < arguments.length(); rightBound++) {
                if (arguments.charAt(rightBound) == OPEN_EXP.charAt(0)) {
                    // skip enclosed expression
                    rightBound += getEnclosedExpression(arguments.substring(rightBound), OPEN_EXP).length() + 1;

                } else if (arguments.charAt(rightBound) == ARGUMENTS_DELIMITER.charAt(0)) {
                    list.add(arguments.substring(leftBound, rightBound));
                    leftBound += rightBound + 1;
                }
            }
            list.add(arguments.substring(leftBound, rightBound));

            result = list.toArray(new String[0]);

        } else {
            result = arguments.split(ARGUMENTS_DELIMITER);
        }

        return result;
    }

    /**
     * Finds and returns a part of the expression which is enclosed in
     * parentheses. Searching starts from the left side of the expression.
     *
     * @param expression The string contains a math expression
     * @return The string contains the enclosed expression that can be empty
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    String getEnclosedExpression(final String expression, final String openExp) throws InvalidInputExpressionException {
        int leftBound = expression.indexOf(openExp) + openExp.length();
        int rightBound = leftBound;

        try {
            for (int counter = 1; counter > 0; rightBound++) {
                if (expression.substring(rightBound, rightBound + 1).equals(CLOSE_EXP)) {
                    counter--;

                } else if (expression.substring(rightBound, rightBound + 1).equals(OPEN_EXP)) {
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
     * Checks whether a String contains any function
     *
     * @param expression The expression string
     * @return true if there is a function
     */
    boolean hasFunction(final String expression) {
        return FUNCTIONS_PATTERN.matcher(expression).find();
    }

    /**
     * Finds all parts of the expression which are enclosed in parentheses.
     * Computes such parts and put the value instead of the corresponding
     * parts. Removes parentheses respectively. Computes the remaining
     * expression
     *
     * @param expression The string contains a math expression
     * @return The string contains an expression with open parentheses
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    abstract String openEnclosedExpression(final String expression) throws InvalidInputExpressionException;

    /**
     * Computes the received expression according to the {@code Operators}
     * precedence.
     *
     * @param expression The string contains the math expression
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    abstract String computeArithmeticExpression(final String expression) throws InvalidInputExpressionException;

    /**
     * Checks whether a String contains any expression enclosed within
     * {@code OPEN_EXP} and {@code CLOSE_EXP}
     *
     * @param expression The expression string
     * @return true if there is an enclosed expression
     */
    boolean hasEnclosedExpression(final String expression) {
        return expression.contains(OPEN_EXP);
    }

    /**
     * Computes the binary expression.
     *
     * @param expression The string contains a binary expression
     * @return The string contains the computed value
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    String computeBinaryExpression(final String expression, final Operators operator)
            throws InvalidInputExpressionException {

        String result = expression;

        try {
            String leftOperand = result.substring(0, result.lastIndexOf(operator.getRepresentation()));
            String rightOperand = result.substring(result.lastIndexOf((operator.getRepresentation())) + 1);

            result = Operators.convertFromENotation(
                                operator.calculate(Double.parseDouble(leftOperand), Double.parseDouble(rightOperand)));

        } catch (NumberFormatException | StringIndexOutOfBoundsException | ArithmeticException e) {
            throw new InvalidInputExpressionException(String.format("Input data is invalid because of " +
                    "this part of expression: '%s'", result));
        }

        return result;
    }

    /**
     * Normalizes an expression according to common math rules
     * <p>
     *     <ul>
     *         <li> if there is '--' after '(', it is altered by ''</li>
     *         <li>remaining '--' is altered by '+'</li>
     *         <li>if the string starts with '+', it is altered by ''</li>
     *         <li>finally, all the '+-' are altered by '-'</li>
     *     </ul>
     * </p>
     *
     * @param expression The string contains a math expression
     * @return The normalized expression
     */
    String normalizeExpression(final String expression) {
        return expression.replaceAll(DOUBLE_MINUS_AFTER_PARENTHESES_EXP, "").
                        replaceAll("--", "+").
                        replaceAll("^\\+", "").
                        replaceAll("\\+-|-\\+", "-");
    }

    /**
     * Converts all the numbers in an incoming String which are written in
     * E-notation to the same values in decimal notation.
     *
     * @param expression The string contains a math expression
     * @return The converted string
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    String convertFromENotation(final String expression) throws InvalidInputExpressionException {
        String result = expression;

        try {
            for (Matcher matcher = E_NOTATION_PATTERN.matcher(result); matcher.find();
                 matcher = E_NOTATION_PATTERN.matcher(result)) {

                result = result.replace(matcher.group(),
                                        Operators.convertFromENotation(Double.parseDouble(matcher.group())));
            }

        } catch (NumberFormatException e) {
            throw new InvalidInputExpressionException("Input data is invalid. Some numbers cannot be converted from " +
                                                        "E-notation");
        }

        return result;
    }
}