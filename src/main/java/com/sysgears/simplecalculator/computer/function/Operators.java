package com.sysgears.simplecalculator.computer.function;

import java.util.Collection;
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
    private final Integer precedence;

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
     * Builds and returns a string representation of the operator list
     *
     * @return The string with the description of all the operators
     */
    public static String getList() {
        return getOperatorsByPrecedence().stream().map(e -> "\t" + e + "(" + e.getImage() + ")").
                collect(Collectors.joining(System.lineSeparator()));
    }
}