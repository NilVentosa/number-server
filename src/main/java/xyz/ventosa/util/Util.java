package xyz.ventosa.util;

public class Util {

    private Util(){}

    public static boolean isNineDigits(String input) {
        if (input.length() != 9) {
            return false;
        }
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException ignore) {
            return false;
        }
        return true;
    }

}
