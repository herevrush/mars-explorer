package com.example.explorer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * Simulation Test
 */
@RunWith(JUnit4.class)
public class SimulationTest {

    private final ByteArrayOutputStream testOutput = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errOutput = new ByteArrayOutputStream();
    private final PrintStream sysOut = System.out;
    private final PrintStream sysErr = System.err;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(testOutput));
        System.setErr(new PrintStream(errOutput));
    }

    @After
    public void restoreStreams() {
        System.setOut(sysOut);
        System.setErr(sysErr);
    }

    /**
     * Test PLACE command
     */
    @Test
    public void testPlaceCommand(){
        ByteArrayInputStream in = new ByteArrayInputStream("PLACE 0,0\nREPORT".getBytes());
        System.setIn(in);
        Simulation simulation = new Simulation();
        simulation.readInputStream();
        assertEquals("E:(0,0) B: ", testOutput.toString());

    }

    /**
     * Test PLACE & BLOCK commands
     */
    @Test
    public void testPlaceBlockCommands(){
        ByteArrayInputStream in = new ByteArrayInputStream("PLACE 0,0\nBLOCK 0,1\nBLOCK 0,3\nREPORT".getBytes());
        System.setIn(in);
        Simulation simulation = new Simulation();
        simulation.readInputStream();
        assertEquals("E:(0,0) B: (0,1) (0,3) ", testOutput.toString());

    }

    /**
     * Test multiple PLACE commands
     */
    @Test
    public void testMultiplePlaceCommand(){
        ByteArrayInputStream in = new ByteArrayInputStream("PLACE 0,0\nBLOCK 0,1\nBLOCK 0,3\nPLACE 0,1\nREPORT".getBytes());
        System.setIn(in);
        Simulation simulation = new Simulation();
        simulation.readInputStream();
        assertEquals("E:(0,1) B: ", testOutput.toString());

    }

    /**
     * Test EXPLORE command
     */
    @Test
    public void testExploreCommand(){
        ByteArrayInputStream in = new ByteArrayInputStream("PLACE 0,0\nBLOCK 0,2\nEXPLORE 0,3".getBytes());
        System.setIn(in);
        Simulation simulation = new Simulation();
        simulation.readInputStream();
        assertEquals("PATH: (0,0) (0,1) (1,1) (1,2) (1,3) (0,3) ", testOutput.toString());
    }

    /**
     * Test EXPLORE and REPORT command
     */
    @Test
    public void testReportCommand(){
        ByteArrayInputStream in = new ByteArrayInputStream("PLACE 0,0\nBLOCK 0,2\nEXPLORE 0,3\nREPORT".getBytes());
        System.setIn(in);
        Simulation simulation = new Simulation();
        simulation.readInputStream();
        assertEquals("PATH: (0,0) (0,1) (1,1) (1,2) (1,3) (0,3) E:(0,3) B: (0,2) ", testOutput.toString());
    }
}
