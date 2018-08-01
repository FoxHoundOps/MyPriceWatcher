package edu.utep.cs.cs4330.mypricewatcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * The Main Activity features the viewing and managing of a list of items whose prices will be
 * tracked by this My Price Watcher app. Every item that a user wants to keep track of will be
 * shown in this list. Each row in the list will show the item's name, initial price, and current
 * price. Each row contains a context menu that allows a user to refresh an item's current price,
 * edit the item's name or URL, and delete the item from the list. The menu bar for this Main
 * Activity also has two addition items: 1) a refresh button that, when clicked, will refresh the
 * current price for all the items in the list 2) an add button that, when clicked, will display
 * an AddItemDialog, so that the item's url can be entered, and the item can be added to the list.
 *
 * @author Damian Najera
 * @version 1.2
 */
public class MainActivity extends AppCompatActivity implements DeleteDialogListener, ManageItemDialogListener {
    private Tracker tracker;                    /* Internal tracker for all items */
    private ItemListAdapter itemsAdapter;       /* The adapter for the ListView */
    private int selectedPosition;               /* Selected context menu option position */
    private DBHandler dbHandler;                /* DB handler for storing tracked items */

    /**
     * Private class for a custom ArrayAdapter: ItemListAdapter. This custom adapter defines
     * what list is kept track of in a ListView, and what a single row in the ListView looks like.
     */
    private class ItemListAdapter extends ArrayAdapter<Item> {
        private final List<Item> items;     /* Internal list of items to manage displaying */

        /**
         * Private constructor for the ItemListAdapter.
         *
         * @param ctx       The Context that invoked the creation of this ItemListAdapter
         * @param items     The List to display
         */
        private ItemListAdapter(Context ctx, List<Item> items) {
            super(ctx, android.R.layout.simple_list_item_1, items);
            this.items = items;
        }

        /**
         * Override the getView() method in order to define what a single Item row looks like.
         *
         * {@inheritDoc}
         */
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
            Item item = items.get(position);
            TextView view = row.findViewById(R.id.item_name);
            view.setText(item.getName());
            view = row.findViewById(R.id.initial_price);
            view.setText(item.getInitPrice());
            view = row.findViewById(R.id.current_price);
            view.setText(item.getCurrPrice());

            row.setOnClickListener(view1 -> {
                Intent i = new Intent("edu.utep.cs.cs4330.mypricewatcher.ITEM_ACTIVITY");
                i.putExtra("item", item);
                startActivityForResult(i, 69);
            });
            row.setLongClickable(true);
            return row;

        }
    }

    /**
     * Override what happens when a result from another activity is given to this activity.
     *
     * {@inheritDoc}
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Item item = data.getParcelableExtra("item");
            List<Item> items = tracker.getItems();
            for (Item i : items) {
                if (i.getName().equals(item.getName())) {
                    int index = items.indexOf(i);
                    items.set(index, item);
                    runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
                    return;
                }
            }
        }

        if (resultCode == 2) {
            Item item = data.getParcelableExtra("itemToDelete");
            List<Item> items = tracker.getItems();
            for (Item i : items) {
                if (i.getName().equals(item.getName())) {
                    items.remove(i);
                    runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
                    return;
                }
            }
        }
    }

    /**
     * Override what happens when the activity is created. Create and initialize the tracker and Items table.
     *
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tracker = Tracker.getInstance();
        ListView itemsList = findViewById(R.id.items_list);

        itemsList.addHeaderView(getLayoutInflater().inflate(R.layout.items_list_titles, itemsList, false), null, false);
        itemsAdapter = new ItemListAdapter(this, tracker.getItems());
        itemsList.setAdapter(itemsAdapter);

        registerForContextMenu(itemsList);

        dbHandler = new DBHandler(this, null, null, 1);
        initTracker(dbHandler);
    }

    /**
     * Override what happens when the context menu for this view is being built. Inflate the context
     * menu that will be used for Main Activity.
     *
     * {@inheritDoc}
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.tracker_context_menu, menu);
    }

    /**
     * Override what happens and define what happens when a context menu item is selected. Defines
     * what happens when refresh, edit name, edit URL, or delete options are selected.
     *
     * {@inheritDoc}
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        selectedPosition = info.position - 1;
        switch (item.getItemId()) {
            case (R.id.refresh_context):
                try {
                    new Thread(() -> {
                        tracker.getItems().get(selectedPosition).fetchCurrPrice();
                        runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
                    }).start();
                    Toast.makeText(this, "Refreshing Price...", Toast.LENGTH_LONG).show();
                    return true;
                }
                catch(PriceNotFoundException e) {
                    Toast.makeText(this, "Error getting price", Toast.LENGTH_LONG).show();
                }
            case (R.id.delete_context):
                new DeleteDialog().show(getSupportFragmentManager(), "DeleteDialog");
                return true;

            case (R.id.edit_item_context):
                ManageItemDialog.newInstance(tracker.getItems().get(selectedPosition)).show(getSupportFragmentManager(), "");
                return true;
        }
        return true;
    }

    /**
     * Override in order to add a refresh and delete button.
     *
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem refreshItems = menu.add(0, 0, 0, "Refresh Items");
        {
            refreshItems.setIcon(R.drawable.ic_refresh_white_24dp);
            refreshItems.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        MenuItem addItem = menu.add(0, 1, 1, "Add Item");
        {
            addItem.setIcon(R.drawable.ic_add_white_24dp);
            addItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return true;
    }

    /**
     * Override in order to invoke getMenuChoice(), and allow for handling.
     *
     * {@inheritDoc}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return getMenuChoice(item);
    }

    /**
     * Override in order to restore the tracker and last selectedPosition.
     *
     * {@inheritDoc}
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        tracker = savedInstanceState.getParcelable("tracker");
        selectedPosition = savedInstanceState.getInt("selectedPosition");
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Override in order to save the tracker and last selectedPosition.
     *
     * {@inheritDoc}
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("tracker", tracker);
        outState.putInt("selectedPosition", selectedPosition);
        super.onSaveInstanceState(outState);
    }

    /**
     * Implement handling of onResponse() from DeleteDialogListener.
     *
     * @param d       The DeleteItemDialog instance that is returning a response
     * @param proceed A boolean describing whether a user selected the positive or negative button
     */
    @Override
    public void onResponse(DeleteDialog d, boolean proceed) {
        if (proceed) {
            Item item = tracker.getItems().get(selectedPosition);
            tracker.removeItem(item);
            dbHandler.deleteItem(item);
            runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
        } else d.dismiss();
    }

    /**
     *  Given a MenuItem, determine and handle what item was selected.
     *
     * @param item The MenuItem instance
     * @return     Boolean whether an item was successfully handled
     */
    private boolean getMenuChoice(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                for (Item i : tracker.getItems()) {
                    new Thread(() -> {
                        try {
                            i.fetchCurrPrice();
                            runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
                            runOnUiThread(() -> Toast.makeText(this, "Updated price for: " + i.getName(), Toast.LENGTH_LONG).show());
                        }
                        catch(PriceNotFoundException e) {
                            runOnUiThread(() -> Toast.makeText(this, "An error occurred getting the price for: " + i.getName(), Toast.LENGTH_LONG).show());
                        }
                    }).start();
                }
                return true;
            case 1:
                ManageItemDialog.newInstance().show(getSupportFragmentManager(), "");
                return true;
        }
        return false;
    }

    /**
     * Override onItemManaged from the ManageItemDialogListener in order to respond accordingly.
     *
     * @param d             The ManageItemDialog instance that is returning a response
     * @param itemName      The item name that was entered
     * @param url           The Web url that was entered
     * @param proceed       Boolean, whether or not positive or negative button was pressed
     * @param newItem       Boolean, whether the item is a new item or not
     */
    @Override
    public void onItemManaged(ManageItemDialog d, String itemName, String url, boolean proceed, boolean newItem) {
        if (proceed) {
            if (newItem) {

                new Thread(() -> {
                    try {
                        runOnUiThread(() -> Toast.makeText(this, "Adding item at: " + url, Toast.LENGTH_LONG).show());
                        Item item = tracker.addItem(itemName, url);
                        dbHandler.addItem(item);
                        runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
                        runOnUiThread(() -> Toast.makeText(this, "Added item!", Toast.LENGTH_LONG).show());
                    }
                    catch(PriceNotFoundException e) {
                        runOnUiThread(() -> Toast.makeText(this, "Error with URL: " + url, Toast.LENGTH_LONG).show());
                    }
                }).start();
                Toast.makeText(this, "Adding: " + itemName, Toast.LENGTH_LONG).show();
            }
            if (!newItem) {
                Item itemToModify = tracker.getItems().get(selectedPosition);
                dbHandler.editItem(itemToModify, itemName, url);
                itemToModify.setName(itemName);
                itemToModify.setURL(url);
                runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
                Toast.makeText(this, "Item saved!", Toast.LENGTH_LONG).show();
            }
        } else d.dismiss();
    }

    private void initTracker(DBHandler dbHandler) {
        ArrayList<Item> storedItems = dbHandler.getItems();
        for (Item i : storedItems)
            tracker.addItem(i);
    }
}

