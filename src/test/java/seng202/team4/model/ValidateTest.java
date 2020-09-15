package seng202.team4.model;

import junit.framework.TestCase;

public class ValidateTest extends TestCase {

    public void testIsNumeric1() {
        String num1 = "123";
        assertTrue(Validate.isNumeric(num1));
    }

    public void testIsNumeric2() {
        String num2 = "1.23";
        assertTrue(Validate.isNumeric(num2));
    }

    public void testIsNumeric3() {
        String num3 = "-123";
        assertTrue(Validate.isNumeric(num3));
    }

    public void testIsNumeric4() {
        String num4 = "-1.23";
        assertTrue(Validate.isNumeric(num4));
    }

    public void testIsNumeric5() {
        String num5 = "a.23";
        assertFalse(Validate.isNumeric(num5));
    }

    public void testIsNumeric6() {
        String num6 = null;
        assertFalse(Validate.isNumeric(num6));
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
        assertTrue(Validate.isValidIATA(iata1));
    }

    public void testIsValidIATA2() {
        String iata2 = "AA";
        assertFalse(Validate.isValidIATA(iata2));
    }

    public void testIsValidIATA3() {
        String iata3 = "";
        assertTrue(Validate.isValidIATA(iata3));
    }

    public void testIsValidICAO1() {
        String icao1 = "ABCD";
        assertTrue(Validate.isValidICAO(icao1));
    }

    public void testIsValidICAO2() {
        String icao2 = "AB";
        assertFalse(Validate.isValidICAO(icao2));
    }

    public void testIsValidICAO3() {
        String icao3 = "";
        assertTrue(Validate.isValidICAO(icao3));
    }
}