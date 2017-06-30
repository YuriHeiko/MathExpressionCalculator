package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ComputerMagic extends Computer {

    @Override
    public String compute(String expression) throws InvalidInputExpressionException {
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");

        try {
            return engine.eval(expression).toString();

        } catch (ScriptException e) {
            throw new InvalidInputExpressionException("JavaScript error");
        }
    }

    @Override
    String openParentheses(String expression) {
        return "";
    }

    @Override
    String computeArithmeticExpression(String expression) {
        return "";
    }
}
