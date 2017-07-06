package com.sysgears.simplecalculator.everythingisfunction;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.*;

/**
 * Contains allowed math functions and their logic. All the computes 
 * heavily rely on {@code Double} type. However, it lead to round-off
 * errors, and this type is constrained by number size. So, it can be
 * changed to {@code BigDecimal} so as to solve problems above.
 */
public enum Functions {
    /**
     * A square root function
     */
    SQRT("sqrt", 1) {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            super.calculate(arguments);

            return convertNegativeZero(sqrt(arguments[0]));
        }
    },
    /**
     * A cosines function
     */
    SIN("sin", 1) {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            super.calculate(arguments);

            return convertNegativeZero(sin(arguments[0]));
        }
    },
    /**
     * A cosines function
     */
    COS("cos", 1) {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            super.calculate(arguments);

            return convertNegativeZero(cos(arguments[0]));
        }
    },
    /**
     * A power function
     */
    POW("pow", 2) {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            super.calculate(arguments);

            return convertNegativeZero((arguments[0] < 0 ? -1 : 1) * Math.pow(arguments[0], arguments[1]));
/*
            return convertNegativeZero(IntStream.range(operands.length - 1, 0).
                    mapToDouble(i -> operands[i]).
                    reduce((v1, v2) -> (v1 < 0 ? -1 : 1) * Math.pow(v2, v1)).orElse(0.0));
*/
        }
    },
    /**
     * A divide function
     */
    DIVIDE("div", null) {
        @Override
        public Double calculate(final Double... operands) throws ArithmeticException, InvalidInputExpressionException {
            double result = operands[0];
            for (int i = 1; i < operands.length; i++) {
                if (operands[i] == 0) {
                    throw new ArithmeticException();
                }
                result /= operands[i];
            }

            return convertNegativeZero(result);
        }
    },
    /**
     * A multiply function
     */
    MULTIPLY("mult", null) {
        @Override
        public Double calculate(final Double... operands) throws InvalidInputExpressionException {
            return convertNegativeZero(Arrays.stream(operands).reduce((v1, v2) -> v1 * v2).orElse(0.0));
        }
    },
    /**
     * A sum function
     */
    SUM("sum", null) {
        @Override
        public Double calculate(final Double... operands) throws InvalidInputExpressionException {
            return convertNegativeZero(Arrays.stream(operands).reduce((v1, v2) -> v1 + v2).orElse(0.0));
        }
    };

    /**
     * The string representation of the function
     */
    final private String representation;

    /**
     * The math precedence of the function
     */
    final private Integer argumentsNumber;

    /**
     * Constructs an object
     *
     * @param representation The string representation of the function
     * @param argumentsNumber The number of arguments. 'null' if there
     *                        can be infinity number
     */
    Functions(final String representation, final Integer argumentsNumber) {
        this.representation = representation;
        this.argumentsNumber = argumentsNumber;
    }

    /**
     * Contains the calculating logic of the function. Checks whether
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
     * Returns a string representation of the function
     *
     * @return The string representation of the function
     */
    public String getRepresentation() {
        return representation;
    }

    /**
     * Returns the RegExp string representation of the function
     *
     * @return The normalized string representation of the function
     */
    public String getRegExpRepresentation() {
        return Pattern.quote(representation);
    }

    /**
     * Builds a RegExp string contains all the functions
     *
     * @return The RegExp string contains all the functions
     */
    static String getRegExp() {
        return Stream.of(values()).
                map(Functions::getRegExpRepresentation).
                collect(Collectors.joining("|", "(", ")\\("));
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
     * Builds and returns a string representation of the function list
     *
     * @return The string with the description of all the functions
     */
    public static String getList() {
        return Stream.of(values()).
                map(e -> "\t" + e + "(" + e.getRepresentation() + ")").
                collect(Collectors.joining(System.lineSeparator()));
    }
}