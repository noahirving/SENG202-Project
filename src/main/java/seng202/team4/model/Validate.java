package seng202.team4.model;

public interface Validate {
    static final String NULL = "\\N";

    static boolean isFloat(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("-?\\d+(\\.\\d+)?");
    }

    static boolean isInteger(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("-?\\d+");
    }

    static boolean isAlpha(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("[A-Za-z][A-Za-z\\s]*");
    }

    static boolean isAlphaMultiLanguage(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("[-\\a-zA-Z0-9 \'.\u0080-\u9fff]*+");
    }

    static boolean isAlphaNumeric(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("[A-Za-z0-9\\s]*");
    }

    static boolean isAirportIATA(String string) {
        if (string == null) {
            return false;
        }
        return string.equals("") || string.equals(NULL) || string.matches("[a-zA-Z0-9а-яА-Я%]{3}");
    }

    static boolean isAirportICAO(String string) {
        if (string == null) {
            return false;
        }
        return string.equals("") || string.equals(NULL) || string.matches("[a-zA-Z0-9-_%]{3,4}");
    }

    static boolean isAirlineIATA(String string) {
        if (string == null) {
            return false;
        }
        return string.equals("") || string.equals(NULL) || string.matches("[-\\a-zA-Z0-9а-яА-Я +^;:&.?!]*");
    }

    static boolean isAirlineICAO(String string) {
        if (string == null) {
            return false;
        }
        return string.equals("") || string.equals(NULL) || string.matches("[-\\a-zA-Z0-9а-яА-Я '*/+=:&.?!]*");
    }
    static boolean isValidTZDB(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("[a-zA-Z]+/[a-zA-Z_-]+") || string.equals(NULL);
    }

    static boolean isValidTimeZone(String string) {
        try {
            double timezone = Double.parseDouble(string);
            if (timezone >= -12 && timezone <= 14) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    static boolean isAsciiOrNull(String string) {
        return string.equals(NULL) || string.matches("^\\p{ASCII}*$");
    }
}
