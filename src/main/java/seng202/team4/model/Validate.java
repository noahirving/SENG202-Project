package seng202.team4.model;

public interface Validate {
    static boolean isNumeric(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("-?\\d+(\\.\\d+)?");
    }

    static boolean isAlpha(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("[A-Za-z][A-Za-z\\s]*");
    }

    static boolean isAlphaNumeric(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("[A-Za-z0-9][A-Za-z0-9\\s]*");
    }

    static boolean isValidIATA(String string) {
        if (string == null) {
            return false;
        } else if (string.equals("")) {
            return true; //for not assigned/unknown iata
        }
        return string.matches("[a-zA-Z]{2,3}");
    }

    static boolean isValidICAO(String string) {
        if (string == null) {
            return false;
        } else if (string.equals("")) {
            return true; //for not assigned icao
        }
        return string.matches("[a-zA-Z]{3,4}");
    }

    static boolean isValidTZDB(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("[a-zA-Z]+/[a-zA-Z_-]+");
    }

}
