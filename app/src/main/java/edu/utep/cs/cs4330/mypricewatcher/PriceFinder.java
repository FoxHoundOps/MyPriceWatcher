package edu.utep.cs.cs4330.mypricewatcher;

import java.util.Random;

/**
 * A singleton object that is responsible for parsing through a web URL
 * containing an item, and retrieving information about the item. The PriceFinder
 * object can provide a String representation of the item's name and a double
 * representation of the item's price.
 *
 * @author Damian Najera
 * @version 1.0
 */
public class PriceFinder {
    private static final Random r = new Random();
    private static final double min = 0.0;
    private static final double max = 100.0;
    private static final PriceFinder instance = new PriceFinder();


    /**
     * The private constructor for the PriceFinder singleton.
     */
    private PriceFinder() {

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

    /**
     * Returns a string representation of the price of the item found within the url.
     *
     * @param url The Web url containing an item
     * @return    A string representation of the name of the item found within the url.
     */
    public String fetchName(String url) {
        return "Super Cool Item";
    }

    /**
     * Returns the singleton instance of a PriceFinder.
     *
     * @return  The PriceFinder singleton.
     */
    public static PriceFinder getInstance() {
        return instance;
    }
}
