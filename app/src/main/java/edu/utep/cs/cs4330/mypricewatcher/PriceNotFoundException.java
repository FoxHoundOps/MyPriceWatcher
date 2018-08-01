package edu.utep.cs.cs4330.mypricewatcher;

/**
 *  A custom RuntimeException to throw in order to recognize those specific occurrences where a
 *  price is unable to be retrieved from the PriceFinder.
 *
 * @author Damian Najera
 * @version 1.0
 */
public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException() {
        super("Unable to fetch price.");
    }
}
