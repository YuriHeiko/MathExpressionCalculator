package com.sysgears.simplecalculator.computer.everythingisfunction2;

import org.junit.Assert;
import org.junit.Test;

public class FunctionComputerTest {
    FunctionComputer functionComputer = new FunctionComputer();

    @Test
    public void testCompute() throws Exception {
        Assert.assertEquals("25.546232258060808", functionComputer.compute("cos(180)+22/sin(1)"));
    }

    @Test
    public void testGetOperands() throws Exception {
    }

    @Test
    public void testGetEnclosedExpression() throws Exception {
    }

    @Test
    public void testComputeFunction0() throws Exception {
        Assert.assertEquals("-1.0", functionComputer.computeFunction("sum(subtract(0,2),1)"));
    }

    @Test
    public void testComputeFunction1() throws Exception {
        Assert.assertEquals("-1.5657424899641503", functionComputer.computeFunction("sum(cos(pow(2,4)),sin(cos(4)))"));
    }

    @Test
    public void testComputeFunction2() throws Exception {
        Assert.assertEquals("0.17506738369244146",
                functionComputer.computeFunction("sum(cos(pow(subtract(2,8),sum(4,3))),sin(cos(subtract(0,multiply(4,2)))))"));
    }

    @Test
    public void testComputeFunction3() throws Exception {
        Assert.assertEquals("-224.0",
                functionComputer.computeFunction("subtract(21,multiply(8,45),(subtract(10,20)),45,(subtract(0,40)),(subtract(0,10,100)))"));
    }

    @Test
    public void testComputeFunction4() throws Exception {
        Assert.assertEquals("-211.9401511604541",
                functionComputer.computeFunction("sum(pow(subtract(12,cos(12)),2),multiply(cos(subtract(cos(24),sin(6))),sqrt(sum(cos(4),pow(10,2)))),(subtract(21,multiply(8,45),10,20,45,40,(subtract(0,10,100)))))"));
    }

    @Test
    public void testComputeFunction5() throws Exception {
        Assert.assertEquals("512.0", functionComputer.computeFunction("pow(2,3,2)"));
    }

/*
    @Test
    public void testGetEnclosedExpressionBound1() throws Exception {
        Assert.assertEquals(2, functionComputer.getEnclosedExpressionBound("(2)", "(", ")", 0));
    }

    @Test
    public void testGetEnclosedExpressionBound2() throws Exception {
        Assert.assertEquals(13, functionComputer.getEnclosedExpressionBound("(cos(2-(12+5)))", "(", ")", 4));
    }

    @Test
    public void testGetBound1() throws Exception {
        Assert.assertEquals(13, functionComputer.getBound("(cos(2-(12+5)))", 7, "(", ")"));
    }

    @Test
    public void testGetOperatorExpression1() throws Exception {
        Assert.assertEquals("2-(12+5)", functionComputer.getOperatorExpression("(cos(2-(12+5)))", "-"));
    }

    @Test
    public void testGetOperatorExpression2() throws Exception {
        Assert.assertEquals("12+5", functionComputer.getOperatorExpression("(cos(2-(12+5)))", "+"));
    }
*/
}