package ru.zverkov_studio.split;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterClub extends RecyclerView.Adapter<AdapterClub.ViewHolder> {

    private static final int PENDING_REMOVAL_TIMEOUT = 1000; // 3sec

    private static final String TABLE_CLUB = "club_table";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_QUALIFY = "qualify";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";

    private Context mContext;
    private Cursor mCursor;
    private DataBasePersons mDB;
    List<String> itemsPendingRemoval;
    boolean undoOn = false;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public AdapterClub(Context context, DataBasePersons dataBasePersons){
        mContext = context;
        mDB = dataBasePersons;
        mCursor = mDB.getAllData(TABLE_CLUB, COLUMN_NAME);
        itemsPendingRemoval = new ArrayList<>();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_club_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        for (int i = 0; i < itemsPendingRemoval.size(); i++){
            Log.d("ITEM_REMOVAL", itemsPendingRemoval.get(i));
        }


        final String item = mCursor.getString(mCursor.getColumnIndex(COLUMN_ID));
        holder.id = item;

        if (position == 0){
            holder.card_item.setBackground(null);
        }

        if (itemsPendingRemoval.contains(item)) {
            Log.d("myLog", "itemsPendingRemoval - Position" + String.valueOf(position) + " Item - " + item);
            // we need to show the "undo" state of the row
            holder.card_item.setBackground(mContext.getResources().getDrawable(R.drawable.background_delete_club_item));
            holder.person_birthday.setVisibility(View.GONE);
            holder.person_name.setVisibility(View.GONE);
            holder.double_ok.setVisibility(View.GONE);
            holder.undoButton.setVisibility(View.VISIBLE);
            holder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(item);
                    pendingRunnables.remove(item);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(position);

                }
            });
        } else {
            // we need to show the "normal" state
            holder.card_item.setBackground(mContext.getResources().getDrawable(R.drawable.background_club_person_item));
            holder.person_name.setVisibility(View.VISIBLE);
            holder.person_birthday.setVisibility(View.VISIBLE);
            holder.person_name.setText(mCursor.getString(mCursor.getColumnIndex(COLUMN_NAME)));
            holder.person_birthday.setText(mCursor.getString(mCursor.getColumnIndex(COLUMN_ID)));
            holder.double_ok.setVisibility(View.VISIBLE);
            holder.undoButton.setVisibility(View.GONE);
            holder.undoButton.setOnClickListener(null);
        }
    }

    public void change(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        mCursor.moveToPosition(position);
        final String item = mCursor.getString(mCursor.getColumnIndex(COLUMN_ID));
        if (!itemsPendingRemoval.contains(item)) {
            Log.d("myLog", "pendingRemoval - Position " + String.valueOf(position) + " item- " + item);
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(position);
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        mCursor.moveToPosition(position);
        String item = mCursor.getString(mCursor.getColumnIndex(COLUMN_ID));
        Log.d("myLog", "Position for remove " + String.valueOf(position) + item);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        Log.d("myLog", "Delete from DB - " + item);
        mDB.delRec(TABLE_CLUB, mCursor.getInt(0));
        mCursor = mDB.getAllData(TABLE_CLUB, COLUMN_NAME);
        notifyItemRemoved(position);

    }

    public void addItem(Cursor cursor){
        int position = -1;
        mCursor.moveToFirst();
        cursor.moveToFirst();
        Log.d("myLog", "Quant = " + String.valueOf(getItemCount()));
        for (int i = 0; i < getItemCount(); i++){
            String old_id = mCursor.getString(mCursor.getColumnIndex(COLUMN_ID));
            String new_id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
            Log.d("myLog", "Old - " + old_id + " New - " + new_id);
            if (!old_id.equals(new_id)){
                Log.d("myLog", "Old - " + old_id + " New - " + new_id);
                position = i;
                break;
            }
            else {
                mCursor.moveToNext();
                cursor.moveToNext();
            }
        }
        if (position == -1) {
            position = getItemCount();
        }

        mCursor = cursor;
        notifyItemInserted(position);
    }

    public boolean isPendingRemoval(int position) {
        mCursor.moveToPosition(position);
        String item = mCursor.getString(mCursor.getColumnIndex(COLUMN_ID));
        return itemsPendingRemoval.contains(item);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        String id;
        TextView person_name, person_birthday, undoButton;
        ImageView double_ok;
        LinearLayout card_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            person_name = itemView.findViewById(R.id.person_name);
            person_birthday = itemView.findViewById(R.id.person_birthday);
            undoButton = itemView.findViewById(R.id.undo);
            card_item = itemView.findViewById(R.id.card_item);
            double_ok = itemView.findViewById(R.id.double_ok);
        }
    }
}