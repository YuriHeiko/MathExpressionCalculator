package com.sysgears.simplecalculator.everythingisfunction;

import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains allowed math operators and the relative function. Minus must be before plus
 * according to algorithms' logic.
 * <p>
 *     Parentheses symbols, '--', '+-' and '-+' CANNOT use either as
 *     operators or as a part of an operator
 * </p>
 */
public enum Operators {
    /**
     * A power operator
     */
    POWER("^", Functions.POW, 50, false),
    /**
     * A divide operator
     */
    DIVIDE("/", Functions.DIVIDE, 40, true),
    /**
     * A multiply operator
     */
    MULTIPLY("*", Functions.MULTIPLY, 30, true),
    /**
     * A subtract operator
     */
    SUBTRACT("-", Functions.SUM, 20, true),
    /**
     * An add operator
     */
    ADD("+", Functions.SUM, 10, true);

    /**
     * The string representation of the operator
     */
    private final String representation;

    /**
     * The string representation of the operator
     */
    private final Functions function;

    /**
     * The math precedence of the operator
     */
    final Integer precedence;

    /**
     * Whether it is a functions or binary operator
     */
    private final boolean direction;

    /**
     * Constructs an object
     *
     * @param representation The string representation of the operator
     * @param precedence     The math precedence of the operator
     * @param direction      true if it is a function
     */
    Operators(final String representation, final Functions function, final int precedence, final boolean direction) {
        this.representation = representation;
        this.function = function;
        this.precedence = precedence;
        this.direction = direction;
    }

    /**
     * Returns a string representation of the operator
     *
     * @return The string representation of the operator
     */
    public String getRepresentation() {
        return representation;
    }

    /**
     * Returns the RegExp string representation of the operator
     *
     * @return The normalized string representation of the operator
     */
    public String getRegExpRepresentation() {
        return Pattern.quote(representation);
    }

    /**
     * Builds a RegExp string contains all the binary operators
     *
     * @return The RegExp string contains all the operators
     */
    static String getRegExp() {
        return Stream.of(values()).map(Operators::getRegExpRepresentation).collect(Collectors.joining("", "[", "]"));
    }

    /**
     * Builds and returns a string representation of the operator list
     *
     * @return The string with the description of all the operators
     */
    public static String getList() {
        return getOperatorsByPrecedence().stream().map(e -> "\t" + e + "(" + e.getRepresentation() + ")").
                collect(Collectors.joining(System.lineSeparator()));
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

    public static String getFunction(final Operators operator, final String arguments) {
        return Stream.of(arguments.split(operator.getRegExpRepresentation())).
                collect(Collectors.joining("", operator.function.getRepresentation() + "(", ")"));
    }
}