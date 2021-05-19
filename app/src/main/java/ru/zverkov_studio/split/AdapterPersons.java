package ru.zverkov_studio.split;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterPersons extends RecyclerView.Adapter<AdapterPersons.ViewHolder> {

    DataBasePersons persons;
    private static final String TABLE_DECLARED = "declared_table";
    private static final String TABLE_UNDECLARED = "undeclared_table";
    ContentValues cv = new ContentValues();

    private static final int PENDING_REMOVAL_TIMEOUT = 1000; // 3sec

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_QUALIFY = "qualify";
    public static final String COLUMN_GENDER = "gender";

    private Context mContext;
    String item;
    Cursor mCursor;
    boolean undoOn = true;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public AdapterPersons(Context context){
        mContext = context;
        open_DB();
        mCursor = persons.getAllData(TABLE_DECLARED);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        item = mCursor.getString(mCursor.getColumnIndex(COLUMN_ID));

        holder.id = item;
        holder.person_name.setText(mCursor.getString(mCursor.getColumnIndex(COLUMN_NAME)));
        holder.person_gender.setText(mCursor.getString(mCursor.getColumnIndex(COLUMN_ID)));
    }

    public void change(Cursor cursor){
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mCursor.moveToPosition(position);
        cv.put(COLUMN_NAME, mCursor.getString(mCursor.getColumnIndex(COLUMN_NAME)));
        cv.put(COLUMN_BIRTHDAY, mCursor.getString(mCursor.getColumnIndex(COLUMN_BIRTHDAY)));
        cv.put(COLUMN_GENDER, mCursor.getString(mCursor.getColumnIndex(COLUMN_GENDER)));
        cv.put(COLUMN_QUALIFY, mCursor.getString(mCursor.getColumnIndex(COLUMN_QUALIFY)));
        persons.addRec(TABLE_UNDECLARED, cv);
        persons.delRec(TABLE_DECLARED, mCursor.getString(mCursor.getColumnIndex(COLUMN_ID)));
        mCursor = persons.getAllData(TABLE_DECLARED);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        String id;
        TextView person_name, person_gender, number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            person_name = itemView.findViewById(R.id.person_activity_name);
            person_gender = itemView.findViewById(R.id.person_activity_gender);
        }
    }
    public void open_DB(){
        persons = new DataBasePersons(mContext);
        persons.open();
    }
}