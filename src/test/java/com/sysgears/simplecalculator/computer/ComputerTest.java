package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;
import org.junit.Assert;
import org.junit.Test;

public class ComputerTest {

    Computer computer = new Computer();

    @Test
    public void testGetParenthesesExpression() {
        Assert.assertEquals("2+2", computer.getParenthesesExpression("(2+2)*2"));
    }

    @Test
    public void testGetParenthesesExpressionLeadingMinus() {
        Assert.assertEquals("-2+2", computer.getParenthesesExpression("(-2+2)*2"));
    }

    @Test
    public void testGetParenthesesExpressionLong() {
        Assert.assertEquals("(2+2)", computer.getParenthesesExpression("2+((2+2))"));
    }

    @Test
    public void testGetParenthesesExpressionShort() {
        Assert.assertEquals("", computer.getParenthesesExpression("()"));
    }

    @Test
    public void testGetParenthesesExpressionLong2() {
        Assert.assertEquals("(2+(1-1)*2)", computer.getParenthesesExpression("2+((2+(1-1)*2))"));
    }

    @Test
    public void testGetExpressionOperandLeft() {
        Assert.assertEquals("1", computer.getExpressionOperand("1+2", Operators.ADD, -1));
    }

    @Test
    public void testGetExpressionOperandRight() {
        Assert.assertEquals("2", computer.getExpressionOperand("1+2", Operators.ADD, 1));
    }

    @Test
    public void testGetExpressionOperandLeftComplex() {
        Assert.assertEquals("-1", computer.getExpressionOperand("5-1*2+9", Operators.MULTIPLY, -1));
    }

    @Test
    public void testGetExpressionOperandRightComplex() {
        Assert.assertEquals("2", computer.getExpressionOperand("5-1/2*3", Operators.DIVIDE, 1));
    }

    @Test
    public void testGetExpressionOperandLeftNegative() {
        Assert.assertEquals("-1", computer.getExpressionOperand("-1+2", Operators.ADD, -1));
    }

    @Test
    public void testGetExpressionOperandLeftNegative2() {
        Assert.assertEquals("-5.0", computer.getExpressionOperand("-5.0-2", Operators.SUBTRACT, -1));
    }

    @Test
    public void testGetExpressionOperandRightNegative() {
        Assert.assertEquals("2", computer.getExpressionOperand("-5.0-2", Operators.SUBTRACT, 1));
    }

    @Test
    public void testGetExpressionOperandLeftNegativeNumber() {
        Assert.assertEquals("-1", computer.getExpressionOperand("5-1+2", Operators.ADD, -1));
    }

    @Test
    public void testGetExpressionOperandLeftDouble() {
        Assert.assertEquals("10.1", computer.getExpressionOperand("10.1+2", Operators.ADD, -1));
    }

    @Test
    public void testGetExpressionOperandRightDouble() {
        Assert.assertEquals("20.3", computer.getExpressionOperand("1+20.3", Operators.ADD, 1));
    }

    @Test
    public void testCalculateExpression() {
        Assert.assertEquals("4.0", computer.calculateExpression("2+2"));
    }

    @Test
    public void testCalculateExpressionNegative() {
        Assert.assertEquals("-2.0", computer.calculateExpression("2+2-6"));
    }

    @Test
    public void testCalculateExpressionConsequence() {
        Assert.assertEquals("3.0", computer.calculateExpression("-1+2-1+2-1+2"));
    }

    @Test
    public void testOpenParentheses() {
        Assert.assertEquals("24.0", computer.openParentheses("(2+2)*6"));
    }

    @Test
    public void testOpenParenthesesNegative() {
        Assert.assertEquals("-5.0", computer.openParentheses("-(12-7)"));
    }

    @Test
    public void testOpenParenthesesNegativeLong() {
        Assert.assertEquals("-19.0", computer.openParentheses("(-(12-7)-5)*2+1"));
    }

    @Test
    public void testOpenParenthesesLong() {
        Assert.assertEquals("-2171.0", computer.openParentheses("(-(12-7)*(6-2)+4/(7-3))*9-1000*2+(10-10)"));
    }

    @Test
    public void testOpenParenthesesLongPower() {
        Assert.assertEquals("164.0", computer.openParentheses("(12-2)^2+8^2"));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testEvaluateInvalidString() {
        computer.calculate("(12f2)^2+8^2");
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testEvaluateNullString() {
        computer.calculate(null);
    }

    @Test
    public void testEvaluateEmptyString() {
        Assert.assertEquals("", computer.calculate(""));
    }
}