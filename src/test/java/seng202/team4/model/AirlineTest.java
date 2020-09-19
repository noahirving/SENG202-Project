package seng202.team4.model;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

public class AirlineTest extends TestCase {

    @Test
    public void testInsertStatement() {
        Airline airline = new Airline("19675,\"Rainbow Air Canada\",\"Rainbow Air CAN\",\"RY\",\"RAY\",\"Rainbow CAN\",\"Canada\",\"Y\"");
        String statement = airline.getInsertStatement(10);
        assertEquals("INSERT INTO Airline ('NAME', 'ALIAS', 'IATA', 'ICAO', 'CALLSIGN', 'COUNTRY', 'RECENTLYACTIVE', 'SETID') VALUES ('Rainbow Air Canada', 'Rainbow Air CAN', 'RY', 'RAY', 'Rainbow CAN', 'Canada', 'true', '10');", statement);
    }

    @Test
    public void testGetValid1() {
        Airline airline = Airline.getValid("","Rainbow Air CAN","RY","RAY","Rainbow CAN","Canada","Y", new ArrayList<>());
        assertEquals(null, airline);
    }

    @Test
    public void testGetValid2() {
        Airline airline = Airline.getValid("Rainbow Air Canada","r@inb0w 4!r","RY","RAY","Rainbow CAN","Canada","Y", new ArrayList<>());
        assertEquals(null, airline);
    }

    @Test
    public void testGetValid3() {
        Airline airline = Airline.getValid("Rainbow Air Canada","Rainbow Air CAN","RYAN","RAY","Rainbow CAN","Canada","Y", new ArrayList<>());
        assertEquals(null, airline);
    }

    @Test
    public void testGetValid4() {
        Airline airline = Airline.getValid("Rainbow Air Canada","Rainbow Air CAN","RY","RAYAB","Rainbow CAN","Canada","Y", new ArrayList<>());
        assertEquals(null, airline);
    }

    @Test
    public void testGetValid5() {
        Airline airline = Airline.getValid("Rainbow Air Canada","Rainbow Air CAN","RY","RAY","Ra!nbow C4N","Canada","Y", new ArrayList<>());
        assertEquals(null, airline);
    }

    @Test
    public void testGetValid6() {
        Airline airline = Airline.getValid("Rainbow Air Canada","Rainbow Air CAN","RY","RAY","Rainbow CAN","C4nada","Y", new ArrayList<>());
        assertEquals(null, airline);
    }

    @Test
    public void testGetValid7() {
        Airline airline = Airline.getValid("Rainbow Air Canada","Rainbow Air CAN","RY","RAY","Rainbow CAN","Canada","S", new ArrayList<>());
        assertEquals(null, airline);
    }

    @Test
    public void testGetValid8() {
        Airline airline = new Airline();
        Airline newline = (Airline) airline.getValid(new String[]{"Rainbow Air Canada","Rainbow Air CAN","RY","RAY","Rainbow CAN","Canada","Y"}, new ArrayList<>());
        assertTrue(newline.equalsTest(new Airline("Rainbow Air Canada","Rainbow Air CAN","RY","RAY","Rainbow CAN","Canada",true)));
    }
}