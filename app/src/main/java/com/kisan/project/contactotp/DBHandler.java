package com.kisan.project.contactotp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class DBHandler extends SQLiteOpenHelper
{
    // All Static variables
    private static ArrayList<String> msgList;
    public static JSONObject msg_json = new JSONObject();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "sentMessages";

    // Contacts table name
    private static final String TABLE_MESSAGE = "msgDetails";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "contact_number";
    private static final String KEY_TIME = "time";
    private static final String KEY_OTP = "otp";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MSGTABLE_TABLE = "CREATE TABLE " + TABLE_MESSAGE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_PH_NO + " TEXT," + KEY_TIME + " TEXT," + KEY_OTP + " TEXT" + ")";

        db.execSQL(CREATE_MSGTABLE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    void addMsg(String name, String phone_no, String time, String _otp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Contact Name
        values.put(KEY_PH_NO, phone_no); // Contact Phone
        values.put(KEY_TIME, time); // Time
        values.put(KEY_OTP, _otp); // Otp
        // Inserting Row
        db.insert(TABLE_MESSAGE, null, values);
        db.close(); // Closing database connection
    }
    // Getting contacts Count
    public int getMsgCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MESSAGE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int h = cursor.getCount();
        cursor.close();

        // return count
        return h;
    }

    public void deleteRecord()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_MESSAGE);
    }

    // Deleting single msg details
    public void deleteMsgDetail(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MESSAGE, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }


    // Getting All sent msg details
    public JSONObject getAllMsgDetails() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        msgList = new ArrayList<>();
        JSONArray jlist = new JSONArray();
        JSONObject item;
        // looping through all rows and adding to list
        if (cursor.moveToFirst())
        {
            do {
                    try {
                            item = new JSONObject();

                            item.put("_id", cursor.getString(0));
                            item.put("_name", cursor.getString(1));
                            item.put("_number", cursor.getString(2));
                            item.put("_time", cursor.getString(3));
                            item.put("_otp", cursor.getString(4));
                            jlist.put(item);

                            msg_json.put("msgdata", jlist);
                        }
                    catch (Exception e)
                        {
                            e.printStackTrace();
                        }

            }while (cursor.moveToNext());
        }

        return msg_json;
    }
}
