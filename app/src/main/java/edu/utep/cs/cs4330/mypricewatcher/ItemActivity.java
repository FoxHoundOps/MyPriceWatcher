package edu.utep.cs.cs4330.mypricewatcher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
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
public class ItemActivity extends AppCompatActivity {
    private Item currItem;                      /* Current item in view */
    private TextView itemName;                  /* Name of the current item */
    private TextView priceInit;                 /* Initial price of the current item */
    private TextView priceCurr;                 /* Current price of the current item */
    private TextView percChange;                /* Percentage change of the item prices */
    private Button refreshButton;               /* Refresh button for the item in view */
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
     * @param v The view associated with the onClick() event that called this function
     */
    private void refreshItem(View v) {
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

        // Initialize the refresh button and its handler
        refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(this::refreshItem);
    }
}
