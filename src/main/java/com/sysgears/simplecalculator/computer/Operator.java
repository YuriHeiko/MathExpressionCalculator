package com.sysgears.simplecalculator.computer;

public enum Operator {
    POWER("^") {
        @Override
        public Double evaluate(double v1, double v2) {
            return Math.pow(v1, v2);
        }

        @Override
        public String getRegExp() {
            return "\\" + getDepiction();
        }
    },
    DIVIDE("/") {
        @Override
        public Double evaluate(double v1, double v2) {
            return v1 / v2;
        }

        @Override
        public String getRegExp() {
            return "\\" + getDepiction();
        }
    },
    MULTIPLY("*") {
        @Override
        public Double evaluate(double v1, double v2) {
            return v1 * v2;
        }

        @Override
        public String getRegExp() {
            return "\\" + getDepiction();
        }
    },
    SUBTRACT("-") {
        @Override
        public Double evaluate(double v1, double v2) {
            return v1 - v2;
        }

        @Override
        public String getRegExp() {
            return "\\" + getDepiction();
        }
    },
    ADD("+") {
        @Override
        public Double evaluate(double v1, double v2) {
            return v1 + v2;
        }

        @Override
        public String getRegExp() {
            return "\\" + getDepiction();
        }
    };

    private String depiction;

    Operator(String operator) {
        this.depiction = operator;
    }

    public abstract Double evaluate(double v1, double v2);

    public abstract String getRegExp();

    public String getDepiction() {
        return depiction;
    }
}
