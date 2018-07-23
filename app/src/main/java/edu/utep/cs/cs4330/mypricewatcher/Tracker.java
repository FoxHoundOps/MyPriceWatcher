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
    private static ArrayList<Item> items;
    private static final Tracker instance = new Tracker();

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
     * @return The Tracker singleton.
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(items);
    }

    private Tracker(Parcel in) {
        items = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<Tracker> CREATOR = new Creator<Tracker>() {
        @Override
        public Tracker createFromParcel(Parcel in) {
            return new Tracker(in);
        }

        @Override
        public Tracker[] newArray(int size) {
            return new Tracker[size];
        }
    };
}
