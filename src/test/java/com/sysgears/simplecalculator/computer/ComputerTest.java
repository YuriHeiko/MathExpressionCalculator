package com.sysgears.simplecalculator.computer;

import org.junit.Assert;
import org.junit.Test;

public class ComputerTest {

    Computer computer = new Computer();

    @Test
    public void testFindFirstParenthesesExpression() {
        Assert.assertEquals("2+2", computer.getParenthesesExpression("(2+2)*2"));
    }

    @Test
    public void testFindFirstParenthesesExpressionLeadingMinus() {
        Assert.assertEquals("-2+2", computer.getParenthesesExpression("(-2+2)*2"));
    }

    @Test
    public void testFindFirstParenthesesExpressionLong() {
        Assert.assertEquals("(2+2)", computer.getParenthesesExpression("2+((2+2))"));
    }

    @Test
    public void testFindFirstParenthesesExpressionLong2() {
        Assert.assertEquals("(2+(1-1)*2)", computer.getParenthesesExpression("2+((2+(1-1)*2))"));
    }

    @Test
    public void testGetExpressionValuesLeft() {
        Assert.assertEquals("1", computer.getExpressionValue("1+2", Operator.ADD, -1));
    }

    @Test
    public void testGetExpressionValuesRight() {
        Assert.assertEquals("2", computer.getExpressionValue("1+2", Operator.ADD, 1));
    }

    @Test
    public void testGetExpressionValuesLeftComplex() {
        Assert.assertEquals("-1", computer.getExpressionValue("5-1*2+9", Operator.MULTIPLY, -1));
    }

    @Test
    public void testGetExpressionValuesRightComplex() {
        Assert.assertEquals("2", computer.getExpressionValue("5-1/2*3", Operator.DIVIDE, 1));
    }

    @Test
    public void testGetExpressionValuesLeftNegative() {
        Assert.assertEquals("-1", computer.getExpressionValue("-1+2", Operator.ADD, -1));
    }

    @Test
    public void testGetExpressionValuesLeftNegative2() {
        Assert.assertEquals("-5.0", computer.getExpressionValue("-5.0-2", Operator.SUBTRACT, -1));
    }

    @Test
    public void testGetExpressionValuesRightNegative() {
        Assert.assertEquals("2", computer.getExpressionValue("-5.0-2", Operator.SUBTRACT, 1));
    }

    @Test
    public void testGetExpressionValuesLeftNegativeNumber() {
        Assert.assertEquals("-1", computer.getExpressionValue("5-1+2", Operator.ADD, -1));
    }

    @Test
    public void testGetExpressionValuesLeftDouble() {
        Assert.assertEquals("10.1", computer.getExpressionValue("10.1+2", Operator.ADD, -1));
    }

    @Test
    public void testGetExpressionValuesRightDouble() {
        Assert.assertEquals("20.3", computer.getExpressionValue("1+20.3", Operator.ADD, 1));
    }

    @Test
    public void testEvaluateOperators() {
        Assert.assertEquals("4.0", computer.evaluateOperators("2+2"));
    }

    @Test
    public void testEvaluateOperatorsNegative() {
        Assert.assertEquals("-2.0", computer.evaluateOperators("2+2-6"));
    }

    @Test
    public void testEvaluateOperatorsConsequence() {
        Assert.assertEquals("3.0", computer.evaluateOperators("-1+2-1+2-1+2"));
    }

    @Test
    public void testEvaluateParentheses() {
        Assert.assertEquals("24.0", computer.evaluateParentheses("(2+2)*6"));
    }

    @Test
    public void testEvaluateParenthesesNegative() {
        Assert.assertEquals("-5.0", computer.evaluateParentheses("-(12-7)"));
    }

    @Test
    public void testEvaluateParenthesesNegativeLong() {
        Assert.assertEquals("-19.0", computer.evaluateParentheses("(-(12-7)-5)*2+1"));
    }

    @Test
    public void testEvaluateParenthesesLong() {
        Assert.assertEquals("-2171.0", computer.evaluateParentheses("(-(12-7)*(6-2)+4/(7-3))*9-1000*2+(10-10)"));
    }

    @Test
    public void testEvaluateParenthesesLongPower() {
        Assert.assertEquals("164.0", computer.evaluateParentheses("(12-2)^2+8^2"));
    }

    @Test
    public void testEvaluateParenthesesInvalid() {
        Assert.assertEquals("164.0", computer.evaluateParentheses("(12f2)^2+8^2"));
    }
}
