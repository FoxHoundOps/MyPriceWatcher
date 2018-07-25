package edu.utep.cs.cs4330.mypricewatcher;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Encapsulates an item and information about it, including
 * the items initial price (the price when the Item object
 * is created, the current price (which will be updated and fetched by
 * the PriceFinder object, the percentage change (which will be updated
 * when the current price is updated, the web URL associated with the
 * item, and the a String representation of the date when the object was created.
 *
 * @author Damian Najera
 * @version 1.2
 */
public class Item implements Parcelable{
    private String name;                        /* Name of the item */
    private double initPrice;                   /* Initial price of the item */
    private double currPrice;                   /* Current (last fetched) priced of the item */
    private double percChange;                  /* Percentage change from initial to current price */
    private String url;                         /* The Web URL of the item */
    private String dateAdded;                   /* String representation of the date the Item was created */
    private static final PriceFinder priceFinder = PriceFinder.getInstance();

    /* Date formatter for displaying dates */
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy", java.util.Locale.US);
    /* Decimal formatter for percentages */
    private static final DecimalFormat percFormatter = new DecimalFormat("#0.##%;- #0.##%");
    /* Decimal formatter for dollar values */
    private DecimalFormat dollarFormatter = new DecimalFormat("$#,##0.00");

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
        dateAdded = calToDate(Calendar.getInstance());
    }

    /**
     * The constructor used when creating an Item from a Parcel object.
     *
     * @param in The Parcel object encapsulating the Item object
     */
    private Item(Parcel in) {
        this.name = in.readString();
        this.initPrice = in.readDouble();
        this.currPrice = in.readDouble();
        this.percChange = in.readDouble();
        this.url = in.readString();
        this.dateAdded = in.readString();
    }

    /**
     * Creator used for reconstructing an Item from a Parcel object.
     */
    public static final Creator<Item> CREATOR = new Creator<Item>() {
        /**
         * {@inheritDoc}
         */
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    /**
     * This method is not used, and it not meant to be used.
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Implementation for writing an Item to a Parcel.
     *
     * {@inheritDoc}
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(initPrice);
        parcel.writeDouble(currPrice);
        parcel.writeDouble(percChange);
        parcel.writeString(url);
        parcel.writeString(dateAdded);
    }

    /**
     * Convert Calendar instance into a string representation: MM/dd/yy.
     *
     * @param c The Calendar instance whose date will be return as a string representation
     * @return  The string representation of the Calendar instance's date.
     */
    private String calToDate(Calendar c) {
        return dateFormatter.format(c.getTime());
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
     * Setter for Item's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for Item's initial price.
     *
     * @return The string representation of the Item's initial price
     */
    public String getInitPrice() {
        return doubleToDollar(initPrice);
    }

    /**
     * Getter for Item's current price.
     *
     * @return The string representation of the Item's current price
     */
    public String getCurrPrice() {
        return doubleToDollar(currPrice);
    }

    /**
     * Getter for Item's percentage change from its initial price to its current price.
     *
     * @return The string representation of the Item's percentage change
     */
    public String getPercChange() {
        return doubleToPerc(percChange);
    }

    /**
     * Getter for Item's date added string representation associated with the date it was created.
     *
     * @return The Calendar instance associate with the date it was created
     */
    public String getDateAdded() {
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
     * Setter for the Item's url.
     */
    public void setURL(String url) {
        this.url = url;
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

    /**
     * Convert double into a string fomratted as a dollar value.
     *
     * @param d The value of type double that will be formatted into a dollar string representation
     * @return  The dollar value string representation of the double value
     */
    private String doubleToDollar(double d) {
        return dollarFormatter.format(d);
    }

    /**
     * Covert double into a string formatted as a percentage value.
     *
     * @param d The value of type double that will be formatted into a percentage string representation.
     * @return  The percentage string representation of the double value
     */
    private String doubleToPerc(double d) {
        return percFormatter.format(d);
    }

    /**
     * Private constructor used for HW2, in order to create predefined items.
     *
     * @param name          The Item's name
     * @param initPrice     The Item's initial price
     * @param currPrice     The Item's current price
     * @param percChange    The Item's percentage change from initial price to current price
     * @param url           The Item's web URL
     * @param dateAdded     The date the Item was created
     */
    private Item(String name, double initPrice, double currPrice, double percChange, String url, String dateAdded) {
        this.name = name;
        this.initPrice = initPrice;
        this.currPrice = currPrice;
        this.percChange = percChange;
        this.url = url;
        this.dateAdded = dateAdded;
    }

    /**
     * Method used to demonstrate My Price Watcher app for HW purposes.
     *
     * @return An ArrayList containing 3 Items
     */
    public ArrayList<Item> getHW2items() {
        Item i0 = new Item("Avengers Marvel Legends Series Infinity Gauntlet Articulated Electronic Fist ",
                49.99,
                49.99,
                0.0,
                "https://www.amazon.com/Avengers-Infinity-Gauntlet-Articulated-Electronic/dp/B071WT4KLM/ref=sr_1_1?ie=UTF8&qid=1532192445&sr=8-1&keywords=infinity+gauntlet",
                calToDate(Calendar.getInstance()));

        Item i1 = new Item("Nintendo Switch Pro Controller",
                69.99,
                69.99,
                0.0,
                "https://www.amazon.com/Nintendo-Switch-Pro-Controller/dp/B01NAWKYZ0/ref=sr_1_3?ie=UTF8&qid=1532192674&sr=8-3&keywords=nintendo%2Bswitch%2Bpro&th=1",
                calToDate(Calendar.getInstance()));

        Item i2 = new Item("Goodie Two Sleeves Men's Humor Cat Rides Llamacorn Adult T-Shirt ",
                19.99,
                19.99,
                0.0,
                "https://www.amazon.com/Goodie-Two-Sleeves-Llamacorn-Sublimated/dp/B01N951XG1/ref=sr_1_12?s=apparel&ie=UTF8&qid=1532192870&sr=1-12&nodeID=7141123011&psd=1&keywords=cat+shirt",
                calToDate(Calendar.getInstance()));

        ArrayList<Item> items = new ArrayList<>();
        items.add(i0);
        items.add(i1);
        items.add(i2);
        return items;
    }
}
