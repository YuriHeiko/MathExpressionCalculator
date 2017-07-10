package com.sysgears.simplecalculator.computer.everythingisfunction2;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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
         * If there is a minus in front of an operand returns the subtract
         * function with the argument as the second one and zero as the
         * first one.
         *
         * @param arguments
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
     * @param arguments
     * @return {@code String} contains function
     */
    String getFunction(final Stream<String> arguments) {
        return arguments.collect(Collectors.joining(FunctionComputer.DELIMITER,function.getImage() + FunctionComputer.OPEN_EXP,
                                                    FunctionComputer.CLOSE_EXP));
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



    String getConstraints(final String closeExp) {
        return Stream.of(Operators.values()).map(Operators::getImage).filter(e -> !e.equals(image)).
                collect(Collectors.joining("", FunctionComputer.DELIMITER, closeExp));
    }


    static String convertToFunctions(final String expression) {
        String result = expression;

        for (Operators operator : getOperatorsByPrecedence()) {
            String image = operator.image;
            while (result.contains(image)) {
                String operatorExp = operator.getOperatorExpression(result, image);
                result = result.replace(operatorExp, operator.getFunction(operator.getOperands(operatorExp, image)));
            }
        }

        return result;
    }

    /**
     * Finds the left and right operands of the expression.
     *
     * @param expression The string contains a math expression
     * @return The valid part of the expression
     */
    String getOperatorExpression(final String expression, final String image) {
        StringBuilder builder = new StringBuilder(expression);
        int operatorIndex = builder.indexOf(image);

        int rightIndex = operatorIndex + image.length();
        int rightBound = getBound(builder, rightIndex, FunctionComputer.OPEN_EXP, FunctionComputer.CLOSE_EXP);

        int leftIndex = builder.length() - operatorIndex;
        int leftBound = builder.length() - getBound(builder.reverse(), leftIndex, FunctionComputer.CLOSE_EXP, FunctionComputer.OPEN_EXP);

        return expression.substring(leftBound, rightBound);
    }

    /**
     * Breaks an argument string by delimiter and combines arguments into a
     * String array.
     *
     * @param expression The string contains the expression
     * @return The String array with arguments
     */
    Stream<String> getOperands(final String expression, final String image) {
        Stream<String> result;

        if (expression.contains(FunctionComputer.OPEN_EXP) && expression.contains(image)) {
            List<String> list = new LinkedList<>();
            int leftBound;
            int rightBound;

            for (leftBound = 0, rightBound = 0; rightBound < expression.length(); rightBound++) {
                if (expression.charAt(rightBound) == FunctionComputer.OPEN_EXP.charAt(0)) {
                    // skip enclosed expression
                    rightBound += getEnclosedExpressionBound(expression.substring(rightBound),
                            FunctionComputer.OPEN_EXP, FunctionComputer.CLOSE_EXP, 0);

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
     * Finds a bound of operand of the incoming expression. The side of the operand is
     * set by {@code direction}
     *
     * @param expression The math expression
     * @param operatorIndex
     * @return The last or first index of the operand
     */
    int getBound(final CharSequence expression, final int operatorIndex, final String openExp, String closeExp) {
        int bound = operatorIndex;
        String constraints = getConstraints(closeExp);

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
     */
    int getEnclosedExpressionBound(final CharSequence expression, final String openExp, final String closeExp,
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
    String removeEnclosingSymbols(final String expression) {
        String result = expression;

        while (result.charAt(0) == FunctionComputer.OPEN_EXP.charAt(0)) {
            int endIndex = getEnclosedExpressionBound(result, FunctionComputer.OPEN_EXP, FunctionComputer.CLOSE_EXP, 0);
            result = result.substring(1, endIndex) + result.substring(endIndex + 1, result.length());
        }

        return result;
    }
}