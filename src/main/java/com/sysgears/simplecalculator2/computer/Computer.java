package com.sysgears.simplecalculator2.computer;

import java.util.Objects;

public class Computer {
    private final int LEFT = -1;
    private final int RIGHT = 1;

    public String evaluate(String expression) { // Is there the String argument final or not???
        expression = Objects.requireNonNull(expression, "Incoming string cannot be null");

        if (expression.isEmpty()) {
            return expression;
        } else {
            return evaluateOperators(evaluateParentheses(expression.replaceAll("\\s", "")));
        }
    }

    String evaluateParentheses(String expression) {
        while (existParentheses(expression)) {
            String parenthesesExpression = findParenthesesExpression(expression);
            expression = expression.replaceAll(
                    castToRegEx("(" + parenthesesExpression + ")"),
                    evaluateParentheses(parenthesesExpression));
        }

        return evaluateOperators(expression);
    }

    String evaluateOperators(String expression) {
        for (Operator operator : Operator.values()) {
            while (existOperator(expression, operator) && isNotFinalNumber(expression)) {
                String leftValue = getExpressionValues(expression, operator, LEFT);
                String rightValue = getExpressionValues(expression, operator, RIGHT);
                expression = expression.
                        replaceAll(castToRegEx(leftValue) + operator.getRegExp() + rightValue,
                                (leftValue.charAt(0) == '-' ? "+" : "") +
                                        operator.evaluate(Double.parseDouble(leftValue),
                                                Double.parseDouble(rightValue)).toString());
            }
        }

        return expression;
    }

    boolean existParentheses(final String expression) {
        return expression.contains("(");
    }

    String findParenthesesExpression(final String expression) {
        int startIndex = expression.indexOf("(");
        int endIndex = startIndex;
        int c = 1;

        while (c > 0) {
            if (expression.charAt(++endIndex) == '(') {
                c++;
            } else if (expression.charAt(endIndex) == ')') {
                c--;
            }
        }

        return expression.substring(startIndex + 1, endIndex);
    }

    boolean existOperator(final String expression, final Operator operator) {
        return expression.contains(operator.getDepiction());
    }

    String getExpressionValues(final String expression, final Operator operator, final int DIRECTION) {
        // expression must not contain parentheses

        int operatorIndex = expression.indexOf(operator.getDepiction());
        if (expression.indexOf(operator.getDepiction(), operatorIndex + 1) != -1) {
            operatorIndex = expression.indexOf(operator.getDepiction(), operatorIndex + 1);
        }

        int index = operatorIndex + DIRECTION;

        while (index > 0 && index < expression.length() &&
                (Character.isDigit(expression.charAt(index)) || expression.charAt(index) == '.')) {

            index += DIRECTION;
        }

        if (DIRECTION == LEFT) {
            if (index > 0 && expression.charAt(index) != '-') {
                index++; // step back
            }

            return expression.substring(index, operatorIndex);

        } else {
            return expression.substring(operatorIndex + 1, index);
        }
    }

    String castToRegEx(final String expression) {
        return expression.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\");
    }

    boolean isNotFinalNumber(final String expression) {
        try {
            Double.parseDouble(expression);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}