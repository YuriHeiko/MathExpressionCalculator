package com.sysgears.simplecalculator.history;

import java.text.SimpleDateFormat;

public class ResultPair {
    private String expression;
    private String result;
    private long timeStamp;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm:ss");

    public ResultPair(String expression) {
        this.expression = expression;
    }

    public ResultPair(String expression, String result) {
        this(expression, result, System.currentTimeMillis());
    }

    public ResultPair(String expression, String result, long timeStamp) {
        this.expression = expression;
        this.result = result;
        this.timeStamp = timeStamp;
    }

    public String getExpression() {
        return expression;
    }

    public String getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultPair that = (ResultPair) o;

        return expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }

    @Override
    public String toString() {
        return "Expression = '" + expression + '\'' +
                ", Result = " + result +
                ", Time = " + sdf.format(timeStamp);
    }
}