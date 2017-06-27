package com.sysgears.simplecalculator.computer;

public class Expression implements Computer {
    private ValueHolder firstValue;
    private ValueHolder secondValue;
    private Operator operator;

    public Expression(Object firstValue, Object secondValue, Operator operator) {
        if (firstValue instanceof Double) {
            this.firstValue = new ValueHolder((Double) firstValue);
        } else if (firstValue instanceof Expression) {
            this.firstValue = new ValueHolder((Expression) firstValue);
        } else {
            throw new IllegalArgumentException("firstValue is neither Double nor Expression");
        }
        
        if (secondValue instanceof Double) {
            this.secondValue = new ValueHolder((Double) secondValue);
        } else if (secondValue instanceof Expression) {
            this.secondValue = new ValueHolder((Expression) secondValue);
        } else {
            throw new IllegalArgumentException("secondValue is neither Double nor Expression");
        }

        this.operator = operator;
    }

    Double getFirstValue() {
        return firstValue.getValue();
    }

    Double getSecondValue() {
        return secondValue.getValue();
    }

    @Override
    public Double evaluate() {
        return operator.evaluate(0.0,0.0);
    }

    private class ValueHolder {
        private Double evaluatedValue;
        private Expression expression;

        ValueHolder(Double evaluatedValue) {
            this.evaluatedValue = evaluatedValue;
        }

        ValueHolder(Expression expression) {
            this.expression = expression;
        }

        Double getValue() {
            return evaluatedValue == null ? expression.evaluate() : evaluatedValue;
        }
    }
}