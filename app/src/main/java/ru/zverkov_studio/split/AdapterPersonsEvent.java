package ru.zverkov_studio.split;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class AdapterPersonsEvent extends RecyclerView.Adapter<AdapterPersonsEvent.ViewHolder> {

    DataBasePersons persons;
    DataBaseEvents events;
    private static final String TABLE_DECLARED = "declared_table";
    ContentValues cv = new ContentValues();

    private static final int PENDING_REMOVAL_TIMEOUT = 1000; // 3sec

    List<String> track;
    int point = 0;
    private Context mContext;

    int item;
    Cursor mCursor;

    long[][] times;
    HashMap<Integer, long[]> times_array = new HashMap<>();
    HashMap<Integer, Integer> on_track = new HashMap<>();
    long start_time, start_lap_time;

    ArrayList<Integer> position_click = new ArrayList<>();

    boolean undoOn = true;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public AdapterPersonsEvent(Context context){
        mContext = context;
        open_DB();
        mCursor = persons.getAllData(TABLE_DECLARED, null);
        track = events.get_track();
        times = new long[mCursor.getCount()][track.size()];
        for (int i = 0; i < mCursor.getCount(); i++){
            mCursor.moveToPosition(i);
            item = mCursor.getInt(0);
            times_array.put(item, new long[track.size()]);
            on_track.put(item, 0);

        }
        for (int key: on_track.keySet()){
            Log.d("AdapterEvents", String.valueOf(key) + ": " + String.valueOf(on_track.get(key)));
        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("AdapterEvents", "onCreate");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("AdapterEvents", "onBind  " + String.valueOf(position));
        mCursor.moveToPosition(position);
        item = mCursor.getInt(0);
        Log.d("AdapterEvents", "item  " + String.valueOf(item));

        holder.id = item;
        holder.number.setText(String.valueOf(position + 1));
        holder.sportsman_name.setText(mCursor.getString(mCursor.getColumnIndex(DataBasePersons.COLUMN_NAME)));
        holder.distance_point.setText("");
        if (!position_click.contains(item)){
            holder.stopwatch.stop_setting();
            holder.main_time.setText("СТАРТ");
            holder.lap_time.setText("");
        }
        else {
            //start_time = times[position][0];
            //start_lap_time = times[position][holder.point];
            Log.d("AdapterEvents", "position " + String.valueOf(position));
            Log.d("AdapterEvents", "point " + String.valueOf(on_track.get(item)));
            start_time = times_array.get(item)[0];
            start_lap_time = times_array.get(item)[on_track.get(item) - 1];

            holder.stopwatch.run_setting(holder.main_time, holder.lap_time, start_time, start_lap_time);
        }
        holder.button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCursor.moveToPosition(position);
                item = mCursor.getInt(0);
                Log.d("AdapterEvents", "Click " + String.valueOf(position));
                Log.d("AdapterEvents", "point_before_click " + String.valueOf(on_track.get(item)));
                Log.d("AdapterEvents", "item_click " + String.valueOf(item));
                //times[position][holder.point] = SystemClock.uptimeMillis();
                times_array.get(item)[on_track.get(item)] = SystemClock.uptimeMillis();
                on_track.put(item, on_track.get(item) + 1);

                Log.d("AdapterEvents", "holder_point_after_click " + String.valueOf(on_track.get(item)));
                position_click.add(item);
                notifyItemChanged(position);
                for (int key: on_track.keySet()){
                    Log.d("AdapterEvents", String.valueOf(key) + ": " + String.valueOf(on_track.get(key)));
                }
            }
        });
    }

    public void change(Cursor cursor){
        notifyDataSetChanged();
    }

    public void remove(int position) {
        /*activity_data.remove_from_activity((String[]) mData.get(position));
        mData.remove(position);
        notifyItemRemoved(position);*/
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        int id, point;
        TextView sportsman_name, main_time, lap_time, distance_point, number;
        LinearLayout button_time;
        Stopwatch stopwatch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sportsman_name = itemView.findViewById(R.id.sportsman_name);
            main_time = itemView.findViewById(R.id.main_time);
            lap_time = itemView.findViewById(R.id.lap_time);
            distance_point = itemView.findViewById(R.id.distance_point);
            number = itemView.findViewById(R.id.number);
            button_time = itemView.findViewById(R.id.catch_time);
            stopwatch = new Stopwatch();
            point = 0;
        }
    }

    public void open_DB(){
        persons = new DataBasePersons(mContext);
        persons.open();
        events = new DataBaseEvents(mContext);
        events.open();
    }

}