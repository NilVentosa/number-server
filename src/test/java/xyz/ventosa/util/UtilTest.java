package xyz.ventosa.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    void isValidNumber_valid() {
        assertEquals(0, Util.getValidNumber("000000000"));
        assertEquals(1, Util.getValidNumber("000000001"));
        assertEquals(123456789, Util.getValidNumber("123456789"));
    }

    @Test
    void isValidNumber_invalid() {
        assertNull(Util.getValidNumber("2"));
        assertNull(Util.getValidNumber("d"));
        assertNull(Util.getValidNumber("carbonara"));
        assertNull(Util.getValidNumber("TERMINATE"));
        assertNull(Util.getValidNumber(null));
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