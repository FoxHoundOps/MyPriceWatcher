package edu.utep.cs.cs4330.mypricewatcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * The Item Activity of the app for HW1 purposes. This activity features
 * the viewing of one single item that will be tracked. This current implementation
 * features information such as the item's name, the initial price of the item, the
 * current price of the object (from when its price was last updated), the percentage
 * change from the initial price to the current price, the date that the item was
 * added to the Tracker, and a refresh button. When the refresh button is pressed,
 * the item currently in view will have its price updated.
 *
 * @author Damian Najera
 * @version 1.0
 */
public class ItemActivity extends AppCompatActivity implements DeleteDialogListener {
    private Item currItem;                      /* Current item in view */
    private TextView itemName;                  /* Name of the current item */
    private TextView priceInit;                 /* Initial price of the current item */
    private TextView priceCurr;                 /* Current price of the current item */
    private TextView percChange;                /* Percentage change of the item prices */
    private TextView url;                       /* URL of the item */
    private Intent intentResult;                /* Intent that will hold any result for MainActivity */
    private boolean hasSetResult = false;       /* Whether a result has been set with setResult(int) */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        currItem = i.getParcelableExtra("item");

        /* Initialize the tracker and add a simulated item */
        if (savedInstanceState == null) {
           displayItem();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem refreshItem = menu.add(0, 0, 0, "Refresh Item");
        {
            refreshItem.setIcon(R.drawable.ic_refresh_white_24dp);
            refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        MenuItem deleteItem = menu.add(0, 1, 1, "Delete Item");
        {
            deleteItem.setIcon(R.drawable.ic_delete_white_24dp);
            deleteItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

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

        return getMenuChoice(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        intentResult = savedInstanceState.getParcelable("intentResult");
        hasSetResult = savedInstanceState.getBoolean("hasSetResult");
        if (hasSetResult)
            setResult(RESULT_OK, intentResult);
        displayItem();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("intentResult", intentResult);
        outState.putBoolean("hasSetResult", hasSetResult);
        super.onSaveInstanceState(outState);
    }

    /**
     *Refresh current item's price by having the item refresh its price, then update
     * the current price in the interface.
     *
     */
    private void refreshItem() {
        currItem.fetchCurrPrice();
        updateValues();
        intentResult = new Intent();
        intentResult.putExtra("item", currItem);
        setResult(RESULT_OK, intentResult);
        hasSetResult = true;
    }

    private void updateValues() {
        priceCurr.setText(Html.fromHtml(getString(R.string.curr_price_template, currItem.getCurrPrice())));
        percChange.setText(Html.fromHtml(getString(R.string.perc_change_template, currItem.getPercChange())));
    }

    private void displayItem() {
        /* Initialize values for the current item */
        itemName = findViewById(R.id.item_name);
        itemName.setText(Html.fromHtml(getString(R.string.item_name_template, currItem.getName())));
        priceInit = findViewById(R.id.init_price);
        priceInit.setText(Html.fromHtml(getString(R.string.init_price_template, currItem.getInitPrice(), currItem.getDateAdded())));
        priceCurr = findViewById(R.id.curr_price);
        priceCurr.setText(Html.fromHtml(getString(R.string.curr_price_template, currItem.getCurrPrice())));
        percChange = findViewById(R.id.perc_change);
        percChange.setText(Html.fromHtml(getString(R.string.perc_change_template, currItem.getPercChange())));
        url = findViewById(R.id.item_url);
        url.setText(Html.fromHtml(getString(R.string.url_template, currItem.getURL())));
        url.setClickable(true);
        url.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private boolean getMenuChoice(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                refreshItem();
                return true;
            case 1:
                new DeleteDialog().show(getSupportFragmentManager(), "DeleteDialog");
                return true;
        }
        return false;
    }

    @Override
    public void onResponse(DeleteDialog d, boolean proceed) {
        if (proceed) {
            intentResult = new Intent();
            intentResult.putExtra("itemToDelete", currItem);
            setResult(2, intentResult);
            hasSetResult = true;
            finish();
        } else d.dismiss();
    }
}
