package com.sysgears.simplecalculator.computer.operatorsandfunctions;

import org.junit.BeforeClass;

public class ComputerRegExpTest extends ComputerTest {
    @BeforeClass
    public static void setUp() throws Exception {
        operatorsComputer = new ComputerRegExp();
    }
}