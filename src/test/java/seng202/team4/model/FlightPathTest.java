package seng202.team4.model;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

public class FlightPathTest extends TestCase {

    @Test
    public void testInsertStatement() {
        FlightPath path = new FlightPath("FIX","ATMAP",35000,-12,118.255);
        String statement = path.getInsertStatement(10);
        assertEquals("INSERT INTO FlightPath ('TYPE', 'FLIGHTPATHID', 'ALTITUDE', 'LATITUDE', 'LONGITUDE', 'SETID') VALUES ('FIX', 'ATMAP', '35000', '-12.0', '118.255', '10');", statement);
    }

    @Test
    public void testGetValid1() {
        FlightPath path = (FlightPath) FlightPath.getValid("F1X","ATMAP","35000","-12","118.255", new ArrayList<>());
        assertEquals(null, path);
    }

    @Test
    public void testGetValid2() {
        FlightPath path = (FlightPath) FlightPath.getValid("FIX","ATM4P","35000","-12","118.255", new ArrayList<>());
        assertEquals(null, path);
    }

    @Test
    public void testGetValid4() {
        FlightPath path = (FlightPath) FlightPath.getValid("FIX","ATMAP","3500O","-12","118.255", new ArrayList<>());
        assertEquals(null, path);
    }

    @Test
    public void testGetValid5() {
        FlightPath path = (FlightPath) FlightPath.getValid("FIX","ATMAP","35000","-R","118.255", new ArrayList<>());
        assertEquals(null, path);
    }

    @Test
    public void testGetValid6() {
        FlightPath path = (FlightPath) FlightPath.getValid("FIX","ATMAP","35000","-12","118.25a", new ArrayList<>());
        assertEquals(null, path);
    }

    @Test
    public void testGetValid7() {
        FlightPath path = new FlightPath();
        FlightPath newpath = (FlightPath) path.getValid(new String[]{"FIX","ATMAP","35000","-12","118.25"}, new ArrayList<>());
        assertTrue(newpath.equalsTest(new FlightPath("FIX","ATMAP",35000,-12,118.25)));
    }

}