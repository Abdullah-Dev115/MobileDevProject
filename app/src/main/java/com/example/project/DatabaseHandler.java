package com.example.project;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;


import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 8;
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
    private static final String TABLE_FOUND_REPORTS = "found_reports";
    private static final String TABLE_REPORTS = "reports";
    private static final String KEY_REPORT_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_CONTACT_INFO = "contact_info";


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

            // Create Events table
            String CREATE_EVENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS + "("
                    + KEY_EVENT_ID + " INTEGER PRIMARY KEY,"
                    + KEY_EVENT_TITLE + " TEXT,"
                    + KEY_EVENT_DESCRIPTION + " TEXT,"
                    + KEY_EVENT_TIMESTAMP + " TEXT,"
                    + KEY_EVENT_ADMIN_ID + " INTEGER"
                    + ")";

            db.execSQL(CREATE_USERS_TABLE);
            db.execSQL(CREATE_EVENTS_TABLE);
            Log.d("DatabaseHandler", "Users table created successfully");

            // Create Reports Table
            String CREATE_REPORTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_REPORTS + "("
                    + KEY_REPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_TITLE + " TEXT NOT NULL,"
                    + KEY_DESCRIPTION + " TEXT NOT NULL,"
                    + KEY_LOCATION + " TEXT NOT NULL,"
                    + KEY_IMAGE_PATH + " TEXT,"
                    + KEY_CONTACT_INFO + " TEXT,"

                    + KEY_TIMESTAMP + " INTEGER"
                    + ")";
            String CREATE_FOUND_REPORTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_FOUND_REPORTS + "("
                    + KEY_REPORT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + KEY_TITLE + " TEXT NOT NULL,"
                    + KEY_DESCRIPTION + " TEXT NOT NULL,"
                    + KEY_LOCATION + " TEXT NOT NULL,"
                    + KEY_IMAGE_PATH + " TEXT,"
                    + KEY_CONTACT_INFO + " TEXT,"

                    + KEY_TIMESTAMP + " INTEGER"
                    + ")";
            db.execSQL(CREATE_FOUND_REPORTS_TABLE);
            db.execSQL(CREATE_REPORTS_TABLE);
            Log.d("DatabaseHandler", "Reports table created successfully");

        } catch (Exception e) {
            Log.e("DatabaseHandler", "Error creating tables: " + e.getMessage(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//         Drop all existing tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOUND_REPORTS);

        // Recreate all tables
        onCreate(db);
    }

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

        // Insert row and get the id
        long id = db.insert(TABLE_REPORTS, null, values);
        db.close();

        return id;
    }

    /**
     * Gets all lost reports
     * @return List of lost reports
     */
    public List<Report> getAllLostReports() {
        Log.d(TAG, "Fetching all lost reports");
        List<Report> reportList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_REPORTS +
                    " ORDER BY " + KEY_TIMESTAMP + " DESC";

            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    reportList.add(createReportFromCursor(cursor));
                } while (cursor.moveToNext());
            }

            Log.d(TAG, "Retrieved " + reportList.size() + " lost reports");
            return reportList;

        } catch (SQLException e) {
            Log.e(TAG, "Error getting lost reports: " + e.getMessage(), e);
            return new ArrayList<>();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * Gets all found reports
     * @return List of found reports
     */
    public List<Report> getAllFoundReports() {
        Log.d(TAG, "Fetching all found reports");
        List<Report> reportList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT * FROM " + TABLE_FOUND_REPORTS +
                    " ORDER BY " + KEY_TIMESTAMP + " DESC";

            db = this.getReadableDatabase();
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    reportList.add(createReportFromCursor(cursor));
                } while (cursor.moveToNext());
            }

            Log.d(TAG, "Retrieved " + reportList.size() + " found reports");
            return reportList;

        } catch (SQLException e) {
            Log.e(TAG, "Error getting found reports: " + e.getMessage(), e);
            return new ArrayList<>();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }
    /**
     * Creates a Report object from cursor data
     * @param cursor Database cursor positioned at valid row
     * @return Report object
     */
    private Report createReportFromCursor(Cursor cursor) {
        Report report = new Report();
        try {
            report.setId(cursor.getLong(getColumnIndexOrThrow(cursor, KEY_REPORT_ID)));
            report.setTitle(cursor.getString(getColumnIndexOrThrow(cursor, KEY_TITLE)));
            report.setDescription(cursor.getString(getColumnIndexOrThrow(cursor, KEY_DESCRIPTION)));
            report.setLocation(cursor.getString(getColumnIndexOrThrow(cursor, KEY_LOCATION)));
            report.setImageUrl(cursor.getString(getColumnIndexOrThrow(cursor, KEY_IMAGE_PATH)));
            report.setContactInfo(cursor.getString(getColumnIndexOrThrow(cursor, KEY_CONTACT_INFO)));
            report.setTimestamp(cursor.getLong(getColumnIndexOrThrow(cursor, KEY_TIMESTAMP)));
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Error creating report from cursor: " + e.getMessage(), e);
        }
        return report;
    }

    /**
     * Safe way to get column index that handles missing columns
     */
    private int getColumnIndexOrThrow(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Column '" + columnName + "' not found in database");
        }
        return columnIndex;
    }
    /**
     * Gets report count from specified table
     */









    public long addReport(Report report) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, report.getTitle());
        values.put(KEY_DESCRIPTION, report.getDescription());
        values.put(KEY_LOCATION, report.getLocation());
        values.put(KEY_IMAGE_PATH, report.getImageUrl());
        values.put(KEY_CONTACT_INFO, report.getContactInfo());// Convert boolean to int
        values.put(KEY_TIMESTAMP, System.currentTimeMillis());



        long id = db.insert(TABLE_REPORTS, null, values);
        
        // Verify the insertion

            



        
        db.close();
        return id;
    }
    public long addFoundReport(Report report) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, report.getTitle());
        values.put(KEY_DESCRIPTION, report.getDescription());
        values.put(KEY_LOCATION, report.getLocation());
        values.put(KEY_IMAGE_PATH, report.getImageUrl());
        values.put(KEY_CONTACT_INFO, report.getContactInfo());
        values.put(KEY_TIMESTAMP, System.currentTimeMillis());



        long id = db.insert(TABLE_FOUND_REPORTS, null, values);
        db.close();
        return id;
    }

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

        // Return -1 if no user is found, meaning the email is invalid
        return -1;  
    }


    public void deleteReport(String reportId) {
            SQLiteDatabase db = this.getWritableDatabase();
            int rowsDeleted = db.delete(TABLE_REPORTS, KEY_REPORT_ID + " = ?", new String[] { reportId });
            if (rowsDeleted > 0) {
                Log.d("Database", "Report deleted successfully");
            } else {
                Log.d("Database", "is the report id" + reportId);
            }
            db.close();
        }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        
        String[] columns = {
            KEY_USER_ID,
            KEY_USERNAME,
            KEY_EMAIL,
            KEY_PHONE,
            KEY_IS_ADMIN
        };
        
        String selection = KEY_EMAIL + " = ?";
        String[] selectionArgs = {email};
        
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(KEY_USERNAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL)));
            user.setPhone(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE)));
            user.setIsAdmin(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_ADMIN)) == 1);
            cursor.close();
        }
        
        return user;
    }

    }



