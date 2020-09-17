package seng202.team4.model;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;

public class RouteTest extends TestCase {

    @Test
    public void testInsertStatement() {
        Route route = new Route("BA,1355,SIN,3316,MEL,3339,Y,0,744");
        String statement = route.getInsertStatement(10);
        assertEquals("INSERT INTO Route ('AIRLINE', 'AIRLINEID', 'SourceAirport', 'SOURCEAIRPORTID', 'DESTINATIONAIRPORT', 'DESTINATIONAIRPORTID', 'CODESHARE', 'STOPS', 'EQUIPMENT', 'DISTANCE', 'SETID') VALUES ('BA', '1355', 'SIN', '3316', 'MEL', '3339', 'true', '0', '744', '0.0', '10');", statement);
    }

    @Test
    public void testGetValid1() {
        Route route = Route.getValid("BA!!","SIN","MEL","Y","0","744", new ArrayList<>());
        assertEquals(null, route);
    }

    @Test
    public void testGetValid2() {
        Route route = Route.getValid("BA","SIN!!","MEL","Y","0","744", new ArrayList<>());
        assertEquals(null, route);
    }

    @Test
    public void testGetValid3() {
        Route route = Route.getValid("BA","SIN","MEL!!","Y","0","744", new ArrayList<>());
        assertEquals(null, route);
    }

    @Test
    public void testGetValid4() {
        Route route = Route.getValid("BA","SIN","MEL","S","0","744", new ArrayList<>());
        assertEquals(null, route);
    }

    @Test
    public void testGetValid5() {
        Route route = Route.getValid("BA","SIN","MEL","Y","0a","744", new ArrayList<>());
        assertEquals(null, route);
    }

    @Test
    public void testGetValid6() {
        Route route = Route.getValid("BA","SIN","MEL","Y","0","744@", new ArrayList<>());
        assertEquals(null, route);
    }

    @Test
    public void testGetValid7() {
        Route route = new Route();
        Route newroute = (Route) route.getValid(new String[]{"BA","SIN","MEL","Y","0","744"}, new ArrayList<>());
        assertTrue(newroute.equals(new Route("BA,1355,SIN,3316,MEL,3339,Y,0,744")));
    }

}