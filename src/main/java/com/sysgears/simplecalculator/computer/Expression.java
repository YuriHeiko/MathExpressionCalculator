package com.sysgears.simplecalculator.computer;

public class Expression<T, S> {
    private T t;
    private S s;
    private Operators operator;

    public Expression(T t, S s, Operators operator) {
        this.t = t;
        this.s = s;
        this.operator = operator;
    }

//    (\((?:[^()]++|(?1))*\))

    public Expression() {
    }

    public Integer getFirstValue() {
        return (Integer) t;
    }

    public Integer getSecondValue() {
        return (Integer) s;
    }

    public Operators getOperator() {
        return operator;
    }


}
