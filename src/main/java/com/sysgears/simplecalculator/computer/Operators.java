package com.sysgears.simplecalculator.computer;

public enum Operators {
    ADD('+', 100) {
        @Override
        public Double evaluate(Expression expression) {
            return expression.getFirstValue() + expression.getSecondValue();
        }
    },
    SUBTRACT('-', 100) {
        @Override
        public Double evaluate(Expression expression) {
            return expression.getFirstValue() - expression.getSecondValue();
        }
    },
    MULTIPLAY('*', 1000) {
        @Override
        public Double evaluate(Expression expression) {
            return expression.getFirstValue() * expression.getSecondValue();
        }
    },
    DIVIDE('/', 1000) {
        @Override
        public Double evaluate(Expression expression) {
            return expression.getFirstValue() / expression.getSecondValue();
        }
    };

    private char operator;
    private int priority;

    Operators(char operator, int priority) {
        this.operator = operator;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public abstract Double evaluate(Expression expression);
}
