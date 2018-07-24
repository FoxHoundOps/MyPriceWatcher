package edu.utep.cs.cs4330.mypricewatcher;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Encapsulates Item objects in an internal data structure. The Tracker singleton
 * will provide adding/removing of Item objects. The Tracker also provides a
 * way for each Item to have its price updated.
 *
 * @author Damian Najera
 * @version 1.1
 */
public class Tracker implements Parcelable{
    private static ArrayList<Item> items;                   /* Tracker's internal list of Items */
    private static final Tracker instance = new Tracker();  /* Tracker's singleton */

    /**
     * The constructor for a Tracker object.
     */
    private Tracker() {
        items = new Item("").getHW2items();
    }

    /**
     * Add an Item object into the tracker's internal list of Item objects to track.
     *
     * @param url The string representation of the Web url containing the item to be tracked
     * @return The Item object encapsulating the item added to the tracker.
     */
    public Item addItem(String url) {
        Item i = new Item(url);
        items.add(i);
        return i;
    }

    /**
     * Remove an Item object from the tracker's internal list of Item objects to track.
     *
     * @param item The item to remove
     */
    public void removeItem(Item item) {
        items.remove(item);
    }

    /**
     * Clear the internal list of Item objects.
     */
    public void clearItemsList() {
        items.clear();
    }

    /**
     * Invoke every item in the internal list of Item objects to update its price.
     */
    public void updatePrices() {
        for (Item i: items)
            i.fetchCurrPrice();
    }

    /**
     * Returns the Tracker singleton object.
     *
     * @return The Tracker singleton
     */
    public static Tracker getInstance() {
        return instance;
    }

    /**
     * Returns the ArrayList containing the Item objects being tracked.
     *
     * @return The ArrayList encapsulating the Item objects being tracked.
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * This method is not used, and it not meant to be used.
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Implementation for writing an Tracker to a Parcel
     *
     * {@inheritDoc}
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(items);
    }

    /**
     * The constructor used when creating a Tracker from a Parcel object.
     *
     * @param in The Parcel object encapsulating the Item object
     */
    private Tracker(Parcel in) {
        items = in.createTypedArrayList(Item.CREATOR);
    }

    /**
     * Creator used for reconstructing an Item from a Parcel object.
     */
    public static final Creator<Tracker> CREATOR = new Creator<Tracker>() {
        /**
         * {@inheritDoc}
         */
        @Override
        public Tracker createFromParcel(Parcel in) {
            return new Tracker(in);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Tracker[] newArray(int size) {
            return new Tracker[size];
        }
    };
}
