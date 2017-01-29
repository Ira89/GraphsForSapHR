package ru.polynkina.irina.unitTests;

import org.junit.Assert;
import org.junit.*;
import java.util.*;
import ru.polynkina.irina.regions.RegionsContainer;

public class TestRegions {

    private static final int AMOUNT_REGIONS = 9;
    private static RegionsContainer regions;
    private static List<String> nameRegions = new ArrayList<String>();
    private static int indexRegionRU;

    @BeforeClass
    public static void init() throws Exception {
        regions = new RegionsContainer();
        nameRegions.add("RU");

        for(int indexRegion = 0; indexRegion < AMOUNT_REGIONS; ++indexRegion) {
            if(nameRegions.contains(regions.getNameRegion(indexRegion))) {
                indexRegionRU = indexRegion;
                break;
            }
        }
    }

    @Test
    public void getAmountRegion() {
        Assert.assertEquals(AMOUNT_REGIONS, regions.getAmountRegions());
    }

    @Test
    public void getNameRegion() {
        Assert.assertEquals("RU", (regions.getNameRegion(indexRegionRU)));
    }

    @Test
    public void generationNeededForRegion() {
        Assert.assertEquals(true, regions.generationNeededForRegion(indexRegionRU));
    }

    @Test
    public void graphUsedInRegion() {
        Assert.assertTrue(regions.graphUsedInRegion(0, "NORM"));
        Assert.assertFalse(regions.graphUsedInRegion(0, "ABCD"));
    }
}
