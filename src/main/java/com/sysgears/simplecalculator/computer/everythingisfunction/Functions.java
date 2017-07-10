package com.sysgears.simplecalculator.computer.everythingisfunction;

import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

            return convertNegativeZero(Math.sqrt(arguments[0]));
        }
    },
    /**
     * A sinuses function
     */
    SIN("sin", 1) {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            super.calculate(arguments);

            return convertNegativeZero(Math.sin(arguments[0]));
        }
    },
    /**
     * A cosines function
     */
    COS("cos", 1) {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            super.calculate(arguments);

            return convertNegativeZero(Math.cos(arguments[0]));
        }
    },
    /**
     * A power function
     */
    POW("pow") {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            super.calculate(arguments);

            return convertNegativeZero(IntStream.range(1 - arguments.length, 1).mapToDouble(i -> arguments[-i]).
                                                 reduce((v1, v2) -> (v1 < 0 ? -1 : 1) * Math.pow(v2, v1)).orElse(0.0));
        }
    },
    /**
     * A divide function
     */
    DIVIDE("divide") {
        @Override
        public Double calculate(final Double... arguments) throws ArithmeticException, InvalidInputExpressionException {
            return convertNegativeZero(Stream.of(arguments).reduce((v1, v2) -> v1 / (v2 == 0 ? 0 / 0 : v2)).orElse(0.0));
        }
    },
    /**
     * A multiply function
     */
    MULTIPLY("multiply") {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            return convertNegativeZero(Stream.of(arguments).reduce((v1, v2) -> v1 * v2).orElse(0.0));
        }
    },
    /**
     * A subtract function
     */
    SUBTRACT("subtract") {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            return convertNegativeZero(Stream.of(arguments).reduce((v1, v2) -> v1 - v2).orElse(0.0));
        }
    },
    /**
     * A sum function
     */
    SUM("sum") {
        @Override
        public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
            return convertNegativeZero(Stream.of(arguments).reduce((v1, v2) -> v1 + v2).orElse(0.0));
        }
    };

    /**
     * The string representation of the function
     */
    final String image;

    /**
     * The math precedence of the function
     */
    private final Integer argumentsNumber;

    /**
     * Constructs an object with virtually endless number of arguments
     *
     * @param image           The string representation of the function
     */
    Functions(final String image) {
        this(image, null);
    }

    /**
     * Constructs an object
     *
     * @param image           The string representation of the function
     * @param argumentsNumber The number of arguments. 'null' if there
     *                        can be infinity number
     */
    Functions(final String image, final Integer argumentsNumber) {
        this.image = image;
        this.argumentsNumber = argumentsNumber;
    }

    /**
     * Contains the calculating logic of the function. Checks whether
     * number of arguments is right.
     *
     * @param arguments The arguments
     * @return The computed value
     * @throws ArithmeticException             If an arithmetic error is happen
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    public Double calculate(final Double... arguments) throws InvalidInputExpressionException {
        if (argumentsNumber != null && argumentsNumber != arguments.length) {
            throw new InvalidInputExpressionException("Input data is invalid cause this part cause the function " +
                    this + " contains " + arguments.length + " arguments instead of " + this.argumentsNumber);
        }

        return 0.0;
    }

    /**
     * Calculates a function
     *
     * @param functions The string representation of the function
     * @param arguments The function arguments
     * @return The string contains the computed value
     * @throws ArithmeticException             If an arithmetic error is happen
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    public static String calculate(final String functions, Double... arguments) throws InvalidInputExpressionException,
            ArithmeticException {

        return valueOf(functions.toUpperCase()).calculate(arguments).toString();
    }

    /**
     * Returns a string representation of the function
     *
     * @return The string representation of the function
     */
    public String getImage() {
        return image;
    }

    /**
     * Builds a RegExp string contains all the functions
     *
     * @return The RegExp string contains all the functions
     */
    static String getRegExp() {
        return Stream.of(values()).map(v -> Pattern.quote(v.image)).collect(Collectors.joining("|", "(", ")\\("));
    }

    /**
     * This function convert a value to +0.0, if it is equal to -0.0
     * so as to obtain a predictable behaviour of compare functions
     *
     * @param value a value to convert
     * @return the same or converted value
     */
    static double convertNegativeZero(double value) {
        return value + 0.0;
    }

    /**
     * Builds and returns a string representation of the functions list
     *
     * @return The string with the description of all the operators
     */
    public static String getList() {
        return Stream.of(values()).map(e -> "\t" + e.getImage() + "()").collect(Collectors.joining(System.lineSeparator()));
    }
}