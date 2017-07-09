package com.sysgears.simplecalculator.computer.everythingisfunction2;

import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;

import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains allowed math operators and link to the relative functions. Minus
 * must be before plus according to algorithms' logic.
 */
public enum Operators {
    /**
     * A power operator
     */
    // TODO remove power from operators cause it has reverse computing order
    POWER("^", Functions.POW, 50),
    /**
     * A divide operator
     */
    DIVIDE("/", Functions.DIVIDE, 40),
    /**
     * A multiply operator
     */
    MULTIPLY("*", Functions.MULTIPLY, 30),
    /**
     * A subtract operator
     */
    SUBTRACT("-", Functions.SUBTRACT, 20) {
        /**
         * If there is a minus in front of argument returns the subtract
         * function with the argument as the second argument and zero as
         * the first argument.
         *
         * @param arguments The {@code String} array of arguments
         * @param splitter Arguments' delimiter
         * @return {@code String} contains function
         */
        @Override
        public String getFunction(String[] arguments, String splitter) {
            return super.getFunction(Stream.of(arguments).map(s -> s.isEmpty() ? "0" : s).
                                            collect(Collectors.toList()).toArray(new String[0]), splitter);
        }
    },
    /**
     * An add operator
     */
    ADD("+", Functions.SUM, 10);

    /**
     * The string representation of the operator
     */
    private final String image;

    /**
     * The corresponding function
     */
    private final Functions function;

    /**
     * The math precedence of the operator
     */
    final Integer precedence;

    /**
     * Constructs an object
     *
     * @param image      The string representation of the operator
     * @param precedence The math precedence of the operator
     */
    Operators(final String image, final Functions function, final int precedence) {
        this.image = image;
        this.function = function;
        this.precedence = precedence;
    }

    /**
     * Returns the corresponding function for this object
     *
     * @param arguments The {@code String} array of arguments
     * @param splitter  Arguments' delimiter
     * @return {@code String} contains function
     */
    public String getFunction(final String[] arguments, final String splitter) {
        return Stream.of(arguments).collect(Collectors.joining(splitter, this.function.getImage() + "(", ")"));
    }

    /**
     * Returns a string representation of the operator
     *
     * @return The string representation of the operator
     */
    public String getImage() {
        return image;
    }

    /**
     * Returns a descending sorted by {@code precedence} collection of
     * operators
     *
     * @return The sorted collection of operators
     */
    public static Collection<Operators> getOperatorsByPrecedence() {
        return Stream.of(values()).sorted((e1, e2) -> e2.precedence.compareTo(e1.precedence)).collect(Collectors.toList());
    }

    /**
     * Replaces all the {@code Operators}' expressions by their corresponding
     * functions according to Operators' precedence.
     *
     * @param expression The string contains a math expression
     * @return The string contains a math expression with only functions
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    static String convertOperatorsToFunctions(final String expression) throws InvalidInputExpressionException {
        String result = removeEnclosingSymbols(expression);

        for (Operators operator : Operators.getOperatorsByPrecedence()) {
            String image = operator.getImage();
            while (result.contains(image)) {
                String exp = getOperatorExpression(result, image);
                result = result.replace(exp, operator.getFunction(exp.split(Pattern.quote(image)), FunctionComputer.DELIMITER));
            }
        }

        return result;
    }

    /**
     * Finds the left and right operands of the expression.
     *
     * @param expression The string contains a math expression
     * @param operatorImg The string contains a representation of the operator
     * @return The valid part of the expression
     */
    static String getOperatorExpression(final String expression, final String operatorImg) {
        StringBuilder builder = new StringBuilder(expression);
        int operatorIndex = builder.indexOf(operatorImg);

        int rightIndex = operatorIndex + operatorImg.length();
        int rightBound = getBound(builder, rightIndex, FunctionComputer.OPEN_EXP, FunctionComputer.CLOSE_EXP);

        int leftIndex = builder.length() - operatorIndex;
        int leftBound = builder.length() - getBound(builder.reverse(), leftIndex, FunctionComputer.CLOSE_EXP, FunctionComputer.OPEN_EXP);

        return expression.substring(leftBound, rightBound);
    }

    /**
     * Finds a bound of operand of the incoming expression. The side of the operand is
     * set by {@code direction}
     *
     * @param expression The math expression
     * @param operatorIndex
     * @return The last or first index of the operand
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    static int getBound(final CharSequence expression, final int operatorIndex, final String openExp, String closeExp)
            throws InvalidInputExpressionException {

        String constraints = Stream.of(Operators.values()).map(Operators::getImage).
                collect(Collectors.joining("", FunctionComputer.DELIMITER, closeExp));

        int bound = operatorIndex;
        while (bound < expression.length() && constraints.indexOf(expression.charAt(bound)) == -1) {
            if (expression.charAt(bound) == openExp.charAt(0)) {
                // skip enclosed expression
                bound = getEnclosedExpressionBound(expression, openExp, closeExp, bound);
            }
            bound++;
        }

        return bound;
    }

    /**
     * Finds and returns an enclosed part of the expression. Searching starts
     * from the left side of the expression.
     *
     * @param expression The string contains a math expression
     * @param openExp    The string contains the open expression representation
     * @param closeExp   The string contains the close expression representation
     * @param leftBound      The starting point for searching
     * @return The string contains the enclosed expression that can be empty
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format, or it is null
     */
    static int getEnclosedExpressionBound(final CharSequence expression, final String openExp, final String closeExp,
                                   final int leftBound) {
        int rightBound = leftBound + 1;

        for (int counter = 1; counter > 0; rightBound++) {
            if (expression.charAt(rightBound) == closeExp.charAt(0)) {
                counter--;

            } else if (expression.charAt(rightBound) == openExp.charAt(0)) {
                counter++;
            }
        }

        return rightBound - 1;
    }

    /**
     * Removes enclosing symbols, i.e. '(cos(2))' => 'cos(2)'
     *
     * @param expression The math expression
     * @return The open expression
     */
    static String removeEnclosingSymbols(final String expression) {
        String result = expression;

        while (result.charAt(0) == FunctionComputer.OPEN_EXP.charAt(0)) {
            int endIndex = getEnclosedExpressionBound(result, FunctionComputer.OPEN_EXP, FunctionComputer.CLOSE_EXP, 0);
            result = result.substring(1, endIndex) + result.substring(endIndex + 1, result.length());
        }

        return result;
    }

}