package edu.utep.cs.cs4330.mypricewatcher;

import java.util.Random;

/**
 * A PriceFinder is responsible for parsing through a web URL
 * containing an item, and retrieving information about the item. The PriceFinder
 * object can provide a double representation of the item's price.
 *
 * @author Damian Najera
 * @version 1.3
 */
public class PriceFinder {
    private static final Random r = new Random();
    private static final double min = 0.0;
    private static final double max = 100.0;

    /**
     * The constructor for a PriceFinder.
     */
    public PriceFinder() {

    }

    /**
     * Returns a double representation of the item found withing the url.
     *
     * @param url The Web url containing an item
     * @return    A double representation of the item's price
     */
    public double fetchPrice(String url) {
        return min + (max - min) * r.nextDouble();
    }
}
