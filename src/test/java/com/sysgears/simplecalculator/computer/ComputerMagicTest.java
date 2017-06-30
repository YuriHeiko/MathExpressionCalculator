package com.sysgears.simplecalculator.computer;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComputerMagicTest {

    Computer computer = new ComputerMagic();
    
    @Test
    public void testComputeOne() throws Exception {
        Assert.assertEquals("4", computer.compute("(2+2)"));
    }

    @Test
    public void testComputeTwo() throws Exception {
        Assert.assertEquals("8", computer.compute("(2+(1*8)-2)"));
    }

    @Test
    public void testComputeThree() throws Exception {
        Assert.assertEquals("3", computer.compute("(2+(1*8)-2)-5"));
    }

    @Test
    public void testComputeFour() throws Exception {
        Assert.assertEquals("-125", computer.compute("10*(-2+(-1*8)-2)-5"));
    }

    @Test
    public void testComputeFive() throws Exception {
        Assert.assertEquals("0", computer.compute("1-1-(1-2)-(1-2)-1-1"));
    }

    @Test
    public void testComputeSix() throws Exception {
        Assert.assertEquals("-1", computer.compute("-1+1-1+1-1+1-1"));
    }

    @Test
    public void testComputeSeven() throws Exception {
        Assert.assertEquals("-2171", computer.compute("(-(12-7)*(6-2)+4/(7-3))*9-1000*2+(10-10)"));
    }

    @Test
    public void testComputeEight() throws Exception {
        Assert.assertEquals("164", computer.compute("(12-2)*(12-2)+8*8"));
    }

    @Test
    public void testComputeNine() throws Exception {
        Assert.assertEquals("8.342156896551725", computer.compute("(-(12.1-7)/5.8)*2.9999+10.98"));
    }

    @Test
    public void testComputeTen() throws Exception {
        Assert.assertEquals("30", computer.compute("-(-(-(-10-20)))"));
    }
}