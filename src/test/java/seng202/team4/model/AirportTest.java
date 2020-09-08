package seng202.team4.model;

import junit.framework.TestCase;
import org.junit.Test;

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
}