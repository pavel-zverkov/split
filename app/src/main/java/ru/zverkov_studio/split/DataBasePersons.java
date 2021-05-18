package ru.zverkov_studio.split;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;

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
    public Cursor getAllData(String TABLE) {
        return mDB.query(TABLE, null, null, null, null, null, COLUMN_NAME);
    }

    // добавить запись в DB_TABLE
    public void addRec(String TABLE, ContentValues cv) {
        mDB.insert(TABLE, null, cv);
    }

    // удалить запись из DB_TABLE
    public void delRec(String TABLE, String id) {
        mDB.delete(TABLE, COLUMN_ID + " LIKE " + "'%" + id + "%'", null);
    }

    public void create_additional_tables() {
        mDB.execSQL(TABLE_UNDECLARED_CREATE);
        ContentValues cv = new ContentValues();
        Cursor club_data = getAllData(TABLE_CLUB);
        club_data.moveToFirst();
        for (int i = 0; i < club_data.getCount(); i ++){
            cv.put(COLUMN_NAME, club_data.getString(club_data.getColumnIndex(COLUMN_NAME)));
            cv.put(COLUMN_BIRTHDAY, club_data.getString(club_data.getColumnIndex(COLUMN_BIRTHDAY)));
            cv.put(COLUMN_GENDER, club_data.getString(club_data.getColumnIndex(COLUMN_GENDER)));
            cv.put(COLUMN_QUALIFY, club_data.getString(club_data.getColumnIndex(COLUMN_QUALIFY)));
            cv.put(COLUMN_EMAIL, club_data.getString(club_data.getColumnIndex(COLUMN_EMAIL)));
            cv.put(COLUMN_PHONE, club_data.getString(club_data.getColumnIndex(COLUMN_PHONE)));
            mDB.insert(TABLE_UNDECLARED, null, cv);
            club_data.moveToNext();
        }

        mDB.execSQL(TABLE_DECLARED_CREATE);
    }

    public void drop_additional_tables() {
        mDB.execSQL(String.format("drop table if exists %s", TABLE_DECLARED));
        mDB.execSQL(String.format("drop table if exists %s", TABLE_UNDECLARED));
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

