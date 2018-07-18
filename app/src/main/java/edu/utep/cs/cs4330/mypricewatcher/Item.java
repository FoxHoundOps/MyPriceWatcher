package edu.utep.cs.cs4330.mypricewatcher;

import java.util.Calendar;

/**
 * Encapsulates an item and information about it, including
 * the items initial price (the price when the Item object
 * is created, the current price (which will be updated and fetched by
 * the PriceFinder object, the percentage change (which will be updated
 * when the current price is updated, the web URL associated with the
 * item, and the a Calendar object related to when the object was created.
 *
 * @author Damian Najera
 * @version 1.0
 */
public class Item {
    private String name;
    private double initPrice;
    private double currPrice;
    private double percChange;
    private String url;
    private static final PriceFinder priceFinder = PriceFinder.getInstance();
    private Calendar dateAdded;


    /**
     * The default constructor for an Item object.
     *
     * @param url The string representation of the Web url containing the item to be encapsulated
     */
    public Item(String url) {
        name = priceFinder.fetchName(url);
        initPrice = priceFinder.fetchPrice(url);
        currPrice = initPrice;
        percChange = 0.0;
        this.url = url;
        dateAdded = Calendar.getInstance();
    }

    /**
     * Construct an Item object with all attributes specified.
     *
     * @param name          The name of the item
     * @param initPrice     The initial price of the item
     * @param currPrice     The current price of the item
     * @param percChange    The percentage change from the initial price to the current price
     * @param url           The url containing the item to be tracked
     * @param dateAdded     The Calendar instance associated to when the item began being tracked
     */
    public Item(String name, double initPrice, double currPrice, double percChange, String url, Calendar dateAdded) {
        this.name = name;
        this.initPrice = initPrice;
        this.currPrice = currPrice;
        this.percChange = percChange;
        this.url = url;
        this.dateAdded = dateAdded;
    }

    /**
     * Getter for Item's name.
     *
     * @return The string representation of the Item name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for Item's initial price.
     *
     * @return The double representation of the Item's initial price
     */
    public double getInitPrice() {
        return initPrice;
    }

    /**
     * Getter for Item's current price.
     *
     * @return The double representation of the Item's current price
     */
    public double getCurrPrice() {
        return currPrice;
    }

    /**
     * Getter for Item's percentage change from its initial price to its current price.
     *
     * @return The double representation of the Item's percentage change
     */
    public double getPercChange() {
        return percChange;
    }

    /**
     * Getter for Item's Calendar instance associated with the date it was created.
     *
     * @return The Calendar instance associate with the date it was created
     */
    public Calendar getDateAdded() {
        return dateAdded;
    }

    /**
     * Getter for the Item's url.
     *
     * @return The string representation of the Web url associated with the Item
     */
    public String getURL() {
        return url;
    }

    /**
     * Invokes the PriceFinder instance to fetch this Item's current price, and then updates
     * its current price and recalculated the percentage change.
     */
    public void fetchCurrPrice() {
        currPrice = priceFinder.fetchPrice(this.url);
        calcPercChange();
    }

    /**
     * Calculates the Item's percentage change for the initial price to the current price.
     */
    private void calcPercChange() {
            percChange = (currPrice - initPrice) / initPrice;
    }

}
