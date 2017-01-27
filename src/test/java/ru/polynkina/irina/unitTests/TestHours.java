package ru.polynkina.irina.unitTests;

import org.junit.*;
import ru.polynkina.irina.hours.*;

public class TestHours {

    private static Hours libHours;

    @BeforeClass
    public static void init() throws Exception {
        libHours = new LibHours();
    }

    @Test
    public void findSignDayHours() throws Exception {
        Assert.assertEquals("4C15", libHours.findSignDayHours(15));
        Assert.assertEquals("FREE", libHours.findSignDayHours(0));
        Assert.assertEquals("NO62", libHours.findSignDayHours(6.2));
    }

    @Test(expected = Exception.class)
    public void findIncorrectNegativeDayHours() throws Exception {
        libHours.findSignDayHours(-3);
    }

    @Test(expected = Exception.class)
    public void findIncorrectPositiveDayHours() throws Exception {
        libHours.findSignDayHours(24);
    }

    @Test
    public void findSignNightHours() throws Exception {
        Assert.assertEquals("SUTK", libHours.findSignNightHours(22));
        Assert.assertEquals("FREE", libHours.findSignNightHours(0));
        Assert.assertEquals("4ON5", libHours.findSignNightHours(5));
    }

    @Test(expected = Exception.class)
    public void findIncorrectNegativeNighrHours() throws Exception {
        libHours.findSignNightHours(-8);
    }

    @Test(expected = Exception.class)
    public void findIncorrectPositiveNightHours() throws Exception {
        libHours.findSignNightHours(23);
    }
}
