package com.sysgears.simplecalculator.computer;

import com.sysgears.simplecalculator.exceptions.InvalidInputExpressionException;
import org.junit.Assert;
import org.junit.Test;

public abstract class ComputerTest {
    static Computer computer = new ComputerRegExp();

    @Test
    public void testComputeBinaryExpressionAdd() throws Exception {
        Assert.assertEquals("4", computer.computeBinaryExpression("2+2", Operators.ADD));
    }

    @Test
    public void testComputeBinaryExpressionSubtract() throws Exception {
        Assert.assertEquals("5.8999999999999995", computer.computeBinaryExpression(
                "9.1-3.2", Operators.SUBTRACT));
    }

    @Test
    public void testComputeBinaryExpressionMultiply() throws Exception {
        Assert.assertEquals("10", computer.computeBinaryExpression("2*5", Operators.MULTIPLY));
    }

    @Test
    public void testComputeBinaryExpressionDivide() throws Exception {
        Assert.assertEquals("2.5", computer.computeBinaryExpression("5/2", Operators.DIVIDE));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionAddError() throws Exception {
        Assert.assertEquals("4", computer.computeBinaryExpression("7+3x", Operators.ADD));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionSubtractError() throws Exception {
        Assert.assertEquals("5.9", computer.computeBinaryExpression("9,1-3z2", Operators.SUBTRACT));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionMultiplyError() throws Exception {
        Assert.assertEquals("10", computer.computeBinaryExpression("2x5", Operators.MULTIPLY));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionDivideError() throws Exception {
        Assert.assertEquals("2.5", computer.computeBinaryExpression("4*8f-5/1+3^0", Operators.DIVIDE));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionDivideByZeroError() throws Exception {
        Assert.assertEquals("2.5", computer.computeBinaryExpression("4*8-5/0+3^0", Operators.DIVIDE));
    }

    @Test
    public void testOpenParenthesesOne() throws Exception {
        Assert.assertEquals("4", computer.openParentheses("(2+2)"));
    }

    @Test
    public void testOpenParenthesesTwo() throws Exception {
        Assert.assertEquals("8", computer.openParentheses("(2+(1*8)-2)"));
    }

    @Test
    public void testOpenParenthesesThree() throws Exception {
        Assert.assertEquals("8-5", computer.openParentheses("(2+(1*8)-2)-5"));
    }

    @Test
    public void testOpenParenthesesFour() throws Exception {
        Assert.assertEquals("10*-12-5", computer.openParentheses("10*(-2+(-1*8)-2)-5"));
    }

    @Test
    public void testOpenParenthesesFive() throws Exception {
        Assert.assertEquals("1-1+1-1-1-1", computer.openParentheses("1-1-(1-2)-(2-1)-1-1"));
    }

    @Test
    public void testOpenParenthesesSix() throws Exception {
        Assert.assertEquals("-1+1-1+1-1+1-1", computer.openParentheses("-1+1-1+1-1+1-1"));
    }

    @Test
    public void testOpenParenthesesSeven() throws Exception {
        Assert.assertEquals("-19*9-1000*2+0", computer.openParentheses(
                "(-(12-7)*(6-2)+4/(7-3))*9-1000*2+(10-10)"));
    }

    @Test
    public void testOpenParenthesesEight() throws Exception {
        Assert.assertEquals("10^2+8^2", computer.openParentheses("(12-2)^2+8^2"));
    }

    @Test
    public void testOpenParenthesesNine() throws Exception {
        Assert.assertEquals("-0.8793103448275862*2.9999+10.98", computer.openParentheses(
                "(-(12.1-7)/5.8)*2.9999+10.98"));
    }

    @Test
    public void testOpenParenthesesTen() throws Exception {
        Assert.assertEquals("30", computer.openParentheses("-(-(-(-10-20)))"));
    }

    @Test
    public void testOpenParenthesesEleven() throws Exception {
        Assert.assertEquals("45-10-20-45-40+110", computer.openParentheses("45-10-20-45-40-(-10-100)"));
    }

    @Test
    public void testComputeArithmeticExpressionOne() throws Exception {
        Assert.assertEquals("-1", computer.computeArithmeticExpression("-1+1-1+1-1+1-1"));
    }

    @Test
    public void testComputeArithmeticExpressionTwo() throws Exception {
        Assert.assertEquals("-258.3333333333333", computer.computeArithmeticExpression(
                "-10*12-12^2-100/12+14"));
    }

    @Test
    public void testComputeArithmeticExpressionThree() throws Exception {
        Assert.assertEquals("-156", computer.computeArithmeticExpression("-12-12^2"));
    }

    @Test
    public void testComputeArithmeticExpressionFour() throws Exception {
        Assert.assertEquals("8.342156896551725", computer.computeArithmeticExpression(
                "(-(12.1-7)/5.8)*2.9999+10.98"));
    }

    @Test
    public void testComputeArithmeticExpressionFive() throws Exception {
        Assert.assertEquals("-2170.99999999999", computer.computeArithmeticExpression(
                "(-(12-7)*(6-2)+4/(7-3))*9-1000*2+(10^-11)"));
    }

    @Test
    public void testComputeArithmeticExpressionSix() throws Exception {
        Assert.assertEquals("40", computer.computeArithmeticExpression("45-10-20-45-40+110"));
    }

    @Test
    public void testComputeEmptyString() throws Exception {
        Assert.assertEquals("", computer.compute(""));
    }

    @Test
    public void testComputeEmptyParentheses() throws Exception {
        Assert.assertEquals("", computer.compute("()"));
    }

    @Test
    public void testComputeWhiteSpaces() throws Exception {
        Assert.assertEquals("-49.53333333333334", computer.compute(
                " (2+   8) /  6 - 12,8 * 4  ^    1"));
    }

    @Test
    public void testComputeOne() throws Exception {
        Assert.assertEquals("40", computer.compute("45-10-20-45-40-(-10-100)"));
    }

    @Test(expected = NullPointerException.class)
    public void testComputeNull() throws Exception {
        Assert.assertEquals("", computer.compute(null));
    }


    @Test
    public void testConvertFromScientificNotationOne() throws Exception {
        Assert.assertEquals("0.00001", computer.convertFromENotation("1E-5"));
    }

    @Test
    public void testConvertFromScientificNotationTwo() throws Exception {
        Assert.assertEquals("100000+0.211*180", computer.convertFromENotation("1E5+21.1E-2*18E1"));
    }

}