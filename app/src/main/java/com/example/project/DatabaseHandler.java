package com.example.project;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
<<<<<<< HEAD
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "lostAndFoundManager";
=======
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "lostAndFoundDB";

>>>>>>> origin/main

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";
<<<<<<< HEAD
=======
    private static final String KEY_IS_ADMIN = "is_admin";
    // Events table
    private static final String TABLE_EVENTS = "events";
    private static final String KEY_EVENT_ID = "id";
    private static final String KEY_EVENT_TITLE = "title";
    private static final String KEY_EVENT_DESCRIPTION = "description";
    private static final String KEY_EVENT_TIMESTAMP = "timestamp";
    private static final String KEY_EVENT_ADMIN_ID = "admin_id";

>>>>>>> origin/main

    // Reports table
    private static final String TABLE_REPORTS = "reports";
    private static final String KEY_REPORT_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_CONTACT_INFO = "contact_info";
    private static final String KEY_IS_FOUND = "is_found";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION+1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // Create Users Table
            String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
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
            String CREATE_REPORTS_TABLE = "CREATE TABLE " + TABLE_REPORTS + "("
                    + KEY_REPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_TITLE + " TEXT NOT NULL,"
                    + KEY_DESCRIPTION + " TEXT NOT NULL,"
                    + KEY_LOCATION + " TEXT NOT NULL,"
                    + KEY_IMAGE_PATH + " TEXT,"
                    + KEY_CONTACT_INFO + " TEXT,"
                    + KEY_IS_FOUND + " INTEGER DEFAULT 0,"
                    + KEY_TIMESTAMP + " INTEGER"
                    + ")";
            db.execSQL(CREATE_REPORTS_TABLE);
            Log.d("DatabaseHandler", "Reports table created successfully");

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
<<<<<<< HEAD
//    public long addEvent(Event event) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        values.put(KEY_EVENT_TITLE, event.getTitle());
//        values.put(KEY_EVENT_DESCRIPTION, event.getDescription());
//        values.put(KEY_EVENT_TIMESTAMP, event.getTimestamp());
//
//        long id = db.insert(TABLE_EVENTS, null, values);
//        db.close();
//        return id;
//    }
//    public List<Event> getAllEvents() {
//        List<Event> eventList = new ArrayList<Event>();
//        // Select All Query
//        String selectQuery = "SELECT * FROM " + TABLE_EVENTS;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // Looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Event event = new Event();
//                event.setId(Integer.parseInt(cursor.getString(0))); // ID
//                event.setTitle(cursor.getString(1));                // Title
//                event.setDescription(cursor.getString(2));          // Description
//                event.setTimestamp(cursor.getString(3));            // Timestamp
//
//                // Adding event to list
//                eventList.add(event);
//            } while (cursor.moveToNext());
//        }
//
//        // Closing the cursor
//        cursor.close();
//
//        // Return event list
//        return eventList;
//    }
=======

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

        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int isAdmin = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_ADMIN));
                return isAdmin == 1;  // If is_admin is 1, user is an admin
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log any unexpected exceptions
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;  // Default to false if no record is found or an error occurs
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
>>>>>>> origin/main



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
        values.put(KEY_IS_FOUND, 0); // 0 indicates not found
        values.put(KEY_TIMESTAMP, System.currentTimeMillis());

        // Insert row and get the id
        long id = db.insert(TABLE_REPORTS, null, values);
        db.close();

        return id;
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
                report.setId(cursor.getLong(cursor.getColumnIndex(KEY_REPORT_ID)));
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
<<<<<<< HEAD

    public void markItemAsFound(String itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_FOUND, 1); // 1 indicates the item is found

        // Update the record
        db.update(TABLE_REPORTS,
                values,
                KEY_REPORT_ID + " = ?",
                new String[]{itemId});
        db.close();
    }

    public List<Report> getAllReports(boolean isFound) {
        List<Report> reportList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_REPORTS +
                " WHERE " + KEY_IS_FOUND + " = " + (isFound ? "1" : "0") +
                " ORDER BY " + KEY_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Report report = new Report();
                report.setId(cursor.getLong(0));
                report.setTitle(cursor.getString(1));
                report.setDescription(cursor.getString(2));
                report.setLocation(cursor.getString(3));
                report.setImageUrl(cursor.getString(4));
                report.setFound(cursor.getInt(5) == 1);
                report.setTimestamp(cursor.getLong(6));
                reportList.add(report);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reportList;
    }

    public long addReport(Report report) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, report.getTitle());
        values.put(KEY_DESCRIPTION, report.getDescription());
        values.put(KEY_LOCATION, report.getLocation());
        values.put(KEY_IMAGE_PATH, report.getImageUrl());
        values.put(KEY_CONTACT_INFO, report.getContactInfo());
        values.put(KEY_IS_FOUND, report.isFound() ? 1 : 0);
        values.put(KEY_TIMESTAMP, report.getTimestamp());

        // Insert row and get the id
        long id = db.insert(TABLE_REPORTS, null, values);
        db.close();

        return id;
    }
=======
    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { KEY_USER_ID };  // Assuming KEY_USER_ID is the column for the user ID
        String selection = KEY_EMAIL + " = ?";  // Assuming KEY_USER_EMAIL is the column for email
        String[] selectionArgs = { email };

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID));  // Retrieve the userId
            cursor.close();
            return userId;
        }

        if (cursor != null) {
            cursor.close();
        }

        return -1;  // Return -1 if no user is found, meaning the email is invalid
    }


>>>>>>> origin/main
}
