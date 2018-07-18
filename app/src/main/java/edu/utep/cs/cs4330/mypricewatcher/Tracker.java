package edu.utep.cs.cs4330.mypricewatcher;

import java.util.ArrayList;


/**
 * Encapsulates Item objects in an internal data structure. The Tracker singleton
 * will provide adding/removing of Item objects. The Tracker also provides a
 * way for each Item to have its price updated.
 *
 * @author Damian Najera
 * @version 1.0
 */
public class Tracker {
    private ArrayList<Item> items;
    private static final Tracker instance = new Tracker();

    /**
     * The constructor for a Tracker object.
     */
    private Tracker() {
        items = new ArrayList<>();
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
     * @param itemName The item name.
     */
    public void removeItem(String itemName) {
        for (Item i : items)
            if (i.getName().equals(itemName)) {
                items.remove(i);
                break;
            }
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
}
