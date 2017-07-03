package com.sysgears.simplecalculator.computer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Contains allowed math operators and their logic. The precedence of the
 * operators is their order in this class. Minus must be before plus
 * according to algorithms' logic. All the computes heavily rely on {@code
 * Double} type. However, in Java such calculations lead to round-off
 * errors, and this type is constrained by number size. So, it can be
 * changed to {@code BigDecimal} so as to solve problems above.
 * <p>
 *     Parentheses symbols, '--', '+-' and '-+' CANNOT use either as
 *     operators or as a part of an operator
 * </p>
 */
public enum Operators {
    /**
     * A power operator
     */
    POWER("^") {
        @Override
        public Double calculate(final double x, final double y) {
            return convertNegativeZero((x < 0 ? -1 : 1) * Math.pow(x, y));
        }
    },
    /**
     * A divide operator
     */
    DIVIDE("/") {
        @Override
        public Double calculate(final double x, final double y) {
            if (y == 0) {
                throw new ArithmeticException();
            }

            return convertNegativeZero(x / y);
        }
    },
    /**
     * A multiply operator
     */
    MULTIPLY("*") {
        @Override
        public Double calculate(final double x, final double y) {
            return convertNegativeZero(x * y);
        }
    },
    /**
     * A subtract operator
     */
    SUBTRACT("-") {
        @Override
        public Double calculate(final double x, final double y) {
            return convertNegativeZero(x - y);
        }
    },
    /**
     * An add operator
     */
    ADD("+") {
        @Override
        public Double calculate(final double x, final double y) {
            return convertNegativeZero(x + y);
        }
    };

    /**
     * The string representation of the operator
     */
    private String representation;

    /**
     * Contains the calculating logic of the operator
     *
     * @param x The left operand
     * @param y The right operand
     * @return The computed value
     * @throws ArithmeticException If an arithmetic error is happen
     */
    public abstract Double calculate(final double x, final double y) throws ArithmeticException;

    /**
     * Constructs an object
     *
     * @param representation The string representation of the operator
     */
    Operators(String representation) {
        this.representation = representation;
    }

    /**
     * Builds a RegExp string contains all the operators
     *
     * @return The RegExp string contains all the operators
     */
    static String getRegExp() {
        return "[" + Stream.of(values()).map(Operators::getRegExpRepresentation).
                collect(Collectors.joining()) + "]";
    }

    /**
     * This function convert a value to +0.0, if it is equal
     * to -0.0 so as to obtain a predictable behaviour of compare functions
     *
     * @param value a value to convert
     * @return the same or converted value
     */
    static double convertNegativeZero(double value) {
        if (value == 0.0) {
            value = 0.0;  // convert -0.0 to +0.0
        }

        return value;
    }

    /**
     * This function convert value to String frome E-notation to normal
     * one, i.e. Input: 10E-5   Output: 0.00001
     *
     * @param value the value to convert
     * @return String contains the converted value in decimal notation
     */
    static String convertFromENotation(double value) {
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(340); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS

        return df.format(value);
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
     * Builds and returns a string representation of the operator list
     *
     * @return The string with the description of all the operators
     */
    public static String getList() {
        return Stream.of(values()).
                map(e -> "\t" + e + "(" + e.getRepresentation() + ")").
                collect(Collectors.joining(System.lineSeparator()));
    }
}