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
 * @version 1.3
 */
public class Item implements Parcelable {
    private String name;                        /* Name of the item */
    private double initPrice;                   /* Initial price of the item */
    private double currPrice;                   /* Current (last fetched) priced of the item */
    private double percChange;                  /* Percentage change from initial to current price */
    private String url;                         /* The Web URL of the item */
    private String dateAdded;                   /* String representation of the date the Item was created */
    private static final PriceFinder priceFinder = new PriceFinderClient();

    /* Date formatter for displaying dates */
    private static SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy", java.util.Locale.US);
    /* Decimal formatter for percentages */
    private static final DecimalFormat percFormatter = new DecimalFormat("#0.##%;- #0.##%");
    /* Decimal formatter for dollar values */
    private static final DecimalFormat dollarFormatter = new DecimalFormat("$#,##0.00");

    /**
     * The default constructor for an Item object.
     *
     * @param name The name of the item
     * @param url The string representation of the Web url containing the item to be encapsulated
     */
    public Item(String name, String url) {
        this.name = name;
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
    private static String calToDate(Calendar c) {
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
    private static String doubleToDollar(double d) {
        return dollarFormatter.format(d);
    }

    /**
     * Covert double into a string formatted as a percentage value.
     *
     * @param d The value of type double that will be formatted into a percentage string representation.
     * @return  The percentage string representation of the double value
     */
    private static String doubleToPerc(double d) {
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
    public static ArrayList<Item> getHW2items() {
        Item i0 = new Item("Nintendo Switch",
                49.99,
                49.99,
                0.0,
                "https://www.amazon.com/Nintendo-Switch-Neon-Blue-Red-Joy/dp/B01MUAGZ49?pd_rd_wg=zWezs&pd_rd_r=5d891ea2-7e13-4ae2-9191-092ec5165cd7&pd_rd_w=MXjJt&ref_=pd_gw_simh&pf_rd_r=ASV2VBE4NQ688YQR1E8D&pf_rd_p=a670abbe-a1ba-52d3-b360-3badcefeb448",
                calToDate(Calendar.getInstance()));

        Item i1 = new Item("Nintendo Switch Pro Controller",
                69.99,
                69.99,
                0.0,
                "https://www.amazon.com/Nintendo-Switch-Pro-Controller/dp/B01NAWKYZ0/ref=sr_1_3?ie=UTF8&qid=1532192674&sr=8-3&keywords=nintendo%2Bswitch%2Bpro&th=1",
                calToDate(Calendar.getInstance()));

        Item i2 = new Item("Super Mario Odyssey",
                19.99,
                19.99,
                0.0,
                "https://www.amazon.com/Super-Mario-Odyssey-Nintendo-Switch/dp/B01MY7GHKJ/ref=pd_sim_63_4?_encoding=UTF8&pd_rd_i=B01MY7GHKJ&pd_rd_r=66745a32-915c-11e8-8894-7b6c558f7022&pd_rd_w=TcFIY&pd_rd_wg=G4vbF&pf_rd_i=desktop-dp-sims&pf_rd_m=ATVPDKIKX0DER&pf_rd_p=a180fdfb-b54e-4904-85ba-d852197d6c09&pf_rd_r=RYTHNZKSAMW29HEJY1KE&pf_rd_s=desktop-dp-sims&pf_rd_t=40701&psc=1&refRID=RYTHNZKSAMW29HEJY1KE",
                calToDate(Calendar.getInstance()));

        ArrayList<Item> items = new ArrayList<>();
        items.add(i0);
        items.add(i1);
        items.add(i2);
        return items;
    }
}
