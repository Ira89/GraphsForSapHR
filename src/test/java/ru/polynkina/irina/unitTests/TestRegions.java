package ru.polynkina.irina.unitTests;

import org.junit.Assert;
import org.junit.*;
import java.util.*;
import ru.polynkina.irina.regions.RegionsContainer;

import javax.swing.*;

public class TestRegions {

    private static JCheckBox container = new JCheckBox();
    private static  List<String> ru = Arrays.asList("NORM", "NEP4", "SUT4");
    private static RegionsContainer regions;

    @BeforeClass
    public static void init() throws Exception {

        container.setText("RU");
        container.setSelected(true);
        regions = new RegionsContainer(container, ru);
    }

    @Test
    public void testDetNameRegion() {
        Assert.assertEquals("RU", (regions.getNameRegion(0)));
    }

    @Test
    public void generationNeededForRegion() {
        Assert.assertEquals(true, regions.generationNeededForRegion(0));
    }

    @Test
    public void graphUsedInRegion() {
        Assert.assertTrue(regions.graphUsedInRegion(0, "NORM"));
        Assert.assertFalse(regions.graphUsedInRegion(0, "ABCD"));
    }
}
