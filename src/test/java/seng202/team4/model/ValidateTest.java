package seng202.team4.model;

import junit.framework.TestCase;

public class ValidateTest extends TestCase {

    public void testIsNumeric1() {
        String num1 = "123";
        assertTrue(Validate.isInterger(num1));
    }

    public void testIsNumeric2() {
        String num2 = "1.23";
        assertTrue(Validate.isFloat(num2));
    }

    public void testIsNumeric3() {
        String num3 = "-123";
        assertTrue(Validate.isInterger(num3));
    }

    public void testIsNumeric4() {
        String num4 = "-1.23";
        assertTrue(Validate.isFloat(num4));
    }

    public void testIsNumeric5() {
        String num5 = "a.23";
        assertFalse(Validate.isFloat(num5));
    }

    public void testIsNumeric6() {
        String num6 = null;
        assertFalse(Validate.isInterger(num6));
    }

    public void testIsAlpha1() {
        String str1 = "A";
        assertTrue(Validate.isAlpha(str1));
    }

    public void testIsAlpha2() {
        String str2 = "Air Port";
        assertTrue(Validate.isAlpha(str2));
    }

    public void testIsAlpha3() {
        String str3 = "";
        assertFalse(Validate.isAlpha(str3));
    }

    public void testIsAlpha4() {
        String str4 = "Airport 4";
        assertFalse(Validate.isAlpha(str4));
    }

    public void testIsAlpha5() {
        String str5 = "   ";
        assertFalse(Validate.isAlpha(str5));
    }

    public void testIsAlpha6() {
        String str6 = null;
        assertFalse(Validate.isAlpha(str6));
    }

    public void testIsAlphaNumeric1() {
        String st1 = "Airport 123";
        assertTrue(Validate.isAlphaNumeric(st1));
    }

    public void testIsAlphaNumeric2() {
        String st2 = "Airport 123!$%";
        assertFalse(Validate.isAlphaNumeric(st2));
    }

    public void testIsAlphaNumeric3() {
        String st3 = null;
        assertFalse(Validate.isAlphaNumeric(st3));
    }

    public void testIsValidIATA1() {
        String iata1 = "ABC";
        assertTrue(Validate.isAirportIATA(iata1));
    }

    public void testIsValidIATA2() {
        String iata2 = "AA";
        assertTrue(Validate.isAirlineIATA(iata2));
    }

    public void testIsValidIATA3() {
        String iata3 = "";
        assertTrue(Validate.isAirportIATA(iata3));
    }

    public void testIsValidIATA4() {
        String iata4 = null;
        assertFalse(Validate.isAirportIATA(iata4));
    }

    public void testIsValidIATA5() {
        String iata5 = "ABCD";
        assertFalse(Validate.isAirportIATA(iata5));
    }

    public void testIsValidICAO1() {
        String icao1 = "ABCD";
        assertTrue(Validate.isAirportICAO(icao1));
    }

    public void testIsValidICAO2() {
        String icao2 = "AB";
        assertFalse(Validate.isAirportICAO(icao2));
    }

    public void testIsValidICAO3() {
        String icao3 = "";
        assertTrue(Validate.isAirportICAO(icao3));
    }

    public void testIsValidICAO4() {
        String icao4 = null;
        assertFalse(Validate.isAirportICAO(icao4));
    }

    public void testIsValidICAO5() {
        String icao5 = "ABC";
        assertTrue(Validate.isAirlineICAO(icao5));
    }

    public void testIsValidTZDB1() {
        String tzdb1 = "America/Thule";
        assertTrue(Validate.isValidTZDB(tzdb1));
    }

    public void testIsValidTZDB2() {
        String tzdb2 = "America";
        assertFalse(Validate.isValidTZDB(tzdb2));
    }

    public void testIsValidTZDB3() {
        String tzdb3 = "Amer1ca/Thul3";
        assertFalse(Validate.isValidTZDB(tzdb3));
    }

    public void testIsValidTZDB4() {
        String tzdb4 = null;
        assertFalse(Validate.isValidTZDB(tzdb4));
    }

    public void testIsValidTimeZone1() {
        String tz = "aa";
        assertFalse(Validate.isValidTimeZone(tz));
    }

    public void testIsValidTimeZone2() {
        String tz = "-13";
        assertFalse(Validate.isValidTimeZone(tz));
    }

    public void testIsValidTimeZone3() {
        String tz = "15";
        assertFalse(Validate.isValidTimeZone(tz));
    }

    public void testIsValidTimeZone4() {
        String tz = "3";
        assertTrue(Validate.isValidTimeZone(tz));
    }

    public void testIsValidTimeZone5() {
        String tz = null;
        assertFalse(Validate.isValidTimeZone(tz));
    }
}