package edu.utep.cs.cs4330.mypricewatcher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * The Main Activity of the app for HW1 purposes. This activity features
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
public class MainActivity extends AppCompatActivity {
    private Item currItem;                      /* Current item in view */
    private TextView itemName;                  /* Name of the current item */
    private TextView priceInit;                 /* Initial price of the current item */
    private TextView dateAdded;                 /* Date the current item was added */
    private TextView priceCurr;                 /* Current price of the current item */
    private TextView percChange;                /* Percentage change of the item prices */
    private Button refreshButton;               /* Refresh button for the item in view */
    private Tracker tracker;                    /* Internal tracker for all items */
    /* Decimal formatter for percentages */
    private static final DecimalFormat percFormatter = new DecimalFormat("+ ##.##%;- ##.##%");
    /* Decimal formatter for dollar values */
    private DecimalFormat dollarFormatter = new DecimalFormat("$##.##");
    /* Date formatter for displaying dates */
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy", java.util.Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Initialize the tracker and add a simulated item */
        if (savedInstanceState == null) {
            initTrackerForHw1();

            /* Initialize values for the current item */
            itemName = findViewById(R.id.item_name_value);
            itemName.setText(currItem.getName());
            priceInit = findViewById(R.id.item_initial_price_value);
            priceInit.setText(doubleToDollar(currItem.getInitPrice()));
            dateAdded = findViewById(R.id.item_date_added);
            dateAdded.setText(calToDate(currItem.getDateAdded()));
            priceCurr = findViewById(R.id.item_current_price_value);
            priceCurr.setText(doubleToDollar(currItem.getCurrPrice()));
            percChange = findViewById(R.id.item_change_value);
            percChange.setText(doubleToPerc(currItem.getPercChange()));

            // Initialize the refresh button and its handler
            refreshButton = findViewById(R.id.refresh_button);
            refreshButton.setOnClickListener(this::refreshItem);
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
        if (savedInstanceState != null) {
            this.currItem = new Item(savedInstanceState.getString("name"),
                    savedInstanceState.getDouble("initPrice"),
                    savedInstanceState.getDouble("currPrice"),
                    savedInstanceState.getDouble("percChange"),
                    savedInstanceState.getString("url"),
                    (Calendar)savedInstanceState.getSerializable("dateAdded")
            );

            /* Initialize values for the current item */
            itemName = findViewById(R.id.item_name_value);
            itemName.setText(currItem.getName());
            priceInit = findViewById(R.id.item_initial_price_value);
            priceInit.setText(doubleToDollar(currItem.getInitPrice()));
            dateAdded = findViewById(R.id.item_date_added);
            dateAdded.setText(calToDate(currItem.getDateAdded()));
            priceCurr = findViewById(R.id.item_current_price_value);
            priceCurr.setText(doubleToDollar(currItem.getCurrPrice()));
            percChange = findViewById(R.id.item_change_value);
            percChange.setText(doubleToPerc(currItem.getPercChange()));

            // Initialize the refresh button and its handler
            refreshButton = findViewById(R.id.refresh_button);
            refreshButton.setOnClickListener(this::refreshItem);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("name", currItem.getName());
        outState.putDouble("initPrice", currItem.getInitPrice());
        outState.putDouble("currPrice", currItem.getCurrPrice());
        outState.putDouble("percChange", currItem.getPercChange());
        outState.putString("url", currItem.getURL());
        outState.putSerializable("dateAdded", currItem.getDateAdded());
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
        priceCurr.setText(doubleToDollar(currItem.getCurrPrice()));
        percChange.setText(doubleToPerc(currItem.getPercChange()));
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
     * Convert Calendar instance into a string representation: MM/dd/yy.
     *
     * @param c The Calendar instance whose date will be return as a string representation
     * @return  The string representation of the Calendar instance's date.
     */
    private String calToDate(Calendar c) {
        return dateFormatter.format(c.getTime());
    }

    /**
     * Initialize a tracker and add a fixed and simulated item for HW1 purposes.
     */
    private void initTrackerForHw1() {
        tracker = Tracker.getInstance();
        currItem = tracker.addItem("www.amazon.com/Item0");
    }
}
