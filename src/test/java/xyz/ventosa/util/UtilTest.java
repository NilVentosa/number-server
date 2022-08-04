package xyz.ventosa.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTest {

    @Test
    public void isNineDigitsTerminate() {
        Assertions.assertFalse(Util.isNineDigits("terminate"));
    }

    @Test
    public void isNineDigitsEmpty() {
        Assertions.assertFalse(Util.isNineDigits(""));
    }

    @Test
    public void isNineDigitsTooShort() {
        Assertions.assertFalse(Util.isNineDigits("88888888"));
    }

    @Test
    public void isNineDigitsTooLong() {
        Assertions.assertFalse(Util.isNineDigits("1010101010"));
    }

    @Test
    public void isNineDigitsLetters() {
        Assertions.assertFalse(Util.isNineDigits("sssssssss"));
    }

    @Test
    public void isNineDigitsCaseValid() {
        Assertions.assertTrue(Util.isNineDigits("123456789"));
    }

    @Test
    public void isNineDigitsCaseValidZeroes() {
        Assertions.assertTrue(Util.isNineDigits("000000000"));
    }
}