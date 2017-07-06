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
    SUBTRACT("-", Functions.SUBTRACT, 20, true) {
        @Override
        public String getFunction(String[] arguments, String splitter) {
            String[] arguments1 = Stream.of(arguments).map(s -> s.isEmpty() ? "0" : s).
                    collect(Collectors.toList()).toArray(new String[0]);
            return super.getFunction(arguments1, splitter);
        }
    },
    /**
     * An add operator
     */
    ADD("+", Functions.SUM, 10, true);

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
     * Whether it is a functions or binary operator
     */
    private final boolean direction;

    /**
     * Constructs an object
     *
     * @param image The string representation of the operator
     * @param precedence     The math precedence of the operator
     * @param direction      true if it is a function
     */
    Operators(final String image, final Functions function, final int precedence, final boolean direction) {
        this.image = image;
        this.function = function;
        this.precedence = precedence;
        this.direction = direction;
    }

    public String getFunction(final String[] arguments, final String splitter) {
        return Stream.of(arguments).collect(Collectors.joining(splitter, this.function.getRepresentation() + "(", ")"));
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
     * Returns the RegExp string representation of the operator
     *
     * @return The normalized string representation of the operator
     */
    public String getRegExpRepresentation() {
        return Pattern.quote(image);
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
        return getOperatorsByPrecedence().stream().map(e -> "\t" + e + "(" + e.getImage() + ")").
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
}