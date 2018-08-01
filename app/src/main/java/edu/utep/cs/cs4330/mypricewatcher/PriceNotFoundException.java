package edu.utep.cs.cs4330.mypricewatcher;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException() {
        super("Unable to fetch price.");
    }
}
