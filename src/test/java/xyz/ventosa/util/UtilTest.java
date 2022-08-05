package xyz.ventosa.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void isValidNumber_valid() {
        assertTrue(Util.isValidNumber("000000000"));
        assertTrue(Util.isValidNumber("000000001"));
        assertTrue(Util.isValidNumber("123456789"));
    }

    @Test
    void isValidNumber_invalid() {
        assertFalse(Util.isValidNumber("2"));
        assertFalse(Util.isValidNumber("d"));
        assertFalse(Util.isValidNumber("carbonara"));
        assertFalse(Util.isValidNumber("TERMINATE"));
        assertFalse(Util.isValidNumber(null));
    }

    @Test
    void isValidTerminate_true() {
        assertTrue(Util.isTerminate("terminate"));
    }

    @Test
    void isValidTerminate_false() {
        assertFalse(Util.isTerminate("TERMINATE"));
        assertFalse(Util.isTerminate("TerminatE"));
        assertFalse(Util.isTerminate("terminates"));
        assertFalse(Util.isTerminate(null));
        assertFalse(Util.isTerminate("123456789"));
    }

}