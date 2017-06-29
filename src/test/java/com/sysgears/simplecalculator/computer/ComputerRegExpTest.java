package com.sysgears.simplecalculator.computer;

import org.junit.BeforeClass;

public class ComputerRegExpTest extends ComputerTest {
    @BeforeClass
    public static void setUp() throws Exception {
        computer = new ComputerBruteForce();
    }
}