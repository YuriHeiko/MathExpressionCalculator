package com.sysgears.simplecalculator.computer;

public enum Operator {
    ADD('+', 100) {
        @Override
        public double evaluate(double v1, double v2) {
            return v1 + v2;
        }
    },
    X('x', 100) {
        @Override
        public double evaluate(double v1, double v2) {
            return v1 * v2 / 2;
        }
    },
    SUBTRACT('-', 100) {
        @Override
        public double evaluate(double v1, double v2) {
            return v1 - v2;
        }
    },
    MULTIPLY('*', 1000) {
        @Override
        public double evaluate(double v1, double v2) {
            return v1 * v2;
        }
    },
    DIVIDE('/', 1000) {
        @Override
        public double evaluate(double v1, double v2) {
            return v1 / v2;
        }
    },
    SQRT('s', 2000) {
        @Override
        public double evaluate(double v1, double v2) {
            return Math.sqrt(v1);
        }
    },
    UNARY_PLUS('+', 3000) {
        @Override
        public double evaluate(double v1, double v2) {
            return v1;
        }
    },
    UNARY_MINUS('-', 3000) {
        @Override
        public double evaluate(double v1, double v2) {
            return -v1;
        }
    };

    private char operator;
    private int priority;

    public abstract double evaluate(double v1, double v2);

    private Operator(char operator, int priority) {
        this.operator = operator;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public static Operator getBySymbol(char symbol) {
        for (Operator operator : values()) {
            if (operator.operator == symbol) {
                return operator;
            }
        }

        throw new IllegalArgumentException();
    }

    public static boolean isBySymbol(char symbol) {
        for (Operator operator : values()) {
            if (operator.operator == symbol) {
                return true;
            }
        }

        return false;
    }
}
