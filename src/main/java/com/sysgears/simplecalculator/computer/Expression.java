package com.sysgears.simplecalculator.computer;

public class Expression {
    private ValueHolder firstValue;
    private ValueHolder secondValue;
    private Operators operator;

    public Expression(Double firstValue, Double secondValue, Operators operator) {
        this.firstValue = new ValueHolder(firstValue);
        this.secondValue = new ValueHolder(secondValue);
        this.operator = operator;
    }

    public Expression(Double firstValue, Expression secondValue, Operators operator) {
        this.firstValue = new ValueHolder(firstValue);
        this.secondValue = new ValueHolder(secondValue);
        this.operator = operator;
    }

    public Expression(Expression firstValue, Double secondValue, Operators operator) {
        this.firstValue = new ValueHolder(firstValue);
        this.secondValue = new ValueHolder(secondValue);
        this.operator = operator;
    }

    public Expression(Expression firstValue, Expression secondValue, Operators operator) {
        this.firstValue = new ValueHolder(firstValue);
        this.secondValue = new ValueHolder(secondValue);
        this.operator = operator;
    }

//    (\((?:[^()]++|(?1))*\))


    public Double getFirstValue() {
        return firstValue.getValue();
    }

    public Double getSecondValue() {
        return secondValue.getValue();
    }

    public Double evaluate() {
        return operator.evaluate(this);
    }

    private class ValueHolder {
        private Double evaluatedValue;
        private Expression expression;

        public ValueHolder(Double evaluetedValue) {
            this.evaluatedValue = evaluetedValue;
        }

        public ValueHolder(Expression expression) {
            this.expression = expression;
        }

        public Double getValue() {
            return evaluatedValue == null ? expression.evaluate() : evaluatedValue;
        }
    }

}
