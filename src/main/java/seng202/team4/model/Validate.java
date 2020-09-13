package seng202.team4.model;

public interface Validate {
    static boolean isNumeric(String string) {
        char[] chars = string.toCharArray();
        for (char c: chars) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    static boolean isAlpha(String string) {
        char[] chars = string.toCharArray();
        for (char c: chars) {
            if (!Character.isLetter(c) && !Character.isSpaceChar(c)) {
                return false;
            }
        }
        return true;
    }

    static boolean isAlphaNumeric(String string) {
        return isNumeric(string) || isAlpha(string);
    }

}
