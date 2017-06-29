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
    public void getParenthesesExpression() throws Exception {
    }

    @Test
    public void containOperator() throws Exception {
    }

    @Test
    public void getBinaryExpression() throws Exception {
        Assert.assertEquals("2+2", bruteForce.getBinaryExpression("2+2", Operators.ADD));
    }
}