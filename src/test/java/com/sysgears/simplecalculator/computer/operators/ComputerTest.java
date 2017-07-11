package com.sysgears.simplecalculator.computer.operators;

import com.sysgears.simplecalculator.computer.exceptions.InvalidInputExpressionException;
import org.junit.Assert;
import org.junit.Test;

public abstract class ComputerTest {
    static OperatorsComputer operatorsComputer = new ComputerRegExp();
/*
    @Test
    public void testComputeBinaryEmptyString() throws Exception {
        Assert.assertEquals("", operatorsComputer.computeBinaryExpression("", Operators.ADD));
    }
*/

    @Test
    public void testComputeBinaryExpressionAdd() throws Exception {
        Assert.assertEquals("4", operatorsComputer.computeBinaryExpression("2+2", Operators.ADD));
    }

    @Test
    public void testComputeBinaryExpressionSubtract() throws Exception {
        Assert.assertEquals("5.8999999999999995", operatorsComputer.computeBinaryExpression(
                "9.1-3.2", Operators.SUBTRACT));
    }

    @Test
    public void testComputeBinaryExpressionMultiply() throws Exception {
        Assert.assertEquals("10", operatorsComputer.computeBinaryExpression("2*5", Operators.MULTIPLY));
    }

    @Test
    public void testComputeBinaryExpressionDivide() throws Exception {
        Assert.assertEquals("2.5", operatorsComputer.computeBinaryExpression("5/2", Operators.DIVIDE));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionAddError() throws Exception {
        Assert.assertEquals("4", operatorsComputer.computeBinaryExpression("7+3x", Operators.ADD));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionSubtractError() throws Exception {
        Assert.assertEquals("5.9", operatorsComputer.computeBinaryExpression("9,1-3z2", Operators.SUBTRACT));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionMultiplyError() throws Exception {
        Assert.assertEquals("10", operatorsComputer.computeBinaryExpression("2x5", Operators.MULTIPLY));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionDivideError() throws Exception {
        Assert.assertEquals("2.5", operatorsComputer.computeBinaryExpression("4*8f-5/1+3^0", Operators.DIVIDE));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeBinaryExpressionDivideByZeroError() throws Exception {
        Assert.assertEquals("2.5", operatorsComputer.computeBinaryExpression("4*8-5/0+3^0", Operators.DIVIDE));
    }

/*
    @Test
    public void testOpenParenthesesEmptyString() throws Exception {
        Assert.assertEquals("", operatorsComputer.openEnclosedExpression(""));
    }

    @Test
    public void testOpenParenthesesEmptyParentheses() throws Exception {
        Assert.assertEquals("", operatorsComputer.openEnclosedExpression("()"));
    }
*/

    @Test
    public void testOpenParenthesesOne() throws Exception {
        Assert.assertEquals("4", operatorsComputer.openEnclosedExpression("(2+2)"));
    }

    @Test
    public void testOpenParenthesesTwo() throws Exception {
        Assert.assertEquals("8", operatorsComputer.openEnclosedExpression("(2+(1*8)-2)"));
    }

    @Test
    public void testOpenParenthesesThree() throws Exception {
        Assert.assertEquals("8-5", operatorsComputer.openEnclosedExpression("(2+(1*8)-2)-5"));
    }

    @Test
    public void testOpenParenthesesFour() throws Exception {
        Assert.assertEquals("10*-12-5", operatorsComputer.openEnclosedExpression("10*(-2+(-1*8)-2)-5"));
    }

    @Test
    public void testOpenParenthesesFive() throws Exception {
        Assert.assertEquals("1-1+1-1-1-1", operatorsComputer.openEnclosedExpression("1-1-(1-2)-(2-1)-1-1"));
    }

    @Test
    public void testOpenParenthesesSix() throws Exception {
        Assert.assertEquals("-1+1-1+1-1+1-1", operatorsComputer.openEnclosedExpression("-1+1-1+1-1+1-1"));
    }

    @Test
    public void testOpenParenthesesSeven() throws Exception {
        Assert.assertEquals("-19*9-1000*2+0", operatorsComputer.openEnclosedExpression(
                "(-(12-7)*(6-2)+4/(7-3))*9-1000*2+(10-10)"));
    }

    @Test
    public void testOpenParenthesesEight() throws Exception {
        Assert.assertEquals("10^2+8^2", operatorsComputer.openEnclosedExpression("(12-2)^2+8^2"));
    }

    @Test
    public void testOpenParenthesesNine() throws Exception {
        Assert.assertEquals("-0.8793103448275862*2.9999+10.98", operatorsComputer.openEnclosedExpression(
                "(-(12.1-7)/5.8)*2.9999+10.98"));
    }

    @Test
    public void testOpenParenthesesTen() throws Exception {
        Assert.assertEquals("30", operatorsComputer.openEnclosedExpression("-(-(-(-10-20)))"));
    }

    @Test
    public void testOpenParenthesesEleven() throws Exception {
        Assert.assertEquals("45-10-20-45-40+110", operatorsComputer.openEnclosedExpression("45-10-20-45-40-(-10-100)"));
    }

    @Test
    public void testComputeArithmeticExpressionEmptyString() throws Exception {
        Assert.assertEquals("", operatorsComputer.computeArithmeticExpression(""));
    }

    @Test
    public void testComputeArithmeticExpressionOne() throws Exception {
        Assert.assertEquals("-1", operatorsComputer.computeArithmeticExpression("-1+1-1+1-1+1-1"));
    }

    @Test
    public void testComputeArithmeticExpressionTwo() throws Exception {
        Assert.assertEquals("-258.3333333333333", operatorsComputer.computeArithmeticExpression(
                "-10*12-12^2-100/12+14"));
    }

    @Test
    public void testComputeArithmeticExpressionThree() throws Exception {
        Assert.assertEquals("-156", operatorsComputer.computeArithmeticExpression("-12-12^2"));
    }

    @Test
    public void testComputeArithmeticExpressionFour() throws Exception {
        Assert.assertEquals("8.342156896551725", operatorsComputer.computeArithmeticExpression(
                "(-(12.1-7)/5.8)*2.9999+10.98"));
    }

    @Test
    public void testComputeArithmeticExpressionFive() throws Exception {
        Assert.assertEquals("-2170.99999999999", operatorsComputer.computeArithmeticExpression(
                "(-(12-7)*(6-2)+4/(7-3))*9-1000*2+(10^-11)"));
    }

    @Test
    public void testComputeArithmeticExpressionSix() throws Exception {
        Assert.assertEquals("40", operatorsComputer.computeArithmeticExpression("45-10-20-45-40+110"));
    }

    @Test
    public void testComputeEmptyString() throws Exception {
        Assert.assertEquals("", operatorsComputer.compute(""));
    }

    @Test
    public void testComputeEmptyParentheses() throws Exception {
        Assert.assertEquals("", operatorsComputer.compute("()"));
    }

    @Test
    public void testComputeWhiteSpaces() throws Exception {
        Assert.assertEquals("-49.53333333333334", operatorsComputer.compute("(2+8)/6-12.8*4^1"));
    }

    @Test(expected = InvalidInputExpressionException.class)
    public void testComputeNull() throws Exception {
        Assert.assertEquals("", operatorsComputer.compute(null));
    }

    @Test
    public void testComputeOne() throws Exception {
        Assert.assertEquals("40", operatorsComputer.compute("45-10-20-45-40-(-10-100)"));
    }

    @Test
    public void testComputeTwo() throws Exception {
        Assert.assertEquals("-211.94015116045406",
                operatorsComputer.compute("pow(12-cos(12),2)+cos(cos(24)-sin(6))*sqrt(cos(4)+pow(10,2))+(21-8*45-10-20-45-40-(-10-100))"));
    }

/*
    @Test
    public void testComputeThree() throws Exception {
        Assert.assertEquals("512", operatorsComputer.compute("2^3^2"));
    }
*/

    @Test
    public void testConvertFromENotationEmptyString() throws Exception {
        Assert.assertEquals("", operatorsComputer.convertFromENotation(""));
    }

    @Test
    public void testConvertFromENotationOne() throws Exception {
        Assert.assertEquals("0.00001", operatorsComputer.convertFromENotation("1E-5"));
    }

    @Test
    public void testConvertFromENotationTwo() throws Exception {
        Assert.assertEquals("100000+0.211*180", operatorsComputer.convertFromENotation("1E5+21.1E-2*18E1"));
    }

    @Test
    public void testGetParenthesesExpression() {
        Assert.assertEquals("2+2", operatorsComputer.getEnclosedExpression("(2+2)*2", "("));
    }

    @Test
    public void testGetParenthesesExpressionLeadingMinus() {
        Assert.assertEquals("-2+2", operatorsComputer.getEnclosedExpression("(-2+2)*2", "("));
    }

    @Test
    public void testGetParenthesesExpressionLong() {
        Assert.assertEquals("(2+2)", operatorsComputer.getEnclosedExpression("2+((2+2))", "("));
    }

    @Test
    public void testGetParenthesesExpressionLong2() {
        Assert.assertEquals("-(2+(1-1)*2)", operatorsComputer.getEnclosedExpression("2+(-(2+(1-1)*2))", "("));
    }

/*
    @Test
    public void testGetParenthesesExpressionEmptyParentheses() {
        Assert.assertEquals("", bruteForce.getEnclosedExpression("()"));
    }

    @Test
    public void testGetParenthesesExpressionEmptyString() {
        Assert.assertEquals("", bruteForce.getEnclosedExpression(""));
    }

    @Test
    public void getBinaryExpressionEmptyString() throws Exception {
        Assert.assertEquals("", bruteForce.getBinaryExpression("", Operators.ADD));
    }
*/

    @Test
    public void testComputeFunctionsOne() {
        Assert.assertEquals("-0.4480736161291702", operatorsComputer.computeFunctions("cos(90)"));
    }

    @Test
    public void testComputeFunctionsTwo() {
        Assert.assertEquals("0.8262041463870422", operatorsComputer.computeFunctions("cos(cos(180))"));
    }

    @Test
    public void testComputeFunctionsThree() {
        Assert.assertEquals("0.8262041463870422", operatorsComputer.computeFunctions("cos(-cos(180))"));
    }

    @Test
    public void testComputeFunctionsFour() {
        Assert.assertEquals("124.45959449408869", operatorsComputer.computeFunctions("pow(12-cos(12),2)"));
    }

    @Test
    public void testComputeFunctionsFive() {
        Assert.assertEquals("9.96726423744933", operatorsComputer.computeFunctions("sqrt(cos(4)+pow(10,2))"));
    }

    @Test
    public void testComputeFunctionsSix() {
        Assert.assertEquals("124.45959449408869+0.7625216071729388*9.96726423744933+(21-8*45-10-20-45-40-(-10-100))",
                operatorsComputer.computeFunctions("pow(12-cos(12),2)+cos(cos(24)-sin(6))*sqrt(cos(4)+pow(10,2))+(21-8*45-10-20-45-40-(-10-100))"));
    }
}