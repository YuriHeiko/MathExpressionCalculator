package com.sysgears.simplecalculator.everythingisfunction;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

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
 * <ul>
 * <li> recursively computes all the functions</li>
 * <li>recursively opens all the parentheses by calculating the
 * enclosed expressions</li>
 * <li>calculates the remaining parts of the expression according
 * to operators precedence</li>
 * <li>all possible operators are stored in {@code Operators}</li>
 * </ul>
 * </p>
 * Contains common logic and interface contract for computing algorithms
 */
public class Computer {
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
    private final Pattern FUNCTIONS_PATTERN = Pattern.compile(Functions.getRegExp());



    private final int LEFT = -1;

    private final int RIGHT = 1;



    /**
     * A pattern for an expression enclosed within {@code OPEN_EXP} and
     * {@code CLOSE_EXP}
     */
    final Pattern ENCLOSED_EXP_PATTERN = Pattern.compile("\\" + OPEN_EXP + "(" + NUMBER_EXP + "|(" + NUMBER_EXP +
                                                    Operators.getRegExp() + ")+" + NUMBER_EXP + ")" + "\\" + CLOSE_EXP);

    /**
     * Validates an incoming string. Removes all unnecessary characters.
     * Replaces all ',' by '.' and '()' by ''. Converts numbers from
     * E-notation to the decimal one. Computes the expression.
     *
     * @param expression The string contains a math expression. Can be empty
     * @return The string contains the calculated expression
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    public String compute(final String expression) throws InvalidInputExpressionException {
        if (expression == null) {
            throw new InvalidInputExpressionException("Incoming string cannot be null");
        }

        String result = changeOperatorsToFunctions(convertFromENotation(expression));
        result = computeFunctions(result);

        if (!(result.isEmpty() || result.matches(NUMBER_EXP)) && !(result.equals("-∞") || result.equals("∞"))) {
            throw new InvalidInputExpressionException(String.format("Input data is invalid cause " +
                    "the result of calculation: '%s' is not a number.", result));
        }

        return result;
    }

    /**
     * Replaces all the {@link Operators} by this corresponding functions
     * according to Operator's precedence.
     *
     * @param expression The string contains a math expression. Can be empty
     * @return The string contains a math expression with only functions
     */
    String changeOperatorsToFunctions(final String expression) {
        String result = expression;

        for (Operators operator : Operators.getOperatorsByPrecedence()) {
            while (result.contains(operator.getImage())) {
                String rawArguments = result.substring(getBound(result, operator, LEFT),
                                            result.indexOf(operator.getImage()) + getBound(result, operator, RIGHT));

                if (hasEnclosedExpression(rawArguments)) {
                    rawArguments = openEnclosedExpression(rawArguments);
                }

                String function = operator.getFunction(splitArgumentsByDelimiter(rawArguments,operator.getImage()),
                                                        ARGUMENTS_DELIMITER);

                result = result.replace(rawArguments, function);
            }
        }

        return result;
    }

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
     * Breaks an argument string by {@code ARGUMENTS_DELIMITER} and combines
     * arguments into a String array.
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
                    rightBound +=
                            getEnclosedExpression(arguments.substring(rightBound), OPEN_EXP, CLOSE_EXP).length() + 1;

                } else if (arguments.charAt(rightBound) == delimiter.charAt(0)) {
                    list.add(arguments.substring(leftBound, rightBound));
                    leftBound += rightBound + 1;
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
     *
     * @param expression
     * @param operator
     * @param direction
     * @return
     * @throws InvalidInputExpressionException
     */
    int getBound(final String expression, final Operators operator, final int direction)
            throws InvalidInputExpressionException {

        int start = expression.indexOf(operator.getImage());
        StringBuilder result = new StringBuilder(expression.substring((direction == RIGHT ? start + 1 : 0), 
                                                                    (direction == RIGHT ? expression.length() : start)));
        result = (direction == RIGHT ? result : result.reverse());
        
        String openExp = (direction == RIGHT ? OPEN_EXP : CLOSE_EXP);
        String closeExp = (direction == RIGHT ? CLOSE_EXP : OPEN_EXP);
        String constraints = Stream.of(Operators.values()).filter(v -> v != operator).map(Operators::getImage).
                            collect(Collectors.joining("", closeExp, ARGUMENTS_DELIMITER));

        int end;
        for (end = 0; end < result.length() && constraints.indexOf(result.charAt(end)) == -1; end++) {
            if (result.charAt(end) == openExp.charAt(0)) {
                // skip enclosed expression
                end += getEnclosedExpression(result.toString(), openExp, closeExp).length() + 1;
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
     * @return The string contains the enclosed expression that can be empty
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    String getEnclosedExpression(final String expression, final String openExp, final String closeExp)
            throws InvalidInputExpressionException {

        int leftBound = expression.indexOf(openExp) + openExp.length();
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

        for (Matcher matcher = ENCLOSED_EXP_PATTERN.matcher(result); matcher.find();){
//             matcher = ENCLOSED_EXP_PATTERN.matcher(result)) {

            result = result.replace(matcher.group(1), changeOperatorsToFunctions(matcher.group(1)));
        }

        return result;
    }

    /**
     * Compute all functions.
     *
     * @param expression The string contains a math expression with only
     *                   functions. Can be empty
     * @return The string contains the calculated expression
     */
    String computeFunctions(final String expression) {
        String result = expression;

/*
        for (Matcher matcher = FUNCTIONS_PATTERN.matcher(result); matcher.find();
             matcher = FUNCTIONS_PATTERN.matcher(result)) {

            String enclosedExpression = result.substring(matcher.group().length() + 1, result.length() - 2);

            Double[] functionArguments = Stream.of(splitArgumentsByDelimiter(enclosedExpression)).
                    map(e -> Double.valueOf(computeArithmeticExpression(e))).
                    collect(Collectors.toList()).
                    toArray(new Double[0]);
            try {
                Double functionResult = Operators.valueOf(matcher.group(1).toUpperCase()).calculate(functionArguments);

                result = normalizeExpression(
                        result.replaceAll(Pattern.quote(matcher.group() + enclosedExpression + CLOSE_EXP),
                                convertFromENotation(functionResult.toString())));

            } catch (InvalidInputExpressionException e) {
                throw new InvalidInputExpressionException(String.format("Input data is invalid cause " +
                        "the function: '%s' has %s", matcher.group() + enclosedExpression + CLOSE_EXP, e.getMessage()));
            }
        }
*/

        return result;
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
                        Functions.convertFromENotation(Double.parseDouble(matcher.group())));
            }

        } catch (NumberFormatException e) {
            throw new InvalidInputExpressionException("Input data is invalid. Some numbers cannot be converted from " +
                    "E-notation");
        }

        return result;
    }
}