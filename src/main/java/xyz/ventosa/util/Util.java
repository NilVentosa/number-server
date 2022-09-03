package xyz.ventosa.util;

import xyz.ventosa.server.NumberServerException;

public class Util {

    private Util() {

    }

    public static Integer getValidNumber(String input) throws NumberServerException {
        int result;
        if (input == null) {
            throw new NumberServerException("Connection closed by the client");
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

}
