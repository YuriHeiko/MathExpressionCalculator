package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;
import org.junit.Assert;
import org.junit.Test;

public abstract class ComputerTest {
    static Computer computer = new ComputerBruteForce();

    // TODO add divide by zero test

    @Test
    public void testValidateString() throws Exception {
        Assert.assertFalse(computer.isStringInvalid("12+2"));
    }

    @Test
    public void testValidateStringFalse() throws Exception {
        Assert.assertTrue(computer.isStringInvalid("12D+2f"));
    }

    @Test
    public void testComputeBinaryExpressionAdd() throws Exception {
        Assert.assertEquals("4.0", computer.computeBinaryExpression("2+2", Operators.ADD));
    }

    @Test
    public void testComputeBinaryExpressionSubtract() throws Exception {
        Assert.assertEquals("5.8999999999999995", computer.computeBinaryExpression("9.1-3.2", Operators.SUBTRACT));
    }

    @Test
    public void testComputeBinaryExpressionMultiply() throws Exception {
        Assert.assertEquals("10.0", computer.computeBinaryExpression("2*5", Operators.MULTIPLY));
    }

    @Test
    public void testComputeBinaryExpressionDivide() throws Exception {
        Assert.assertEquals("2.5", computer.computeBinaryExpression("5/2", Operators.DIVIDE));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionAddError() throws Exception {
        Assert.assertEquals("4.0", computer.computeBinaryExpression("7+3x", Operators.ADD));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionSubtractError() throws Exception {
        Assert.assertEquals("5.9", computer.computeBinaryExpression("9,1-3z2", Operators.SUBTRACT));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionMultiplyError() throws Exception {
        Assert.assertEquals("10.0", computer.computeBinaryExpression("2x5", Operators.MULTIPLY));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionDivideError() throws Exception {
        Assert.assertEquals("2.5", computer.computeBinaryExpression("5/sd2", Operators.DIVIDE));
    }

    @Test
    public void testOpenParenthesesOne() throws Exception {
        Assert.assertEquals("4.0", computer.openParentheses("(2+2)"));
    }

    @Test
    public void testOpenParenthesesTwo() throws Exception {
        Assert.assertEquals("8.0", computer.openParentheses("(2+(1*8)-2)"));
    }

    @Test
    public void testOpenParenthesesThree() throws Exception {
        Assert.assertEquals("3.0", computer.openParentheses("(2+(1*8)-2)-5"));
    }

    @Test
    public void testOpenParenthesesFour() throws Exception {
        Assert.assertEquals("-125.0", computer.openParentheses("10*(-2+(-1*8)-2)-5"));
    }

    @Test
    public void testOpenParenthesesFive() throws Exception {
        Assert.assertEquals("0.0", computer.openParentheses("1-1-(1-2)-(1-2)-1-1"));
    }

    @Test
    public void testOpenParenthesesSix() throws Exception {
        Assert.assertEquals("-1.0", computer.openParentheses("-1+1-1+1-1+1-1"));
    }

    @Test
    public void testOpenParenthesesSeven() throws Exception {
        Assert.assertEquals("-2171.0", computer.openParentheses("(-(12-7)*(6-2)+4/(7-3))*9-1000*2+(10-10)"));
    }

    @Test
    public void testOpenParenthesesEight() throws Exception {
        Assert.assertEquals("164.0", computer.openParentheses("(12-2)^2+8^2"));
    }

    @Test
    public void testOpenParenthesesNine() throws Exception {
        Assert.assertEquals("8.342156896551725", computer.openParentheses("(-(12.1-7)/5.8)*2.9999+10.98"));
    }

    @Test
    public void testComputeArithmeticExpressionOne() throws Exception {
        Assert.assertEquals("-1.0", computer.computeArithmeticExpression("-1+1-1+1-1+1-1"));
    }

    @Test
    public void testComputeArithmeticExpressionTwo() throws Exception {
        Assert.assertEquals("-258.3333333333333", computer.computeArithmeticExpression("-10*12-12^2-100/12+14"));
    }

    @Test
    public void testComputeArithmeticExpressionThree() throws Exception {
        Assert.assertEquals("-156.0", computer.computeArithmeticExpression("-12-12^2"));
    }

    @Test
    public void testComputeEmptyString() throws Exception {
        Assert.assertEquals("", computer.compute(""));
    }

    @Test
    public void testComputeWhiteSpaces() throws Exception {
        Assert.assertEquals("-46.333333333333336", computer.compute(" (2+   8) /  6 - 12 * 4  ^    1"));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeNull() throws Exception {
        Assert.assertEquals("", computer.compute(null));
    }

}