package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "lostAndFoundDB";

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    // Events table
    private static final String TABLE_EVENTS = "events";
    private static final String KEY_EVENT_ID = "id";
    private static final String KEY_EVENT_TITLE = "title";
    private static final String KEY_EVENT_DESCRIPTION = "description";
    private static final String KEY_EVENT_TIMESTAMP = "timestamp";
    private static final String KEY_EVENT_ADMIN_ID = "admin_id";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_PHONE + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "("
                + KEY_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EVENT_TITLE + " TEXT,"
                + KEY_EVENT_DESCRIPTION + " TEXT,"
                + KEY_EVENT_TIMESTAMP + " TEXT,"
                + KEY_EVENT_ADMIN_ID + " INTEGER,"
                + "FOREIGN KEY(" + KEY_EVENT_ADMIN_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + ")"
                + ")";
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public long addUser(String username, String email, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_USERNAME, username);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_PHONE, phone);

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }
    public long addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_EVENT_TITLE, event.getTitle());
        values.put(KEY_EVENT_DESCRIPTION, event.getDescription());
        values.put(KEY_EVENT_TIMESTAMP, event.getTimestamp());

        long id = db.insert(TABLE_EVENTS, null, values);
        db.close();
        return id;
    }
    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<Event>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0))); // ID
                event.setTitle(cursor.getString(1));                // Title
                event.setDescription(cursor.getString(2));          // Description
                event.setTimestamp(cursor.getString(3));            // Timestamp

                // Adding event to list
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        // Closing the cursor
        cursor.close();

        // Return event list
        return eventList;
    }



    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { KEY_USER_ID };
        String selection = KEY_EMAIL + " = ?" + " AND " + KEY_PASSWORD + " = ?";
        String[] selectionArgs = { email, password };

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }
}
