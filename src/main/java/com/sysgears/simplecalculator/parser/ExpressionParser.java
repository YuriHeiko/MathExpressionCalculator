package com.sysgears.simplecalculator.parser;

import com.sysgears.simplecalculator.computer.Expression;

public class ExpressionParser implements StringParser {
    private StringBuilder builder;

    public ExpressionParser(String expressionString) {

        if (isExpressionStringValid(expressionString)) {
            this.builder = new StringBuilder(expressionString);
        } else {
            throw new IllegalArgumentException("An invalid expression string!");
        }
    }

    private boolean isExpressionStringValid(String expressionString) {

        return true;
    }

    @Override
    public Expression parse(String incomingString) {



        return null;
    }

    private Double findNumber() {


        return 0.0;
    }

    private Expression findParentheses() {
        //    (\((?:[^()]++|(?1))*\))

        return null;
    }

    private void findNext() {

    }
}
