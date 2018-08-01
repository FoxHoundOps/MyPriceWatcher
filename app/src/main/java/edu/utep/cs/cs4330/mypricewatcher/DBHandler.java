package edu.utep.cs.cs4330.mypricewatcher;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "items.db";

    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_INIT_PRICE = "initPrice";
    public static final String COLUMN_CURR_PRICE = "currPrice";
    public static final String COLUMN_PERC_CHANGE = "percChange";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_DATE_ADDED = "dateAdded";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_ITEMS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_INIT_PRICE + " TEXT, " +
                COLUMN_CURR_PRICE + " TEXT, " +
                COLUMN_PERC_CHANGE + " TEXT, " +
                COLUMN_URL + " TEXT, " +
                COLUMN_DATE_ADDED + " TEXT " +
                ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(sqLiteDatabase);
    }

    public void addItem(Item item) {
        Log.d("a;lsdkjf", "WRITING " + item.getName());
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_INIT_PRICE, item.getInitPrice());
        values.put(COLUMN_CURR_PRICE, item.getCurrPrice());
        values.put(COLUMN_PERC_CHANGE, item.getPercChange());
        values.put(COLUMN_URL, item.getURL());
        values.put(COLUMN_DATE_ADDED, item.getDateAdded());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }

    public void deleteItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ITEMS + " WHERE " + COLUMN_NAME + "=\"" + item.getName() + "\";");
        db.close();
    }

    public void editItem(Item item, String newName, String newUrl) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_ITEMS + " SET name = \'" + newName + "\'" + " WHERE name = \'" + item.getName() + "\';");
        db.execSQL("UPDATE " + TABLE_ITEMS + " SET url = \'" + newUrl + "\'" + " WHERE url = \'" + item.getURL() + "\';");

    }

    public ArrayList<Item> getItems() {
        ArrayList<Item> items = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEMS + " WHERE 1";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        try {
            while (c.moveToNext()) {
                if (c.getString(c.getColumnIndex("name")) != null) {
                    String name = c.getString(c.getColumnIndex("name"));
                    Double initPrice = Double.parseDouble(c.getString(c.getColumnIndex("initPrice")).replace("$", ""));
                    Double currPrice = Double.parseDouble(c.getString(c.getColumnIndex("currPrice")).replace("$", ""));
                    Double percChange = Double.parseDouble(c.getString(c.getColumnIndex("percChange")).replace("%", ""));
                    String url = c.getString(c.getColumnIndex("url"));
                    String dateAdded = c.getString(c.getColumnIndex("dateAdded"));
                    Item item = new Item(name, initPrice, currPrice, percChange, url, dateAdded);
                    items.add(item);
                }
            }
        }
        finally {
            c.close();
        }
        return items;
    }
}
