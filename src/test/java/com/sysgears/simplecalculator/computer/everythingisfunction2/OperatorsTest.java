package com.sysgears.simplecalculator.computer.everythingisfunction2;

import org.junit.Test;

import static org.junit.Assert.*;

public class OperatorsTest {

    @Test
    public void testChangeOperatorsToFunctions0() throws Exception {
        assertEquals("sum(subtract(0,2),1)", Operators.convertToFunctions("-2+1"));
    }

    @Test
    public void testChangeOperatorsToFunctions1() throws Exception {
        assertEquals("sum(cos(power(2,4)),sin(cos(4)))",
                Operators.convertToFunctions("cos(power(2,4))+sin(cos(4))"));
    }

    @Test
    public void testChangeOperatorsToFunctions2() throws Exception {
        assertEquals("power(sum(2,8),sum(4,3))", Operators.convertToFunctions("power(2+8,4+3)"));
    }

    @Test
    public void testChangeOperatorsToFunctions3() throws Exception {
        assertEquals("sum(cos(power(subtract(2,8),sum(4,3))),sin(cos(subtract(0,multiply(4,2)))))",
                Operators.convertToFunctions("cos(power(2-8,4+3))+sin(cos(-4*2))"));
    }

    @Test
    public void testChangeOperatorsToFunctions4() throws Exception {
        assertEquals("sum(cos(subtract(0,power(sum(2,8),sum(4,3)))),sin(cos(multiply(4,2))))",
                Operators.convertToFunctions("cos(-power(2+8,4+3))+sin(cos(4*2))"));
    }

    @Test
    public void testChangeOperatorsToFunctions5() throws Exception {
        assertEquals("subtract(0,10)", Operators.convertToFunctions("-10"));
    }

    @Test
    public void testChangeOperatorsToFunctions6() throws Exception {
        assertEquals("subtract(21,multiply(8,45),(subtract(10,20)),45,(subtract(0,40)),(subtract(0,10,100)))",
                Operators.convertToFunctions("21-8*45-(10-20)-45-(-40)-(-10-100)"));
    }

    @Test
    public void testChangeOperatorsToFunctions7() throws Exception {
        assertEquals("sum(power(subtract(12,cos(12)),2),multiply(cos(subtract(cos(24),sin(6))),sqrt(sum(cos(4),power(10,2)))))",
                Operators.convertToFunctions("power(12-cos(12),2)+cos(cos(24)-sin(6))*sqrt(cos(4)+power(10,2))"));
    }

    @Test
    public void testChangeOperatorsToFunctions8() throws Exception {
        assertEquals("sum(power(subtract(12,cos(12)),2),multiply(cos(subtract(cos(24),sin(6))),sqrt(sum(cos(4),power(10,2)))),(subtract(21,multiply(8,45),10,20,45,40,(subtract(0,10,100)))))",
                Operators.convertToFunctions("power(12-cos(12),2)+cos(cos(24)-sin(6))*sqrt(cos(4)+power(10,2))+" +
                        "(21-8*45-10-20-45-40-(-10-100))"));
    }

    @Test
    public void testChangeOperatorsToFunctions9() throws Exception {
        assertEquals("power(2,3,2)", Operators.convertToFunctions("2^3^2"));
    }

    @Test
    public void testChangeOperatorsToFunctions10() throws Exception {
        assertEquals("sum(2,power(3,2))", Operators.convertToFunctions("2+3^2"));
    }

    @Test
    public void testChangeOperatorsToFunctions11() throws Exception {
        assertEquals("subtract(0,(subtract(0,10,100)))", Operators.convertToFunctions("-(-10-100)"));
    }
}