package com.sysgears.simplecalculator.computer.function2;

import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.sysgears.simplecalculator.computer.function2.FunctionComputer.CLOSE_EXP;
import static com.sysgears.simplecalculator.computer.function2.FunctionComputer.OPEN_EXP;

/**
 * Contains allowed math functions and their logic. It also uses {@code
 * Math} functions. All the computes heavily rely on {@code Double} type.
 * However, it lead to round-off errors, and this type is constrained by
 * number size. So, it can be changed to {@code BigDecimal} so as to solve
 * problems above.
 */
public enum Functions {
    /**
     * A power function
     */
    POWER("power") {
        /**
         * Power differentiate from other functions. Since, when it is
         * written in operators notation, it should be calculated from
         * right to left, i.e.:
         * <p>
         *     RIGHT: 2^3^2 = 2^9 = 512
         *     WRONG: 2^3^2 = 8^2 = 64
         * </p>
         *
         * @param arguments The arguments
         * @return The computed value
         * @throws InvalidInputExpressionException If the incoming string has an
         *                                         invalid format
         */
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
            return convertNegativeZero(Stream.of(arguments).reduce((v1, v2) -> v1 / checkZero(v2)).orElse(0.0));
        }

        /**
         * Checks if {@code value} is zero. If it is true throws an
         * exception.
         *
         * @param value The value
         * @return The value
         * @throws ArithmeticException If the value equals zero
         */
        private double checkZero(double value) throws ArithmeticException {
            if (convertNegativeZero(value) == 0) {
                throw new ArithmeticException();
            }

            return value;
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
    private final String image;

    /**
     * The math precedence of the function
     */
    private final Integer argumentsNumber;

    /**
     * The {@code Map} contains all Math class methods
     */
    private static Map<String, List<String>> mathFunctions = getMathClassFunctions();

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
    Double calculate(final Double... arguments) throws InvalidInputExpressionException {
        if (argumentsNumber != null && argumentsNumber != arguments.length) {
            throw new InvalidInputExpressionException("Input data is invalid cause this part cause the function " +
                            this + " contains " + arguments.length + " arguments instead of " + this.argumentsNumber);
        }

        return 0.0;
    }

    /**
     * Creates a map contains all methods from {@code Math} class
     *
     * @return The map contains functions as keys and their arguments as values
     */
    private static Map<String, List<String>> getMathClassFunctions() {
        Map<String, List<String>> map = new TreeMap<>();

        for (Method method : Math.class.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && method.getReturnType() == double.class) {
                map.put(method.getName(), Stream.of(method.getParameterTypes()).map(Class::toString).
                        collect(Collectors.toList()));
            }
        }

        return map;
    }

    /**
     * Calculates a function. Firstly, tries to find a function in {@code Functions}.
     * If it is found, calculates the function. If it is not found, tries to use
     * {@code Math} functions.
     *
     * @param function  The string representation of the function
     * @param arguments The function arguments
     * @return The string contains the computed value
     * @throws ArithmeticException             If an arithmetic error is happen
     * @throws InvalidInputExpressionException If the incoming string has an
     *                                         invalid format
     */
    public static String calculate(final String function, Double... arguments) throws InvalidInputExpressionException,
            ArithmeticException {

        String value = "";

        if (Stream.of(values()).anyMatch(f -> f.getImage().equals(function))) {
            value = valueOf(function.toUpperCase()).calculate(arguments).toString();

        } else if (mathFunctions.containsKey(function)) {
            List<String> args = mathFunctions.get(function);
            if (arguments.length == args.size()) {
                try {
                    Class<?>[] a = new Class[args.size()];
                    for (int i = 0; i < a.length; i++) {
                        a[i] = double.class;
                    }

                    value = Math.class.getMethod(function, a).invoke(Functions.class, (Object[]) arguments).toString();

                } catch (Exception e) {
                    throw new InvalidInputExpressionException("Input data is invalid cause this part contains " +
                                                                "an unknown function");
                }

            } else {
                throw new InvalidInputExpressionException("Input data is invalid cause this part cause the function " +
                        function + " contains " + arguments.length + " arguments instead of " + args.size());
            }
        }

        return value;
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
        List<String> functions = new LinkedList<>(mathFunctions.keySet());
        functions.addAll(Stream.of(values()).map(Functions::getImage).collect(Collectors.toList()));

        return functions.stream().map(Pattern::quote).collect(Collectors.joining("|", "(", ")\\" + OPEN_EXP));
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
     * Builds and returns a string representation of the User's functions list
     *
     * @return The string with the description of all the functions
     */
    public static String getList() {
        return Stream.of(values()).map(e -> {
            String arguments = IntStream.range(1, e.argumentsNumber == null ? 0 : e.argumentsNumber).
                                         mapToObj(i -> "x" + i).collect(Collectors.joining(", ", OPEN_EXP, CLOSE_EXP));
            return "\t" + e.getImage() +
                            (arguments.equals(OPEN_EXP + CLOSE_EXP) ? OPEN_EXP + "x1, x2 ... xN" + CLOSE_EXP : arguments);
        }).collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * Builds and returns a string representation of the Math functions list
     *
     * @return The string with the description of all the Math functions
     */
    public static String getMathList() {
        return mathFunctions.keySet().stream().
                map(e -> "\t" + e + IntStream.range(1, mathFunctions.get(e).size() + 1).mapToObj(i -> "x" + i).
                        collect(Collectors.joining(", ", OPEN_EXP, CLOSE_EXP))).collect(Collectors.joining(System.lineSeparator()));
    }}
