package seng202.team4.model;

import junit.framework.TestCase;
import org.junit.Test;
import java.util.ArrayList;

public class AirportTest extends TestCase {

    @Test
    public void testSetCoordinates() {
        Airport airport = new Airport();
        airport.setCoordinates(42.0, 13.5);
        String coordinates = airport.getCoordinates();
        assertEquals("13.5000, 42.0000", coordinates);
    }

    @Test
    public void testInsertStatement() {
        Airport airport = new Airport("10,\"Thule Air Base\",\"Thule\",\"Greenland\",\"THU\",\"BGTL\",76.531203,-68.703161,251,-4,\"E\",\"America/Thule\"");
        String statement = airport.getInsertStatement(5);
        assertEquals("INSERT INTO AIRPORT ('NAME', 'CITY', 'COUNTRY', 'IATA', 'ICAO', 'LATITUDE', 'LONGITUDE', 'ALTITUDE', 'TIMEZONE', 'DST', 'TZDATABASETIME', 'SETID') " +
                "VALUES ('Thule Air Base', 'Thule', 'Greenland', 'THU', 'BGTL', '76.531203', '-68.703161', '251.0', '-4.0', 'E', 'America/Thule', '5');", statement);
    }

    @Test
    public void testGetValid1() {
        Airport airport = Airport.getValid("","Thule","Greenland","THU","BGTL","76.531203","-68.703161","251","-4","E","America/Thule", new ArrayList<>());
        assertEquals(null, airport);
    }

    @Test
    public void testGetValid2() {
        Airport airport = Airport.getValid("Thule Air Base","123","Greenland","THU","BGTL","76.531203","-68.703161","251","-4","E","America/Thule", new ArrayList<>());
        assertEquals(null, airport);
    }

    @Test
    public void testGetValid3() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","123","THU","BGTL","76.531203","-68.703161","251","-4","E","America/Thule", new ArrayList<>());
        assertEquals(null, airport);
    }

    @Test
    public void testGetValid4() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","T","BGTL","76.531203","-68.703161","251","-4","E","America/Thule", new ArrayList<>());
        assertEquals(null, airport);
    }

    @Test
    public void testGetValid5() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","B","76.531203","-68.703161","251","-4","E","America/Thule", new ArrayList<>());
        assertEquals(null, airport);
    }

    @Test
    public void testGetValid7() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","BGTL","76.a","-68.703161","251","-4","E","America/Thule", new ArrayList<>());
        assertEquals(null, airport);
    }

    @Test
    public void testGetValid8() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","BGTL","76.531203","-68fb","251","-4","E","America/Thule", new ArrayList<>());
        assertEquals(null, airport);
    }

    @Test
    public void testGetValid9() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","BGTL","76.531203","-68.703161","1d","-4","E","America/Thule", new ArrayList<>());
        assertEquals(null, airport);
    }

    @Test
    public void testGetValid10() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","BGTL","76.531203","-68.703161","251","-u","E","America/Thule", new ArrayList<>());
        assertEquals(null, airport);
    }

    @Test
    public void testGetValid11() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","BGTL","76.531203","-68.703161","251","-4","h","America/Thule", new ArrayList<>());
        assertEquals(null, airport);
    }

    @Test
    public void testGetValid12() {
        Airport airport = Airport.getValid("Thule Air Base","Thule","Greenland","THU","BGTL","76.531203","-68.703161","251","-4","h","America", new ArrayList<>());
        assertEquals(null, airport);
    }


}