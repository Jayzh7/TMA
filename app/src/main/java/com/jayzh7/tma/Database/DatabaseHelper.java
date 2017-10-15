package com.jayzh7.tma.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jayzh7.tma.Models.TravelEvent;

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 *
 * Created by Jay on 10/4/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db";
    public static final String TABLE_EVENTS = "travel";
    public static final String TABLE_TIME = "time";
    public static final String COLUMN_AVAILABLE = "available";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_EVENT_NAME = "EventName";
    public static final String COLUMN_EVENT_TYPE = "EventType";
    public static final String COLUMN_END_TIME = "EndTime";
    public static final String COLUMN_START_TIME = "StartTime";
    public static final String COLUMN_DURATION = "Duration";
    public static final String COLUMN_START_PLACE_ID = "StartPlaceID";
    public static final String COLUMN_END_PLACE_ID = "EndPlaceID";
    public static final String COLUMN_START_PLACE_NAME = "StartPlaceName";
    public static final String COLUMN_END_PLACE_NAME = "EndPlaceName";

    public static final int DATABASE_VERSION = 6;
    public static final String COLUMN_LEFT = "left";
    public static final String COLUMN_RIGHT = "right";
    public static final String COLUMN_TOP = "top";
    public static final String COLUMN_BOTTOM = "bottom";

    private static DatabaseHelper sInstance;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_EVENTS + " ( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EVENT_NAME + " VARCHAR, "
                + COLUMN_START_PLACE_ID + " VARCHAR, "
                + COLUMN_END_PLACE_ID + " VARCHAR, "
                + COLUMN_START_PLACE_NAME + " VARCHAR, "
                + COLUMN_END_PLACE_NAME + " VARCAHR, "
                + COLUMN_START_TIME + " INTEGER, "
                + COLUMN_END_TIME + " INTEGER, "
                + COLUMN_EVENT_TYPE + " INTEGER, "
                + COLUMN_DURATION + " INTEGER);");

        /*
                + COLUMN_LEFT + " INTEGER, "
                + COLUMN_RIGHT + " INTEGER, "
                + COLUMN_TOP + " INTEGER, "
                + COLUMN_BOTTOM + " INTEGER*/

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TIME + " ( "
                + COLUMN_ID + " INTEGER,"
                + COLUMN_AVAILABLE + " INTEGER);");

        initTableLine(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TIME);
        onCreate(sqLiteDatabase);
    }

    public long insertTravelEvent(TravelEvent travelEvent) {
        SQLiteDatabase database = this.getWritableDatabase();

        int startTime = dateTimeToInt(travelEvent.getStartTime());
        int endTime = dateTimeToInt(travelEvent.getEndTime());

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, travelEvent.getEventName());
        values.put(COLUMN_START_TIME, startTime);
        values.put(COLUMN_END_TIME, endTime);
        values.put(COLUMN_START_PLACE_NAME, travelEvent.getFromPlace());
        values.put(COLUMN_END_PLACE_NAME, travelEvent.getToPlace());
        values.put(COLUMN_DURATION, travelEvent.getDuration());
        values.put(COLUMN_START_PLACE_ID, travelEvent.getStartPlaceID());
        values.put(COLUMN_END_PLACE_ID, travelEvent.getEndPlaceID());
        values.put(COLUMN_EVENT_TYPE, travelEvent.getEventType().ordinal());


        long id = database.insert(TABLE_EVENTS, null, values);

        ContentValues values1 = new ContentValues();
        values1.put(COLUMN_AVAILABLE, 0);

        String whereArgs = "";
        for (int i = 0; i < endTime - startTime; i++) {
            int j = database.update(TABLE_TIME, values1, COLUMN_ID + "=?", new String[]{String.valueOf(startTime + i)});
        }

        return id;
    }

    /**
     * Update the item's painted area
     *
     * @param id     id of the item
     * @param left   leftest point
     * @param right  rightest point
     * @param top    top point
     * @param bottom bottom point
     */
    public void updatePosition(int id, int left, int right, int top, int bottom) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_LEFT, left);
        values.put(COLUMN_RIGHT, right);
        values.put(COLUMN_TOP, top);
        values.put(COLUMN_BOTTOM, bottom);

        int i = db.update(TABLE_EVENTS, values, COLUMN_ID + "=" + id, null);
    }

    public int[] readForBarCharts() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_ID,
                COLUMN_START_TIME,
                COLUMN_END_TIME,
        };

        String sortOrder = COLUMN_START_TIME + " DESC";

        Cursor cursor = db.query(
                TABLE_EVENTS,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        int[] array = new int[100];
        int index = 1;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_ID));
            int startTime = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_START_TIME));
            int endTime = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_END_TIME));
            array[index++] = id;
            array[index++] = startTime;
            array[index++] = endTime;
        }

        // Stores the number of items
        array[0] = index;

        return array;
    }

    public ArrayList<TravelEvent> readForEventList() {
        ArrayList<TravelEvent> events = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_EVENT_NAME,
                COLUMN_START_TIME,
                COLUMN_END_TIME,
                COLUMN_START_PLACE_NAME,
                COLUMN_END_PLACE_NAME,
                COLUMN_EVENT_TYPE
        };

        String sortOrder = COLUMN_START_TIME + " ASC";

        Cursor cursor = db.query(
                TABLE_EVENTS,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            int startTime = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_START_TIME));
            int endTime = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_END_TIME));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EVENT_TYPE));
            String start = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_PLACE_NAME));
            String end = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_PLACE_NAME));
            DateTime currentTime = new DateTime();
            events.add(new TravelEvent(
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EVENT_NAME)),
                            start,
                            end,
                            startTime,
                            endTime,
                            type
                    )
            );

        }

        return events;
    }


    public int[] readForTimeLine() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                COLUMN_AVAILABLE
        };

        Cursor cursor = db.query(
                TABLE_TIME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        int[] array = new int[1440];
        int index = 0;
        while (cursor.moveToNext()) {
            array[index++] = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_AVAILABLE)
            );
        }

        return array;
    }

    public void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_AVAILABLE, 1);

        int i = db.update(TABLE_TIME, values, COLUMN_AVAILABLE + "=0", null);

        db.delete(TABLE_EVENTS, null, null);
    }

    private int dateTimeToInt(DateTime dateTime) {
        return dateTime.getHourOfDay() * 60 + dateTime.getMinuteOfHour();
    }

    private void initTableLine(SQLiteDatabase db) {
        for (int i = 0; i < 1440; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_AVAILABLE, 1);
            values.put(COLUMN_ID, i);
            long t = db.insert(TABLE_TIME, null, values);
        }
    }

}
