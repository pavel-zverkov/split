package ru.zverkov_studio.split;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.util.HashMap;

public class DataBasePersons {

    private static final String DB_NAME = "persons";
    private static final int DB_VERSION = 1;
    private static final String TABLE_CLUB = "club_table";
    private static final String TABLE_DECLARED = "declared_table";
    private static final String TABLE_UNDECLARED = "undeclared_table";


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_QUALIFY = "qualify";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";

    private static final HashMap<Integer, long[]> times = new HashMap<>();

    private static final String TABLE_CLUB_CREATE;
    static {
        TABLE_CLUB_CREATE = "create table if not exists " + TABLE_CLUB + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_NAME + " text, " +
                COLUMN_BIRTHDAY + " text, " +
                COLUMN_GENDER + " text, " +
                COLUMN_QUALIFY + " text, " +
                COLUMN_EMAIL + " text, " +
                COLUMN_PHONE + " text" +
                ");";
    }

    private static final String TABLE_DECLARED_CREATE;
    static {
        TABLE_DECLARED_CREATE = "create table if not exists " + TABLE_DECLARED + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_NAME + " text, " +
                COLUMN_BIRTHDAY + " text, " +
                COLUMN_GENDER + " text, " +
                COLUMN_QUALIFY + " text, " +
                COLUMN_EMAIL + " text, " +
                COLUMN_PHONE + " text" +
                ");";
    }

    private static final String TABLE_UNDECLARED_CREATE;
    static {
        TABLE_UNDECLARED_CREATE = "create table if not exists " + TABLE_UNDECLARED + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_NAME + " text, " +
                COLUMN_BIRTHDAY + " text, " +
                COLUMN_GENDER + " text, " +
                COLUMN_QUALIFY + " text, " +
                COLUMN_EMAIL + " text, " +
                COLUMN_PHONE + " text" +
                ");";
    }

    private final Context mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DataBasePersons(Context context) {
        mContext = context;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData(String TABLE, String COLUMN) {
        return mDB.query(TABLE, null, null, null, null, null, COLUMN);
    }

    // добавить запись в DB_TABLE
    public void addRec(String TABLE, ContentValues cv) {
        mDB.insert(TABLE, null, cv);
    }

    // удалить запись из DB_TABLE
    public void delRec(String TABLE, long id) {
        mDB.delete(TABLE, COLUMN_ID + " = " + id, null);
    }

    public void create_additional_tables() {
        mDB.execSQL(TABLE_UNDECLARED_CREATE);
        ContentValues cv = new ContentValues();
        Cursor mCursor = getAllData(TABLE_CLUB, COLUMN_NAME);
        mCursor.moveToFirst();
        for (int i = 0; i < mCursor.getCount(); i ++){
            cv.put(COLUMN_NAME, mCursor.getString(mCursor.getColumnIndex(COLUMN_NAME)));
            cv.put(COLUMN_BIRTHDAY, mCursor.getString(mCursor.getColumnIndex(COLUMN_BIRTHDAY)));
            cv.put(COLUMN_GENDER, mCursor.getString(mCursor.getColumnIndex(COLUMN_GENDER)));
            cv.put(COLUMN_QUALIFY, mCursor.getString(mCursor.getColumnIndex(COLUMN_QUALIFY)));
            cv.put(COLUMN_EMAIL, mCursor.getString(mCursor.getColumnIndex(COLUMN_EMAIL)));
            cv.put(COLUMN_PHONE, mCursor.getString(mCursor.getColumnIndex(COLUMN_PHONE)));
            mDB.insert(TABLE_UNDECLARED, null, cv);
            mCursor.moveToNext();
        }

        mDB.execSQL(TABLE_DECLARED_CREATE);
    }

    public void all_declared() {
        ContentValues cv = new ContentValues();
        Cursor mCursor = getAllData(TABLE_UNDECLARED, COLUMN_NAME);
        mCursor.moveToFirst();
        for (int i = 0; i < mCursor.getCount(); i ++){
            cv.put(COLUMN_NAME, mCursor.getString(mCursor.getColumnIndex(COLUMN_NAME)));
            cv.put(COLUMN_BIRTHDAY, mCursor.getString(mCursor.getColumnIndex(COLUMN_BIRTHDAY)));
            cv.put(COLUMN_GENDER, mCursor.getString(mCursor.getColumnIndex(COLUMN_GENDER)));
            cv.put(COLUMN_QUALIFY, mCursor.getString(mCursor.getColumnIndex(COLUMN_QUALIFY)));
            cv.put(COLUMN_EMAIL, mCursor.getString(mCursor.getColumnIndex(COLUMN_EMAIL)));
            cv.put(COLUMN_PHONE, mCursor.getString(mCursor.getColumnIndex(COLUMN_PHONE)));
            mDB.insert(TABLE_DECLARED, null, cv);
            mCursor.moveToNext();
        }

        mDB.execSQL(String.format("drop table if exists %s", TABLE_UNDECLARED));
        mDB.execSQL(TABLE_UNDECLARED_CREATE);
    }

    public void drop_additional_tables() {
        mDB.execSQL(String.format("drop table if exists %s", TABLE_DECLARED));
        mDB.execSQL(String.format("drop table if exists %s", TABLE_UNDECLARED));
    }


    public void put_times(HashMap<Integer, long[]> times_array){
        for (int key: times_array.keySet()){
            times.put(key, times_array.get(key));
        }
        Log.d("DataBase put_times", String.valueOf(times));
    }

    public HashMap<String, Long> get_times(int position) {
        HashMap<String, Long> point_times = new HashMap<String, Long>();
        long start_time, time;
        Cursor name;
        Log.d("DataBase get_times", String.valueOf(times));
        for(int key: times.keySet()){
            start_time = times.get(key)[0];
            time = times.get(key)[position];
            if (start_time != 0 & time != 0){
                name = mDB.query(TABLE_DECLARED, null, COLUMN_ID + " = " + key, null, null, null, null);
                Log.d("Recording", String.valueOf(name.getCount()));
                name.moveToFirst();
                if (position == 0){
                    point_times.put(name.getString(name.getColumnIndex(COLUMN_NAME)), start_time);
                }
                else {
                    point_times.put(name.getString(name.getColumnIndex(COLUMN_NAME)), time - start_time);
                }

            }
        }
        for(String key: point_times.keySet()){
            Log.d("Database getting times", key + point_times.get(key).toString());
        }

        return point_times;
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
            db.execSQL(TABLE_CLUB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}

