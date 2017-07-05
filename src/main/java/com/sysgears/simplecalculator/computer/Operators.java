package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.*;

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
     * A square root function
     */
    SQRT("sqrt", 1020, true, 1) {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            super.calculate(arguments);

            return convertNegativeZero(sqrt(arguments[0]));
        }
    },
    /**
     * A cosines function
     */
    SIN("sin", 1015, true, 1) {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            super.calculate(arguments);

            return convertNegativeZero(sin(arguments[0]));
        }
    },
    /**
     * A cosines function
     */
    COS("cos", 1010, true, 1) {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            super.calculate(arguments);

            return convertNegativeZero(cos(arguments[0]));
        }
    },
    /**
     * A power function
     */
    POW("pow", 1000, true, 2) {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            super.calculate(arguments);

            return convertNegativeZero((arguments[0] < 0 ? -1 : 1) * Math.pow(arguments[0], arguments[1]));
        }
    },
    /**
     * A power operator
     */
    POWER("^", 50, false, 2) {
        @Override
        public Double calculate(final Double... operands) throws InvalidInputExpressionException {
            super.calculate(operands);

            return convertNegativeZero((operands[0] < 0 ? -1 : 1) * Math.pow(operands[0], operands[1]));
        }
    },
    /**
     * A divide operator
     */
    DIVIDE("/", 40, false, 2) {
        @Override
        public Double calculate(final Double... operands) throws ArithmeticException, InvalidInputExpressionException {
            super.calculate(operands);

            if (operands[1] == 0) {
                throw new ArithmeticException();
            }

            return convertNegativeZero(operands[0] / operands[1]);
        }
    },
    /**
     * A multiply operator
     */
    MULTIPLY("*", 30, false, 2) {
        @Override
        public Double calculate(final Double... operands) throws InvalidInputExpressionException {
            super.calculate(operands);

            return convertNegativeZero(operands[0] * operands[1]);
        }
    },
    /**
     * A subtract operator
     */
    SUBTRACT("-", 20, false, 2) {
        @Override
        public Double calculate(final Double... operands) throws InvalidInputExpressionException {
            super.calculate(operands);

            return convertNegativeZero(operands[0] - operands[1]);
        }
    },
    /**
     * An add operator
     */
    ADD("+", 10, false, 2) {
        @Override
        public Double calculate(final Double... operands) throws InvalidInputExpressionException {
            super.calculate(operands);

            return convertNegativeZero(operands[0] + operands[1]);
        }
    };

    /**
     * The string representation of the operator
     */
    final private String representation;

    /**
     * The math precedence of the operator
     */
    final private Integer precedence;

    /**
     * Whether it is a functions or binary operator
     */
    final boolean isFunction;

    /**
     * Whether it is a functions or binary operator
     */
    final int argumentsNumber;

    /**
     * Constructs an object
     *
     * @param representation The string representation of the operator
     * @param precedence     The math precedence of the operator
     * @param isFunction     true if it is a function
     */
    Operators(final String representation, final int precedence, final boolean isFunction, final int argumentsNumber) {
        this.representation = representation;
        this.precedence = precedence;
        this.isFunction = isFunction;
        this.argumentsNumber = argumentsNumber;
    }

    /**
     * Contains the calculating logic of the operator. Checks whether
     * number of arguments is right.
     *
     * @param arguments The arguments
     * @return The computed value
     * @throws ArithmeticException If an arithmetic error is happen
     */
    public Double calculate(final Double... arguments) throws ArithmeticException, InvalidInputExpressionException {
        if (this.argumentsNumber != arguments.length) {
            throw new InvalidInputExpressionException(arguments.length + " instead of " + this.argumentsNumber);
        }

        return 0.0;
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
     * Builds a RegExp string contains all the functions
     *
     * @return The RegExp string contains all the functions
     */
    static String getFunctionsRegExp(final String OPEN_EXP) {
        return getFunctionsByPrecedence().stream().filter(e -> e.isFunction).
                map(Operators::getRepresentation).
                collect(Collectors.joining("|", "(", ")"));
    }

    /**
     * Builds a RegExp string contains all the binary operators
     *
     * @return The RegExp string contains all the operators
     */
    static String getOperatorsRegExp() {
        return getOperatorsByPrecedence().stream().filter(e -> !e.isFunction).
                map(Operators::getRegExpRepresentation).
                collect(Collectors.joining("", "[", "]"));
    }

    /**
     * This function convert a value to +0.0, if it is equal to -0.0
     * so as to obtain a predictable behaviour of compare functions
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
     * This function convert value to String from E-notation to the regular
     * one, i.e. Input: 10E-5   Output: 0.00001
     *
     * @param value the value to convert
     * @return String contains the converted value in decimal notation
     */
    public static String convertFromENotation(final double value) {
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(340); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS

        return df.format(value);
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

    /**
     * Returns a descending sorted by {@code precedence} collection of
     * only functions
     *
     * @return The sorted collection of functions
     */
    public static Collection<Operators> getFunctionsByPrecedence() {
        return Arrays.
                stream(values()).
                filter(e -> e.isFunction).
                sorted((e1, e2) -> e2.precedence.compareTo(e1.precedence)).
                collect(Collectors.toList());
    }

    /**
     * Returns a descending sorted by {@code precedence} collection of
     * only binary operators
     *
     * @return The sorted collection of operators
     */
    public static Collection<Operators> getOperatorsByPrecedence() {
        return Arrays.
                stream(values()).
                filter(e -> !e.isFunction).
                sorted((e1, e2) -> e2.precedence.compareTo(e1.precedence)).
                collect(Collectors.toList());
    }
}