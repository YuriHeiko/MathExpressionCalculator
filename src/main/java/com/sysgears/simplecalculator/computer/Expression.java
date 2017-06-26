package com.sysgears.simplecalculator.computer;

public class Expression {
    private Object firstValue;
    private Object secondValue;
    private Operators operator;

    public Expression(Object firstValue, Object secondValue, Operators operator) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.operator = operator;
    }

//    (\((?:[^()]++|(?1))*\))

    public Expression() {
    }

    public Integer getFirstValue() {
        return (Integer) firstValue;
    }

    public Integer getSecondValue() {
        return (Integer) secondValue;
    }

    public Operators getOperator() {
        return operator;
    }


}
