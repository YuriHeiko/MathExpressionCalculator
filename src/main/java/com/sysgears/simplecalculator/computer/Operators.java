package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.utils.RegExpUtils;

/**
 * Contains possible operators and their math logic.
 */
public enum Operators {
    POWER("^") {
        @Override
        public Double calculate(final double v1, final double v2) {
            return Math.pow(v1, v2);
        }
    },
    DIVIDE("/") {
        @Override
        public Double calculate(final double v1, final double v2) {
            return v1 / v2;
        }
    },
    MULTIPLY("*") {
        @Override
        public Double calculate(final double v1, final double v2) {
            return v1 * v2;
        }
    },
    SUBTRACT("-") {
        @Override
        public Double calculate(final double v1, final double v2) {
            return v1 - v2;
        }
    },
    ADD("+") {
        @Override
        public Double calculate(final double v1, final double v2) {
            return v1 + v2;
        }
    };

    /**
     * The string representation of the operator
     */
    private String depiction;

    /**
     * Constructs an object
     *
     * @param depiction the string representation of the operator
     */
    Operators(String depiction) {
        this.depiction = depiction;
    }

    /**
     * Contains the math logic of the operator
     *
     * @param v1 the left operand
     * @param v2 the right operand
     * @return computed value
     */
    public abstract Double calculate(final double v1, final double v2);

    /**
     * Returns the string representation of the operator
     *
     * @return the string representation of the operator
     */
    public String getDepiction() {
        return depiction;
    }

    /**
     * Returns the RegExp string representation of the operator
     *
     * @return the string representation of the operator
     */
    public String getDepictionRegExp() {
        return RegExpUtils.changeToRegExp(depiction);
    }

    /**
     * Builds a RegExp string contains all the operators
     *
     * @return a RegExp string contains all the operators
     */
    public static String getRegExp() {
        StringBuilder builder = new StringBuilder("[");

        for (Operators operator : values()) {
            builder.append(operator.getDepictionRegExp());
        }

        return builder.append("]{1}").toString();
    }
}