package com.sysgears.simplecalculator.computer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComputerBruteForceTest extends ComputerTest{
    ComputerBruteForce bruteForce = new ComputerBruteForce();

    @BeforeClass
    public static void setUp() throws Exception {
        computer = new ComputerBruteForce();
    }

    @Test
    public void testGetParenthesesExpression() {
        Assert.assertEquals("2+2", bruteForce.getParenthesesExpression("(2+2)*2"));
    }

    @Test
    public void testGetParenthesesExpressionLeadingMinus() {
        Assert.assertEquals("-2+2", bruteForce.getParenthesesExpression("(-2+2)*2"));
    }

    @Test
    public void testGetParenthesesExpressionLong() {
        Assert.assertEquals("(2+2)", bruteForce.getParenthesesExpression("2+((2+2))"));
    }

    @Test
    public void testGetParenthesesExpressionShort() {
        Assert.assertEquals("", bruteForce.getParenthesesExpression("()"));
    }

    @Test
    public void testGetParenthesesExpressionLong2() {
        Assert.assertEquals("(2+(1-1)*2)", bruteForce.getParenthesesExpression("2+((2+(1-1)*2))"));
    }

    @Test
    public void getBinaryExpressionADD() throws Exception {
        Assert.assertEquals("2+2", bruteForce.getBinaryExpression("2+2", Operators.ADD));
    }

    @Test
    public void getBinaryExpressionPower() throws Exception {
        Assert.assertEquals("-2^2", bruteForce.getBinaryExpression("2-2^2+8", Operators.POWER));
    }

    @Test
    public void getBinaryExpressionDivide() throws Exception {
        Assert.assertEquals("4/8", bruteForce.getBinaryExpression("2-2^2+4/8", Operators.DIVIDE));
    }

    @Test
    public void getBinaryExpressionMultiplty() throws Exception {
        Assert.assertEquals("-2*4.0", bruteForce.getBinaryExpression("-2*4.0+2^2+4/8", Operators.MULTIPLY));
    }

    @Test
    public void getBinaryExpressionSubstract() throws Exception {
        Assert.assertEquals("1-0.0", bruteForce.getBinaryExpression("-2*4.0+2^2+4/8+1-0.0", Operators.SUBTRACT));
    }
}