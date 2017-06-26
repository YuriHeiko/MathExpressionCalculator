package com.sysgears.simplecalculator.computer;

public enum Operators {
    PLUS('+', 100) {
        @Override
        public Integer evaluate(Expression expression) {
            return expression.getFirstValue() + expression.getSecondValue();
        }
    },
    MINUS('-', 100) {
        @Override
        public Integer evaluate(Expression expression) {
            return expression.getFirstValue() - expression.getSecondValue();
        }
    },
    MULTIPLAY('*', 1000) {
        @Override
        public Integer evaluate(Expression expression) {
            return expression.getFirstValue() * expression.getSecondValue();
        }
    },
    DIVIDE('/', 1000) {
        @Override
        public Integer evaluate(Expression expression) {
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

    public abstract Integer evaluate(Expression expression);
}
