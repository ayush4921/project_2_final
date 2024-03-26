package com.example.project2final;

import android.content.ContentValues; import android.content.Context; import android.database.Cursor; import android.database.sqlite.SQLiteDatabase; import android.database.sqlite.SQLiteOpenHelper; import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets; import java.security.MessageDigest; import java.security.NoSuchAlgorithmException; import java.util.ArrayList; import java.util.List;

public class DBClass extends SQLiteOpenHelper { private static final String DATABASE_NAME = "MentalHealthApp.db"; private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_NOTES = "notes";
    private static final String COLUMN_NOTE_ID = "id";
    private static final String COLUMN_NOTE_TEXT = "note_text";
    private static final String COLUMN_USER_ID = "user_id";

    private static final String TABLE_ACTIVITIES = "activities";
    private static final String COLUMN_ACTIVITY_ID = "id";
    private static final String COLUMN_ACTIVITY_NAME = "activity_name";
    private static final String COLUMN_ACTIVITY_DONE = "activity_done";
    private static final String COLUMN_ACTIVITY_USER_ID = "user_id";

    private static final String TABLE_TRACKER = "tracker";
    private static final String COLUMN_TRACKER_ID = "id";
    private static final String COLUMN_MOOD = "mood";
    private static final String COLUMN_ANXIETY_LEVEL = "anxiety_level";
    private static final String COLUMN_MEDICATION_ADHERENCE = "medication_adherence";
    private static final String COLUMN_TRACKER_USER_ID = "user_id";

    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_AGE + " INTEGER,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_USERNAME + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT"
                + ")";

        String createNotesTable = "CREATE TABLE " + TABLE_NOTES + "("
                + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NOTE_TEXT + " TEXT,"
                + COLUMN_USER_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")"
                + ")";

        String createActivitiesTable = "CREATE TABLE " + TABLE_ACTIVITIES + "("
                + COLUMN_ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ACTIVITY_NAME + " TEXT,"
                + COLUMN_ACTIVITY_DONE + " INTEGER,"
                + COLUMN_ACTIVITY_USER_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_ACTIVITY_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")"
                + ")";

        String createTrackerTable = "CREATE TABLE " + TABLE_TRACKER + "("
                + COLUMN_TRACKER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MOOD + " TEXT,"
                + COLUMN_ANXIETY_LEVEL + " INTEGER,"
                + COLUMN_MEDICATION_ADHERENCE + " TEXT,"
                + COLUMN_TRACKER_USER_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_TRACKER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + ")"
                + ")";

        db.execSQL(createUsersTable);
        db.execSQL(createNotesTable);
        db.execSQL(createActivitiesTable);
        db.execSQL(createTrackerTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKER);
        onCreate(db);
    }

    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        int userId = -1; // Default to -1 to indicate not found
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_ID);
            if (columnIndex != -1) { // Check if the column index is valid
                userId = cursor.getInt(columnIndex);
            } else {
                // Handle the case where the column index is -1 (column not found)
                Log.e("DBClass", "Column ID not found in the result set");
            }
        }
        cursor.close();
        return userId;
    }

    public boolean addUser(String name, int age, String gender, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, hashPassword(password));
        long result = db.insert(TABLE_USERS, null, values);
        //db.close();();
        return result != -1;
    }

    public boolean isValidLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean isValid = false;
        if (cursor.moveToFirst()) {
            String storedHashedPassword = cursor.getString(0);
            String hashedPassword = hashPassword(password);
            isValid = storedHashedPassword.equals(hashedPassword);
        }
        cursor.close();
        //db.close();();
        return isValid;
    }

    public String getUserName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_NAME + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        String name = "";
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_NAME);
            if (columnIndex != -1) {
                name = cursor.getString(columnIndex);
            }
        }
        cursor.close();
        ////db.close();();
        return name;
    }
    public void addNote(String noteText, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TEXT, noteText);
        values.put(COLUMN_USER_ID, userId);
        db.insert(TABLE_NOTES, null, values);
        ////db.close();();
    }

    public List<String> getAllNotes(int userId) {
        List<String> notes = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT " + COLUMN_NOTE_TEXT + " FROM " + TABLE_NOTES + " WHERE " + COLUMN_USER_ID + "=?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            int columnIndex = cursor.getColumnIndex(COLUMN_NOTE_TEXT);
            if (columnIndex != -1) {
                while (cursor.moveToNext()) {
                    String note = cursor.getString(columnIndex);
                    notes.add(note);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                ////db.close();();
            }
        }
        return notes;
    }

    public void deleteNote(String noteText, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_NOTE_TEXT + "=? AND " + COLUMN_USER_ID + "=?";
        String[] whereArgs = {noteText, String.valueOf(userId)};
        db.delete(TABLE_NOTES, whereClause, whereArgs);
        ////db.close();();
    }

    public void addActivity(String activityName, boolean activityDone, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_NAME, activityName);
        values.put(COLUMN_ACTIVITY_DONE, activityDone ? 1 : 0);
        values.put(COLUMN_ACTIVITY_USER_ID, userId);
        db.insert(TABLE_ACTIVITIES, null, values);
        ////db.close();();
    }

    public List<String> getActivities(int userId) {
        List<String> activities = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT " + COLUMN_ACTIVITY_NAME + " FROM " + TABLE_ACTIVITIES + " WHERE " + COLUMN_ACTIVITY_USER_ID + "=?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            int columnIndex = cursor.getColumnIndex(COLUMN_ACTIVITY_NAME);
            if (columnIndex != -1) {
                while (cursor.moveToNext()) {
                    String activity = cursor.getString(columnIndex);
                    activities.add(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                ////db.close();();
            }
        }
        return activities;
    }

    public void updateActivityStatus(String activityName, boolean activityDone, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ACTIVITY_DONE, activityDone ? 1 : 0);
        String whereClause = COLUMN_ACTIVITY_NAME + "=? AND " + COLUMN_ACTIVITY_USER_ID + "=?";
        String[] whereArgs = {activityName, String.valueOf(userId)};
        db.update(TABLE_ACTIVITIES, values, whereClause, whereArgs);
        ////db.close();();
    }

    public void addTrackerEntry(String mood, int anxietyLevel, String medicationAdherence, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MOOD, mood);
        values.put(COLUMN_ANXIETY_LEVEL, anxietyLevel);
        values.put(COLUMN_MEDICATION_ADHERENCE, medicationAdherence);
        values.put(COLUMN_TRACKER_USER_ID, userId);
        db.insert(TABLE_TRACKER, null, values);
        ////db.close();();
    }

    public String getLatestMood(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_MOOD + " FROM " + TABLE_TRACKER + " WHERE " + COLUMN_TRACKER_USER_ID + "=? ORDER BY " + COLUMN_TRACKER_ID + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        String mood = "";
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_MOOD);
            if (columnIndex != -1) {
                mood = cursor.getString(columnIndex);
            }
        }
        cursor.close();
        ////db.close();();
        return mood;
    }

    public int getLatestAnxietyLevel(int userId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int anxietyLevel = 0;
        try {
            db = this.getReadableDatabase();
            String query = "SELECT " + COLUMN_ANXIETY_LEVEL + " FROM " + TABLE_TRACKER + " WHERE " + COLUMN_TRACKER_USER_ID + "=? ORDER BY " + COLUMN_TRACKER_ID + " DESC LIMIT 1";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            int columnIndex = cursor.getColumnIndex(COLUMN_ANXIETY_LEVEL);
            if (columnIndex != -1 && cursor.moveToFirst()) {
                anxietyLevel = cursor.getInt(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                ////db.close();();
            }
        }
        return anxietyLevel;
    }

    public String getLatestMedicationAdherence(int userId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String medicationAdherence = "";
        try {
            db = this.getReadableDatabase();
            String query = "SELECT " + COLUMN_MEDICATION_ADHERENCE + " FROM " + TABLE_TRACKER + " WHERE " + COLUMN_TRACKER_USER_ID + "=? ORDER BY " + COLUMN_TRACKER_ID + " DESC LIMIT 1";
            cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
            int columnIndex = cursor.getColumnIndex(COLUMN_MEDICATION_ADHERENCE);
            if (columnIndex != -1 && cursor.moveToFirst()) {
                medicationAdherence = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                ////db.close();();
            }
        }
        return medicationAdherence;
    }

    public String hashPassword(String password) {
        String hashedPassword = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }
}