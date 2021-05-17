package ru.zverkov_studio.split;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

public class DBtoList {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_QUALIFY = "qualify";
    public static final String COLUMN_GENDER = "gender";

    Context mContext;
    ArrayList<String[]> data = new ArrayList<String[]>();
    DataBasePersons club;
    Cursor cursor;
    String[] row_ind = new String[5];

    public DBtoList(Context context){
        mContext = context;
    }

    public ArrayList<String[]> get_data(){
        club = new DataBasePersons(mContext);
        club.open();

        cursor = club.getAllData();
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++){
            String[] row = new String[5];
            row[0] = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
            row[1] = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            row[2] = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY));
            row[3] = cursor.getString(cursor.getColumnIndex(COLUMN_QUALIFY));
            row[4] = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));
            data.add(row);

            cursor.moveToNext();
        }

        club.close();
        return data;
    }
}
