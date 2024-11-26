package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "lostAndFoundDB";


    // Users table
    private static final String TABLE_USERS = "users";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_IS_ADMIN = "is_admin";
    // Events table
    private static final String TABLE_EVENTS = "events";
    private static final String KEY_EVENT_ID = "id";
    private static final String KEY_EVENT_TITLE = "title";
    private static final String KEY_EVENT_DESCRIPTION = "description";
    private static final String KEY_EVENT_TIMESTAMP = "timestamp";
    private static final String KEY_EVENT_ADMIN_ID = "admin_id";


    // Reports table
    private static final String TABLE_REPORTS = "reports";
    private static final String KEY_REPORT_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_TIMESTAMP = "timestamp";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION+1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Create Users Table
            String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "("
                    + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_USERNAME + " TEXT,"
                    + KEY_EMAIL + " TEXT UNIQUE,"
                    + KEY_PASSWORD + " TEXT,"
                    + KEY_PHONE + " TEXT,"
                    + KEY_IS_ADMIN + " INTEGER"  // Default to 0 (not an admin)
                    + ")";


            db.execSQL(CREATE_USERS_TABLE);
            Log.d("DatabaseHandler", "Users table created successfully");

            // Create Reports Table
            String CREATE_REPORTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_REPORTS + "("
                    + KEY_REPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_TITLE + " TEXT NOT NULL,"
                    + KEY_DESCRIPTION + " TEXT NOT NULL,"
                    + KEY_LOCATION + " TEXT NOT NULL,"
                    + KEY_IMAGE_PATH + " TEXT,"
                    + KEY_TIMESTAMP + " INTEGER"
                    + ")";
            db.execSQL(CREATE_REPORTS_TABLE);
            Log.d("DatabaseHandler", "Reports table created successfully");

            // Create Events Table
            String CREATE_EVENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS + "("
                    + KEY_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_EVENT_TITLE + " TEXT,"
                    + KEY_EVENT_DESCRIPTION + " TEXT,"
                    + KEY_EVENT_TIMESTAMP + " TEXT,"
                    + KEY_EVENT_ADMIN_ID + " INTEGER,"
                    + "FOREIGN KEY(" + KEY_EVENT_ADMIN_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_USER_ID + ")"
                    + ")";
            db.execSQL(CREATE_EVENTS_TABLE);
            Log.d("DatabaseHandler", "Events table created successfully");

        } catch (Exception e) {
            Log.e("DatabaseHandler", "Error creating tables: " + e.getMessage(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
//        onCreate(db);
    }
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
//        if (oldVersion < 2) {
//            // Add the is_admin column if upgrading from version 1
//            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + KEY_IS_ADMIN + " INTEGER DEFAULT 0");
//        }
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
//        onCreate(db);
//    }

    public long addUser(String name, String email, String password, String phone, boolean isAdmin) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_PHONE, phone);
        values.put(KEY_IS_ADMIN, isAdmin ? 1 : 0);  // Store boolean as integer (1 = true, 0 = false)

        // Insert row, return the row ID or -1 if there was an error
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
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
    public boolean isAdmin(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { KEY_IS_ADMIN };
        String selection = KEY_USER_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int isAdmin = cursor.getInt(cursor.getColumnIndex(KEY_IS_ADMIN));
            cursor.close();
            return isAdmin == 1;  // If is_admin is 1, then user is an admin
        }
        cursor.close();
        return false;
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

    public long addReport(String title, String description, String location, String imagePath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, title);
        values.put(KEY_DESCRIPTION, description);
        values.put(KEY_LOCATION, location);
        values.put(KEY_IMAGE_PATH, imagePath);
        values.put(KEY_TIMESTAMP, System.currentTimeMillis());

        // Insert row
        long id = db.insert(TABLE_REPORTS, null, values);
        db.close();
        return id;  // This was missing
    }
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(KEY_USERNAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(KEY_PASSWORD)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
                user.setIsAdmin(cursor.getInt(cursor.getColumnIndex(KEY_IS_ADMIN)) == 1);

                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public List<Report> getAllReports() {
        List<Report> reportList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_REPORTS + " ORDER BY " + KEY_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Report report = new Report();
                report.setId(cursor.getString(cursor.getColumnIndex(KEY_REPORT_ID)));
                report.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                report.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                report.setLocation(cursor.getString(cursor.getColumnIndex(KEY_LOCATION)));
                report.setImageUrl(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH)));
                report.setTimestamp(cursor.getLong(cursor.getColumnIndex(KEY_TIMESTAMP)));

                reportList.add(report);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reportList;
    }
}
