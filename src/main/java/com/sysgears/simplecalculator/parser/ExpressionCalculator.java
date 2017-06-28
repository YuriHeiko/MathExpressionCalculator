package com.sysgears.simplecalculator.parser;

import com.sysgears.simplecalculator.computer.Operator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionCalculator {
    StringBuilder builder;
    private Pattern numberPattern = Pattern.compile("(^[+-]?[0-9.,]+)");

    public ExpressionCalculator(String expression) {
        this.builder = new StringBuilder(expression);
    }

    public double evaluate() {
        double result = firstLevel();

        while (true) {
            if (isEvaluationNeeded(100)) {
                result = getOperator().evaluate(result, firstLevel());
            } else {
                return result;
            }
        }
    }

    double firstLevel() {
        double result = secondLevel();

        while (true) {
            if (isEvaluationNeeded(1000)) {
                result = getOperator().evaluate(result, secondLevel());
            } else {
                return result;
            }
        }
    }

    double secondLevel() {
        double result;

        if (isEvaluationNeeded(3000)) {
            result = getOperator().evaluate(secondLevel(), 0.0);

        } else if (isEvaluationNeeded(2000)) {
            if (areParenthesesNext()) {
                builder.deleteCharAt(0);
                result = getOperator().evaluate(secondLevel(), 0.0);
                builder.deleteCharAt(0);

            } else {
                throw new IllegalArgumentException(builder.toString());
            }

        } else if (areParenthesesNext()) {
            builder.deleteCharAt(0);
            result = evaluate();
            builder.deleteCharAt(0);

        } else if (isNextNumber()) {
            result = getNumber();

        } else {
            throw new IllegalArgumentException(builder.toString());
        }

        return result;
    }

    private boolean isEvaluationNeeded(int priority) {
        return builder.length() != 0 &&
                Operator.isBySymbol(builder.charAt(0)) &&
                Operator.getBySymbol(builder.charAt(0)).getPriority() == priority;
    }

    private Operator getOperator() {
        char c = builder.charAt(0);
        builder.deleteCharAt(0);

        return Operator.getBySymbol(c);
    }

    private boolean isNextNumber() {
        return numberPattern.matcher(builder).find();
    }

    private double getNumber() {
        String result = "";

        Matcher matcher = numberPattern.matcher(builder);
        if (matcher.find()) {
            result = matcher.group();
        }
        builder.delete(0, result.length());

        return Double.parseDouble(result);
    }

    private boolean areParenthesesNext() {
        return builder.charAt(0) == '(';
    }
}