package xyz.ventosa.util;

public class Util {

    public static boolean isValidNumber(String input) {
        if (input == null) {
            return false;
        }
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

    public static boolean isTerminate(String line) {
        return "terminate".equals(line);
    }

    private Util(){}
}
