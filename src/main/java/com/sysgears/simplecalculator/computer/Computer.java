package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

public class Computer {
    private static final int LEFT = -1;
    private static final int RIGHT = 1;

    public String calculate(final String expression) throws InvalidInputExpressionException { // Is the String argument final or not???
        if (expression == null) {
            throw new InvalidInputExpressionException("Incoming string cannot be null");

        } else if (expression.isEmpty()) {
            return expression;
        }

        String result = calculateExpression(openParentheses(expression.replaceAll("\\s", "")));

        try {
            if (validateString(expression)) {
                throw new NumberFormatException();

            } else {
                result = Double.valueOf(result).toString();
            }

        } catch (NumberFormatException e) {
            throw new InvalidInputExpressionException(String.format("Input data is probably invalid cause " +
                    "the result of calculation: \"%s\" is not a number.", result));
        }

        return result;
    }

    private boolean validateString(final String expression) {
        return expression.contains("f") || expression.contains("d") || expression.contains("F") || expression.contains("D");
    }

    String openParentheses(String expression) throws InvalidInputExpressionException {
        while (expression.contains("(")) {
            String parenthesesExpression = getParenthesesExpression(expression);
            expression = expression.replace(
                    "(" + parenthesesExpression + ")",
                    openParentheses(parenthesesExpression));
        }

        return calculateExpression(expression);
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

    String calculateExpression(String expression) throws InvalidInputExpressionException {
        for (Operators operator : Operators.values()) {
            while (containOperator(expression, operator)) {
                String leftOperand = getExpressionOperand(expression, operator, LEFT);
                String rightOperand = getExpressionOperand(expression, operator, RIGHT);

                String calculatedValue;
                try {
                    calculatedValue = operator.calculate(Double.parseDouble(leftOperand),
                                                        Double.parseDouble(rightOperand)).toString();

                } catch (NumberFormatException e) {
                    throw new InvalidInputExpressionException(String.format("Input data is probably invalid cause " +
                            "this part of expression: \"%s\" is invalid", expression));
                }

                expression =
                        expression.
                                replace(leftOperand + operator.getDepiction() + rightOperand, calculatedValue).
                                replace("+-", "-");
            }
        }

        return expression;
    }

    boolean containOperator(final String expression, final Operators operator) {
        return expression.charAt(0) == '-'
                ? expression.indexOf(operator.getDepiction(), 1) != -1
                : expression.contains(operator.getDepiction());
    }

    String getExpressionOperand(final String expression, final Operators operator, final int SIDE) {
        int operatorIndex = expression.indexOf(operator.getDepiction(), 1);
        int index = operatorIndex + SIDE;

        while (index > 0 && index < expression.length() &&
                (Character.isDigit(expression.charAt(index)) || expression.charAt(index) == '.')) {

            index += SIDE;
        }

        if (SIDE == LEFT) {
            if (index > 0 && expression.charAt(index) != '-') {
                index++; // step back
            }
            return expression.substring(index, operatorIndex);

        } else {
            return expression.substring(operatorIndex + 1, index);
        }
    }
}