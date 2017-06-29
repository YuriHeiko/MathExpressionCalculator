package com.sysgears.simplecalculator.computer;

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

    private String depiction;

    Operators(String operator) {
        this.depiction = operator;
    }

    public abstract Double calculate(final double v1, final double v2);

    public String getDepiction() {
        return depiction;
    }
}
