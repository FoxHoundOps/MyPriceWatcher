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
import java.util.List;

/**
 * The Main Activity
 *
 * @author Damian Najera
 * @version 1.2
 */
public class MainActivity extends AppCompatActivity implements DeleteDialogListener,
        AddItemDialogListener, EditNameDialogListener, EditURLDialogListener {
    private Tracker tracker;                    /* Internal tracker for all items */
    private ItemListAdapter itemsAdapter;       /* The adapter for the ListView */
    private int selectedPosition;               /* Selected context menu option position */

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
                tracker.getItems().get(selectedPosition).fetchCurrPrice();
                runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
                return true;

            case (R.id.delete_context):
                new DeleteDialog().show(getSupportFragmentManager(), "DeleteDialog");
                return true;

            case (R.id.edit_name_context):
                new EditNameDialog().show(getSupportFragmentManager(), "EditNameDialog");
                return true;

            case (R.id.edit_url_context):
                new EditURLDialog().show(getSupportFragmentManager(), "EditURLDialog");
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
            tracker.removeItem(tracker.getItems().get(selectedPosition));
            runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
        } else d.dismiss();
    }

    /**
     * Implement handling of onUserInput() from AddItemDialogListener.
     *
     * @param d       The AddItemDialog instance that is returning a response
     * @param proceed A boolean describing whether a user selected the positive or negative button
     * @param input   The string input that was entered by the user, if any
     */
    @Override
    public void onUserInput(AddItemDialog d, boolean proceed, String input) {
        if (proceed) {
            tracker.addItem(input);
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
                tracker.updatePrices();
                runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
                return true;
            case 1:
                new AddItemDialog().show(getSupportFragmentManager(), "AddItemDialog");
                return true;
        }
        return false;
    }

    /**
     * Implement handling of onItemName() from EditNameDialogListener.
     *
     * @param d       The EditNameDialog instance that is returning a response
     * @param proceed A boolean describing whether a user selected the positive or negative button
     * @param newName  The new name of the item
     */
    @Override
    public void onItemName(EditNameDialog d, boolean proceed, String newName) {
        if (proceed) {
            tracker.getItems().get(selectedPosition).setName(newName);
            runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
        } else d.dismiss();
    }

    /**
     * Implement handling of onItemURL() from EditURLDialogListener.
     *
     * @param d       The EditURLDialog instance that is returning a response
     * @param proceed A boolean describing whether a user selected the positive or negative button
     * @param newURL  The new URL of the item
     */
    @Override
    public void onItemURL(EditURLDialog d, boolean proceed, String newURL) {
        if (proceed) {
            tracker.getItems().get(selectedPosition).setURL(newURL);
            runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
        } else d.dismiss();
    }
}

