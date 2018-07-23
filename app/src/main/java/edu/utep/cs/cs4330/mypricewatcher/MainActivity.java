package edu.utep.cs.cs4330.mypricewatcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DeleteDialogListener{
    private Tracker tracker;                    /* Internal tracker for all items */
    private ListView itemsList;
    private ItemListAdapter itemsAdapter;
    private Button refreshButton;
    private int selectedPosition;

    private class ItemListAdapter extends ArrayAdapter<Item> {
        private final List<Item> items;

        private ItemListAdapter(Context ctx, List<Item> items) {
            super (ctx, android.R.layout.simple_list_item_1, items);
            this.items = items;
        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Item item = (Item) data.getParcelableExtra("item");
            Log.d("onActivityResult", item.getName());
            List<Item> items = tracker.getItems();
            for (Item i : items) {
                if (i.getName().equals(item.getName())) {
                    int index = items.indexOf(i);
                    items.set(index, item);
                    runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tracker = Tracker.getInstance();
        itemsList = (ListView) findViewById(R.id.items_list);

        itemsList.addHeaderView(getLayoutInflater().inflate(R.layout.items_list_titles, itemsList, false));
        itemsAdapter = new ItemListAdapter(this, tracker.getItems());
        itemsList.setAdapter(itemsAdapter);

        refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener((View v) -> {
            tracker.updatePrices();
            runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
        });

        registerForContextMenu(itemsList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.tracker_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        selectedPosition = info.position - 1;
        if (item.getItemId() == R.id.refresh_context) {
            tracker.getItems().get(selectedPosition).fetchCurrPrice();
            runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
        }
        else if (item.getItemId() == R.id.delete_context)
            new DeleteDialog().show(getSupportFragmentManager(), "DeleteDialog");
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        tracker = savedInstanceState.getParcelable("tracker");
        selectedPosition = savedInstanceState.getInt("selectedPosition");
        itemsList = findViewById(R.id.items_list);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("tracker", tracker);
        outState.putInt("selectedPosition", selectedPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResponse(DeleteDialog d, boolean proceed) {
        if (proceed) {
            tracker.removeItem(tracker.getItems().get(selectedPosition));
            runOnUiThread(() -> itemsAdapter.notifyDataSetChanged());
        }
        else d.dismiss();
    }
}
