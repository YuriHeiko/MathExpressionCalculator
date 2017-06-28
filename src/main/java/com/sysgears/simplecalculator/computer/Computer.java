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
        while (isParentheses(expression)) {
            String parenthesesExpression = getParenthesesExpression(expression);
            expression = expression.replaceAll(
                    convertToRegExp("(" + parenthesesExpression + ")"),
                    evaluateParentheses(parenthesesExpression));
        }

        return evaluateOperators(expression);
    }

    String convertToRegExp(final String expression) {
        return expression.replaceAll("(?=[]\\[+&|!(){}^\"~*?:\\\\-])", "\\\\");
    }

    boolean isParentheses(final String expression) {
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
            while (existOperator(expression, operator)) {
                String leftValue = getExpressionValues(expression, operator, LEFT);
                String rightValue = getExpressionValues(expression, operator, RIGHT);
                Double value = operator.evaluate(Double.parseDouble(leftValue), Double.parseDouble(rightValue));
                expression =
                        expression.
                                replaceAll(leftValue + operator.getRegExp() + rightValue, value.toString()).
                                replaceAll("\\+\\-", "-");
            }
        }

        return expression;
    }

    boolean existOperator(final String expression, final Operator operator) {
        return expression.charAt(0) == '-'
                ? expression.indexOf(operator.getDepiction(), 1) != -1
                : expression.contains(operator.getDepiction());
    }

    String getExpressionValues(final String expression, final Operator operator, final int DIRECTION) {
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