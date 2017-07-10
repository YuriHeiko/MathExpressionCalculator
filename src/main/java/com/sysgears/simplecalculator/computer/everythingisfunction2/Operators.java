package com.sysgears.simplecalculator.computer.everythingisfunction2;

import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sysgears.simplecalculator.computer.everythingisfunction2.FunctionComputer.*;

/**
 * Contains allowed math operators and link to the relative functions. Minus
 * must be before plus according to algorithms' logic.
 */
public enum Operators {
    /**
     * A power operator
     */
    POWER("^", Functions.POWER, 50),
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
         * If there is a minus in front of an operand returns the subtract
         * function with the argument as the second one and zero as the
         * first one, i.e.:
         * <p>
         *     -2*1 = multiply(subtract(0,2),1)
         * </p>
         *
         * @param arguments The stream of arguments
         * @return {@code String} contains function
         */
        @Override
        public String getFunction(final Stream<String> arguments) {
            return super.getFunction(arguments.map(s -> s.isEmpty() ? "0" : s));
        }
    },
    /**
     * An add operator
     */
    ADD("+", Functions.SUM, 10) {
        /**
         * If there is a plus in front of an operand returns the sum
         * function with the argument as the second one and zero as the
         * first one.
         * <p>
         *     +2-1 = subtract(sum(0,2),1)
         * </p>
         *
         * @param arguments The stream of arguments
         * @return {@code String} contains function
         */
        @Override
        public String getFunction(final Stream<String> arguments) {
            return super.getFunction(arguments.map(s -> s.isEmpty() ? "0" : s));
        }
    };

    /**
     * The string representation of the operator
     */
    private final String image;

    /**
     * The corresponding function
     */
    private final Functions function;

    /**
     * The math precedence of the operator as higher the value
     * as higher the precedence
     */
    private final Integer precedence;

    /**
     * Constructs an object
     *
     * @param image      The string representation of the operator
     * @param function   The corresponding function
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
     * @param arguments The stream of arguments
     * @return {@code String} contains the function
     */
    String getFunction(final Stream<String> arguments) {
        return arguments.collect(Collectors.joining(DELIMITER, function.getImage() + OPEN_EXP, CLOSE_EXP));
    }

    /**
     * Returns a string representation of the operator
     *
     * @return The string representation of the operator
     */
    String getImage() {
        return image;
    }

    /**
     * Returns a descending sorted by {@code precedence} collection of
     * operators
     *
     * @return The sorted collection of operators
     */
    static Collection<Operators> getOperatorsByPrecedence() {
        return Stream.of(values()).sorted((e1, e2) -> e2.precedence.compareTo(e1.precedence)).collect(Collectors.toList());
    }


    /**
     * Returns constraints string except for this
     *
     * @param closeExp The string contains the close expression representation
     * @return The constraints string
     */
    String getConstraints(final String closeExp) {
        return Stream.of(values()).map(Operators::getImage).filter(e -> !e.equals(image)).
                                             collect(Collectors.joining("", DELIMITER, closeExp));
    }

    /**
     * Builds and returns a string representation of the operator list
     *
     * @return The string with the description of all the operators
     */
    public static String getList() {
        return getOperatorsByPrecedence().stream().map(e -> "\t" + e + "(" + e.getImage() + ")").
                                                    collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * Converts all operators to corresponding functions
     *
     * @param expression The expression string
     * @return The expression string with only functions
     */
    public static String convertToFunctions(final String expression) {
        String result = expression;

        for (Operators operator : getOperatorsByPrecedence()) {
            while (result.contains(operator.image)) {
                try {
                    String operatorExp = operator.getOperatorExpression(result, operator.image);
                    result = result.replace(operatorExp, operator.getFunction(splitByDelimiter(operatorExp, operator.image)));

                } catch (StringIndexOutOfBoundsException e) {
                    throw new InvalidInputExpressionException("Converting operators to functions error " + result + ".");
                }
            }
        }

        return result;
    }

    /**
     * Finds the left and right operands of the expression. Consider the sequence
     * of the same operators as one function, i.e.:
     * <p>
     *     2+2+2 = sum(2,2,2)
     * </p>
     *
     * @param expression The string contains a math expression
     * @param image      The string representation of the operator
     * @return The valid part of the expression
     */
    String getOperatorExpression(final String expression, final String image) {
        StringBuilder builder = new StringBuilder(expression);
        int operatorIndex = builder.indexOf(image);

        int rightIndex = operatorIndex + image.length();
        int rightBound = getBound(builder, rightIndex, OPEN_EXP, CLOSE_EXP);

        int leftIndex = builder.length() - operatorIndex;
        int leftBound = builder.length() - getBound(builder.reverse(), leftIndex, CLOSE_EXP, OPEN_EXP);

        return expression.substring(leftBound, rightBound);
    }

    /**
     * Breaks a string by the delimiter and combines arguments into a {@code
     * Stream}. Considers as one argument everything enclosed between {@code
     * OPEN_EXP} and {@code CLOSE_EXP}
     *
     * @param expression The string contains the expression
     * @param image      The string representation of the operator
     * @return The stream of arguments
     */
    static Stream<String> splitByDelimiter(final String expression, final String image) {
        Stream<String> result;

        if (expression.contains(OPEN_EXP) && expression.contains(image)) {
            List<String> list = new LinkedList<>();
            int leftBound;
            int rightBound;

            for (leftBound = 0, rightBound = 0; rightBound < expression.length(); rightBound++) {
                if (expression.charAt(rightBound) == OPEN_EXP.charAt(0)) {
                    // skip enclosed expression
                    rightBound += getEnclosedExpressionBound(expression.substring(rightBound), OPEN_EXP, CLOSE_EXP, 0);

                } else if (expression.charAt(rightBound) == image.charAt(0)) {
                    list.add(expression.substring(leftBound, rightBound));
                    leftBound = rightBound + 1;
                }
            }
            list.add(expression.substring(leftBound, rightBound));

            result = list.stream();

        } else {
            result = Stream.of(expression.split(Pattern.quote(image)));
        }

        return result;
    }

    /**
     * Finds operands' bound of the incoming expression of the specified operator
     *
     * @param expression    The math expression
     * @param operatorIndex The index of the first occurrence of the operator
     * @param openExp       The string contains the open expression representation
     * @param closeExp      The string contains the close expression representation
     * @return The last or first index of the operand
     */
    int getBound(final CharSequence expression, final int operatorIndex, final String openExp, final String closeExp) {
        int bound = operatorIndex;
        String constraints = getConstraints(closeExp);

        while (bound < expression.length() && constraints.indexOf(expression.charAt(bound)) == -1) {
            if (expression.charAt(bound) == openExp.charAt(0)) {
                bound = getEnclosedExpressionBound(expression, openExp, closeExp, bound);
            }
            bound++;
        }

        return bound;
    }

    /**
     * Finds and returns a right bound of enclosed part of the expression.
     * Searching starts from the left.
     *
     * @param expression The string contains a math expression
     * @param openExp    The string contains the open expression representation
     * @param closeExp   The string contains the close expression representation
     * @param leftBound  The starting point for searching
     * @return The string contains the enclosed expression that can be empty
     */
    static int getEnclosedExpressionBound(final CharSequence expression, final String openExp, final String closeExp,
                                          final int leftBound) {
        int rightBound = leftBound + 1;

        try {
            for (int counter = 1; counter > 0; rightBound++) {
                if (expression.charAt(rightBound) == closeExp.charAt(0)) {
                    counter--;

                } else if (expression.charAt(rightBound) == openExp.charAt(0)) {
                    counter++;
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new InvalidInputExpressionException("Input data is invalid cause this part " + expression + " is wrong.");
        }

        return rightBound - 1;
    }
}