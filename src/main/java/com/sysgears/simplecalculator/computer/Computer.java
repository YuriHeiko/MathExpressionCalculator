package com.sysgears.simplecalculator.computer;

import java.util.Objects;

public class Computer {
    private final int LEFT = -1;
    private final int RIGHT = 1;

    public String evaluate(String expression) { // Is the String argument final or not???
        expression = Objects.requireNonNull(expression, "Incoming string cannot be null");

        return !expression.isEmpty()
                ? evaluateOperators(evaluateParentheses(expression.replaceAll("\\s", "")))
                : expression;
    }

    String evaluateParentheses(String expression) {
        while (containParentheses(expression)) {
            String parenthesesExpression = getParenthesesExpression(expression);
            expression = expression.replace(
                    "(" + parenthesesExpression + ")",
                    evaluateParentheses(parenthesesExpression));
        }

        return evaluateOperators(expression);
    }

    boolean containParentheses(final String expression) {
        return expression.contains("(");
    }

    String getParenthesesExpression(final String expression) {
        int startIndex = expression.indexOf('(');
        int endIndex = startIndex;

        for (int counter = 1; counter > 0; ) {
            if (expression.charAt(++endIndex) == '(') {
                counter++;
            } else if (expression.charAt(endIndex) == ')') {
                counter--;
            }
        }

        return expression.substring(startIndex + 1, endIndex);
    }

    String evaluateOperators(String expression) {
        for (Operator operator : Operator.values()) {
            while (containOperator(expression, operator)) {
                String leftValue = getExpressionValue(expression, operator, LEFT);
                String rightValue = getExpressionValue(expression, operator, RIGHT);
                String calculatedValue = operator.evaluate(Double.parseDouble(leftValue), Double.parseDouble(rightValue)).toString();
                expression =
                        expression.
                                replace(leftValue + operator.getDepiction() + rightValue, calculatedValue).
                                replace("+-", "-");
            }
        }

        return expression;
    }

    boolean containOperator(final String expression, final Operator operator) {
        return expression.charAt(0) == '-'
                ? expression.indexOf(operator.getDepiction(), 1) != -1
                : expression.contains(operator.getDepiction());
    }

    String getExpressionValue(final String expression, final Operator operator, final int DIRECTION) {
        // expression must not contain parentheses
        int operatorIndex = expression.indexOf(operator.getDepiction(), 1);
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
}