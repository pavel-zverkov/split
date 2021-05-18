package ru.zverkov_studio.split;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DataBaseActivity {

    private static final String DB_NAME = "activities";
    private static final int DB_VERSION = 1;
    private static final String TABLE_TRACK = "track_points_table";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_POINT = "name";

    private static final String TABLE_TRACK_CREATE;
    static {
        TABLE_TRACK_CREATE = "create table if not exists" + TABLE_TRACK + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_POINT + " text);";
    }

    private final Context mContext;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DataBaseActivity(Context context) {
        mContext = context;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mContext, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        mDB.execSQL("drop table " + TABLE_TRACK);
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

    public void create_track_table() {
        mDB.execSQL(TABLE_TRACK_CREATE);

        ContentValues cv = new ContentValues();
        String[] points = new String[] {"СТАРТ", "ФИНИШ"};
        for (String point: points){
            cv.put(COLUMN_POINT, point);
            mDB.insert(TABLE_TRACK, null, cv);
        }
    }

    public void drop_track_table() {
        mDB.execSQL(String.format("drop table if exists %s", TABLE_TRACK));
    }

    public Cursor getTables() {
        String mySql = "SELECT name FROM sqlite_master " + " WHERE type='table'";
        return mDB.rawQuery(mySql, null);
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

