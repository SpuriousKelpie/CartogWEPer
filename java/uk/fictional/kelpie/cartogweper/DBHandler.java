package uk.spurious.kelpie.cartogweper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "results";
    private static final String KEY_ID = "id";
    private static final String KEY_SSID = "ssid";
    private static final String KEY_MAC = "mac";
    private static final String KEY_PROTOCOL = "protocol";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_RSSI = "rssi";
    private static final String KEY_FREQUENCY = "frequency";

    public DBHandler(Context context, String DB_NAME) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_SSID + " TEXT, " + KEY_MAC + " TEXT, " + KEY_PROTOCOL + " TEXT, " + KEY_LATITUDE + " TEXT, " + KEY_LONGITUDE + " TEXT," +
                KEY_RSSI + " TEXT, " + KEY_FREQUENCY + " TEXT" + " ) ";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
        onCreate(db);
    }

    public void insertRecord(String ssid, String mac, String protocol, String latitude, String longitude, int rssi, int frequency) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SSID, ssid);
        values.put(KEY_MAC, mac);
        values.put(KEY_PROTOCOL, protocol);
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);
        values.put(KEY_RSSI, rssi);
        values.put(KEY_FREQUENCY, frequency);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteRecord(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID+"="+id, null);
    }

    public long getTotalRecords(){
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }

    public int getTotalOpenRecords(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_PROTOCOL + " LIKE " +
                "'" + "open" + "'", null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            cursor.close();
            return cursor.getCount();
        }

        return 0;
    }

    public int getTotalWPARecords(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_PROTOCOL + " LIKE " +
                "'" + "wpa" + "'", null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            cursor.close();
            return cursor.getCount();
        }

        return 0;
    }

    public int getTotalWEPRecords(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_PROTOCOL + " LIKE " +
                "'" + "wep" + "'", null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            cursor.close();
            return cursor.getCount();
        }

        return 0;
    }

    public List<String> getSSIDs(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> array = new ArrayList<String>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                String element = cursor.getString(cursor.getColumnIndex("ssid"));
                array.add(element);
            }
            return array;
        }

        return array;
    }

    public List<String> getMacs(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> array = new ArrayList<String>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                String element = cursor.getString(cursor.getColumnIndex("mac"));
                array.add(element);
            }
            return array;
        }

        return array;
    }

    public List<String> getProtocols(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> array = new ArrayList<String>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                String element = cursor.getString(cursor.getColumnIndex("protocol"));
                array.add(element);
            }
            return array;
        }

        return array;
    }

    public List<Double> getLatitudes(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Double> array = new ArrayList<Double>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                Double element = cursor.getDouble(cursor.getColumnIndex("latitude"));
                array.add(element);
            }
            return array;
        }

        return array;
    }

    public List<Double> getLongitudes(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Double> array = new ArrayList<Double>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                Double element = cursor.getDouble(cursor.getColumnIndex("longitude"));
                array.add(element);
            }
            return array;
        }

        return array;
    }

    public List<String> getRSSIs(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> array = new ArrayList<String>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                String element = cursor.getString(cursor.getColumnIndex("rssi"));
                array.add(element);
            }
            return array;
        }

        return array;
    }

    public List<String> getFrequencies(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> array = new ArrayList<String>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            while(cursor.moveToNext()){
                String element = cursor.getString(cursor.getColumnIndex("frequency"));
                array.add(element);
            }
            return array;
        }

        return array;
    }
}
