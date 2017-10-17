package com.jayzh7.tma.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.mikephil.charting.data.PieEntry;
import com.jayzh7.tma.Models.TravelEvent;

import org.joda.time.DateTime;

import java.util.ArrayList;

import static com.jayzh7.tma.Models.EventType.GUIDANCE;
import static com.jayzh7.tma.Models.EventType.MEAL;
import static com.jayzh7.tma.Models.EventType.OTHERS;
import static com.jayzh7.tma.Models.EventType.REST;
import static com.jayzh7.tma.Models.EventType.SHOPPING;
import static com.jayzh7.tma.Models.EventType.SIGHTSEEING;

/**
 * This database helper is used to do all the read, write for database
 * Created by Jay on 10/4/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "db";

    public static final String TABLE_EVENTS = "travel";
    public static final String TABLE_TIME = "time";
    public static final String TABLE_DATE = "date";

    public static final String COLUMN_AVAILABLE = "available";

    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_DAY = "day";

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

    public static final int DATABASE_VERSION = 8;
    public static final String COLUMN_LEFT = "left";
    public static final String COLUMN_RIGHT = "right";
    public static final String COLUMN_TOP = "top";
    public static final String COLUMN_BOTTOM = "bottom";

    private static DatabaseHelper sInstance;

    /**
     * public contructor
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
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

    /**
     * Create a new database upon creation.
     *
     * @param sqLiteDatabase the database where data will be stored
     */
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

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_DATE + " ( "
                + COLUMN_YEAR + " INTEGER, "
                + COLUMN_MONTH + " INTEGER, "
                + COLUMN_DAY + " INTEGER);");

        initTableLine(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TIME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DATE);
        onCreate(sqLiteDatabase);
    }

    /**
     * Insert a date item to database
     *
     * @param year
     * @param month month - 1
     * @param day
     */
    public void insertDate(int year, int month, int day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_MONTH, month + 1);
        values.put(COLUMN_DAY, day);

        db.insert(TABLE_DATE, null, values);
    }

    /**
     * Update the date item
     * Note: there is only one item in DATE table at any time
     *
     * @param year
     * @param month month-1
     * @param day
     */
    public void updateDate(int year, int month, int day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_MONTH, month + 1);
        values.put(COLUMN_DAY, day);

        db.update(TABLE_DATE, values, null, null);
    }
    /**
     * Insert a new event to database
     *
     * @param travelEvent new event
     * @return
     */
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

        updateTimeLine(startTime, endTime, false);

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

    /**
     * Update an event
     *
     * @param startTime start time of the event which is used to identify which event to update
     * @param event     new event
     */
    public void updateEvent(int startTime, TravelEvent event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EVENT_NAME, event.getEventName());
        values.put(COLUMN_EVENT_TYPE, event.getEventType().ordinal());
        values.put(COLUMN_START_TIME, event.getStartT());
        values.put(COLUMN_END_TIME, event.getEndT());
        values.put(COLUMN_START_PLACE_NAME, event.getFromPlace());
        values.put(COLUMN_END_PLACE_NAME, event.getToPlace());
        values.put(COLUMN_START_PLACE_ID, event.getStartPlaceID());
        values.put(COLUMN_END_PLACE_ID, event.getEndPlaceID());

        db.update(TABLE_EVENTS, values, COLUMN_START_TIME + "=" + startTime, null);
    }

    /**
     * Clear or set if the minute is available
     *
     * @param start start minute
     * @param end   end minute
     * @param clear true:  clear
     *              false: set
     */
    public void updateTimeLine(int start, int end, boolean clear) {
        if (start > end)
            return;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (clear)
            values.put(COLUMN_AVAILABLE, 1);
        else
            values.put(COLUMN_AVAILABLE, 0);

        for (int i = start; i <= end; i++)
            db.update(TABLE_TIME, values, COLUMN_ID + "=" + i, null);
    }

    /**
     * Delete an event in database, also clear the time period it occupies.
     *
     * @param id start time used to identify which item to delete
     */
    public void deleteEvent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int endTime = 0;

        String[] projection = {
                COLUMN_END_TIME,
        };

        Cursor cursor = db.query(
                TABLE_EVENTS,
                projection,
                COLUMN_START_TIME + "=" + id,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            endTime = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_END_TIME)
            );
        }
        updateTimeLine(id, endTime, true);
        db.delete(TABLE_EVENTS, COLUMN_START_TIME + "=" + id, null);

    }

    /**
     * Get an event from database
     *
     * @param id start time of the event
     * @return event
     */
    @Deprecated
    public TravelEvent findEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        TravelEvent event = new TravelEvent();

        String[] projection = {
                COLUMN_ID,
                COLUMN_EVENT_NAME,
                COLUMN_EVENT_TYPE,
                COLUMN_END_TIME,
                COLUMN_START_TIME,
                COLUMN_START_PLACE_ID,
                COLUMN_END_PLACE_ID,
                COLUMN_START_PLACE_NAME,
                COLUMN_END_PLACE_NAME
        };

        Cursor cursor = db.query(
                TABLE_EVENTS,
                projection,
                COLUMN_START_TIME + "=" + id,
                null,
                null,
                null,
                null
        );

        return event;
    }

    /**
     * Read event table for necessary info to draw bar chart
     *
     * @return An array contains event ID, start time and end time of the event
     * the first position represents the number of events
     */
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

    public int[] readForDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        int[] date = new int[3];

        String[] projection = {
                COLUMN_YEAR,
                COLUMN_MONTH,
                COLUMN_DAY
        };

        Cursor cursor = db.query(
                TABLE_DATE,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToNext();
        date[0] = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_YEAR));
        date[1] = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_MONTH));
        date[2] = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_DAY));

        return date;
    }

    /**
     * Get row count for events
     *
     * @return row count
     */
    public long getItemNumberForEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_EVENTS);
        return cnt;
    }

    public long getItemNumberForDate() {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_DATE);
    }


    /**
     * Read event database for all events
     *
     * @return An arrayList of events
     */
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

    /**
     * Read data for Pie Chart
     *
     * @return Array list of event types and their count.
     */
    public ArrayList<PieEntry> readForPieChart() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<PieEntry> entries = new ArrayList<>();
        String[] labels = {SIGHTSEEING.toString(), GUIDANCE.toString(), MEAL.toString(),
                REST.toString(), SHOPPING.toString(), OTHERS.toString()};
        int[] cnt = {0, 0, 0, 0, 0, 0};

        String[] projection = {
                COLUMN_EVENT_TYPE,
        };

        Cursor cursor = db.query(
                TABLE_EVENTS,
                projection,
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            int temp = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_EVENT_TYPE)
            );
            cnt[temp]++;
        }

        for (int i = 0; i < 6; i++) {
            if (cnt[i] != 0) entries.add(new PieEntry(cnt[i], labels[i]));
        }

        return entries;
    }

    /**
     * Read the timeLine table for available minutes
     *
     * @return An array of int represnets it that single minute is available
     * 0 N/A
     */
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


    /**
     * clear all occupied time to available
     */
    public void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_AVAILABLE, 1);

        int i = db.update(TABLE_TIME, values, COLUMN_AVAILABLE + "=0", null);

        db.delete(TABLE_EVENTS, null, null);
    }

    /**
     * Convert a DateTime object to minutes it represents
     *
     * @param dateTime
     * @return
     */
    private int dateTimeToInt(DateTime dateTime) {
        return dateTime.getHourOfDay() * 60 + dateTime.getMinuteOfHour();
    }

    /**
     * Initialize the timeLine table to all available
     *
     * @param db the database where the table is stored
     */
    private void initTableLine(SQLiteDatabase db) {
        for (int i = 0; i < 1440; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_AVAILABLE, 1);
            values.put(COLUMN_ID, i);
            db.insert(TABLE_TIME, null, values);
        }
    }
}
