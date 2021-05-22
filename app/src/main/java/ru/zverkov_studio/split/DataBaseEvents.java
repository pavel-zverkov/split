package ru.zverkov_studio.split;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DataBaseEvents {

    private static final String[] event_data = new String[4];
    private static final ArrayList<String> track = new ArrayList<String>();

    private static final String DB_NAME = "activities";
    private static final int DB_VERSION = 1;
    private static final String TABLE_TRACK = "track_points_table";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_POINT = "name";

    private static final String TABLE_TRACK_CREATE;
    static {
        TABLE_TRACK_CREATE = "create table if not exists " + TABLE_TRACK + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_POINT + " text);";
    }

    private final Context mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DataBaseEvents(Context context) {
        mContext = context;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        drop_track_table();
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData(String TABLE) {
        return mDB.query(TABLE, null, null, null, null, null, null);
    }

    // добавить запись в DB_TABLE
    public void addRec(String TABLE, ContentValues cv) {
        mDB.insert(TABLE, null, cv);
    }

    // удалить запись из DB_TABLE
    public void delRec(String TABLE, String id) {
        mDB.delete(TABLE, COLUMN_ID + " LIKE " + "'%" + id + "%'", null);
    }

    public void create_activity_table(String name, List<String> columns, ArrayList<String[]> data) {
        //Creating table
        String query;
        query = String.format("create table if not exists %s (", name);
        for (String column: columns){
            query += column + " text, ";
        }
        query += COLUMN_ID + " integer primary key autoincrement);";
        mDB.execSQL(query);

        ContentValues cv = new ContentValues();
        String[] row;
        for (int i = 0; i < data.size(); i++){
            row = (String[]) data.get(i);
            for (int j = 0; j < row.length; j++){
                cv.put(columns.get(j), row[j]);
            }
            mDB.insert(name, null, cv);
        }
    }

    public Cursor getTables() {
        String mySql = "SELECT name FROM sqlite_master " + " WHERE type='table'";
        return mDB.rawQuery(mySql, null);
    }

    public void fill_event_data(String event_name, String event_date, String kind_sport, String kind_start) {
        event_data[0] = event_name;
        event_data[1] = event_date;
        event_data[2] = kind_sport;
        event_data[3] = kind_start;
    }

    public void clear_event_data() {
        Arrays.fill(event_data, null);
    }

    public String[] get_event_data() {
        return event_data;
    }

    public void create_track_table() {
        String[] points = new String[] {"СТАРТ", "ФИНИШ"};
        for (String point: points){
            track.add(point);
        }
    }

    public void drop_track_table() {
        track.removeAll(track);
    }

    public void add_point(int position, String item){
        track.add(position, item);
    }

    public void remove_point(int position){
        track.remove(position);
    }

    public ArrayList<String> get_track() {
        return track;
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}

