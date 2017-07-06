package com.sysgears.simplecalculator.everythingisfunction;

import org.junit.Assert;
import org.junit.Test;

public class ComputerTest {
    Computer computer = new Computer();

    @Test
    public void compute() throws Exception {
    }

    @Test
    public void changeOperatorsToFunctions0() throws Exception {
        Assert.assertEquals("sum(subtract(0,2),1)", computer.changeOperatorsToFunctions("-2+1"));
    }

    @Test
    public void changeOperatorsToFunctions1() throws Exception {
        Assert.assertEquals("sum(cos(pow(2,4)),sin(cos(4)))",
                computer.changeOperatorsToFunctions("cos(pow(2,4))+sin(cos(4))"));
    }

    @Test
    public void changeOperatorsToFunctions2() throws Exception {
        Assert.assertEquals("pow(sum(2,8),sum(4,3))", computer.changeOperatorsToFunctions("pow(2+8,4+3)"));
    }

    @Test
    public void changeOperatorsToFunctions3() throws Exception {
        Assert.assertEquals("sum(cos(pow(subtract(2,8),sum(4,3))),sin(cos(subtract(0,mult(4,2)))))",
                computer.changeOperatorsToFunctions("cos(pow(2-8,4+3))+sin(cos(-4*2))"));
    }

    @Test
    public void changeOperatorsToFunctions4() throws Exception {
        Assert.assertEquals("sum(cos(subtract(0,pow(sum(2,8),sum(4,3)))),sin(cos(mult(4,2))))",
                computer.changeOperatorsToFunctions("cos(-pow(2+8,4+3))+sin(cos(4*2))"));
    }

    @Test
    public void changeOperatorsToFunctions5() throws Exception {
        Assert.assertEquals("subtract(0,10)",computer.changeOperatorsToFunctions("-10"));
    }

    @Test
    public void changeOperatorsToFunctions6() throws Exception {
        Assert.assertEquals("subtract(0,10)",
                computer.changeOperatorsToFunctions("(2-(-10-100))"));
    }

    @Test
    public void changeOperatorsToFunctions7() throws Exception {
        Assert.assertEquals("subtract(0,10)",
                computer.changeOperatorsToFunctions("pow(12-cos(12),2)+cos(cos(24)-sin(6))*sqrt(cos(4)+pow(10,2))+" +
                        "(21-8*45-10-20-45-40-(-10-100))"));
    }

    @Test
    public void getOperands() throws Exception {
    }

    @Test
    public void getEnclosedExpression() throws Exception {
    }

    @Test
    public void computeFunctions() throws Exception {
    }

    @Test
    public void convertFromENotation() throws Exception {
    }

}