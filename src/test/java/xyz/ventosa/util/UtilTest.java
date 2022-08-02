package xyz.ventosa.util;


import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

    @Test
    public void isNineDigitsTerminate() {
        Assert.assertFalse(Util.isNineDigits("terminate"));
    }

    @Test
    public void isNineDigitsEmpty() {
        Assert.assertFalse(Util.isNineDigits(""));
    }

    @Test
    public void isNineDigitsTooShort() {
        Assert.assertFalse(Util.isNineDigits("88888888"));
    }

    @Test
    public void isNineDigitsTooLong() {
        Assert.assertFalse(Util.isNineDigits("1010101010"));
    }

    @Test
    public void isNineDigitsLetters() {
        Assert.assertFalse(Util.isNineDigits("sssssssss"));
    }

    @Test
    public void isNineDigitsCaseValid() {
        Assert.assertTrue(Util.isNineDigits("123456789"));
    }

    @Test
    public void isNineDigitsCaseValidZeroes() {
        Assert.assertTrue(Util.isNineDigits("000000000"));
    }
}