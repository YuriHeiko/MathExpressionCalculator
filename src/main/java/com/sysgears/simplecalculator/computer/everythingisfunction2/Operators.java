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
         * @param expression
         * @return {@code String} contains function
         */
        @Override
        public Stream<String> getOperands(final String expression) {
            return Stream.of(expression.split(Pattern.quote(this.image))).map(s -> s.isEmpty() ? "0" : s).
                    collect(Collectors.toList()).toArray(new String[0]);
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



    static final String constraints = Stream.of(Operators.values()).map(Operators::getImage).
                                                        collect(Collectors.joining("", FunctionComputer.DELIMITER, ""));



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
     * @param expression
     * @return {@code String} contains function
     */
    String getFunction(final String expression) {
        return getOperands(expression).collect(Collectors.joining(FunctionComputer.DELIMITER,
                                                                  function.getImage() + FunctionComputer.OPEN_EXP,
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

}