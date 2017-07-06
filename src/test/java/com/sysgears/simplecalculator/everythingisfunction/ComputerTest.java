package com.sysgears.simplecalculator.everythingisfunction;

import org.junit.Assert;
import org.junit.Test;

public class ComputerTest {
    Computer computer = new Computer();

    @Test
    public void compute() throws Exception {
    }

    @Test
    public void changeOperatorsToFunctions() throws Exception {
        Assert.assertEquals("sum(2,2)", computer.changeOperatorsToFunctions("2+2"));
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