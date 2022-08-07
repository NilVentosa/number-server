package xyz.ventosa.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Util {

    public static Integer getValidNumber(String input) {
        int result;
        if (input == null) {
            return null;
        }
        if (input.length() != 9) {
            return null;
        }
        try {
            result = Integer.parseInt(input);
        }
        catch (NumberFormatException ignore) {
            return null;
        }
        return result;
    }

    public static boolean isTerminate(String line) {
        return "terminate".equals(line);
    }

}
