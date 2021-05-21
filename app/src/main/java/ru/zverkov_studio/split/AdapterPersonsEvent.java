package ru.zverkov_studio.split;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterPersonsEvent extends RecyclerView.Adapter<AdapterPersonsEvent.ViewHolder> {

    DataBasePersons persons;
    DataBaseEvents events;
    private static final String TABLE_DECLARED = "declared_table";
    ContentValues cv = new ContentValues();

    ArrayList<Stopwatch> stopwatches = new ArrayList<>();

    private static final int PENDING_REMOVAL_TIMEOUT = 1000; // 3sec

    List<String> track;
    int point = 0;
    private Context mContext;
    String item;
    Cursor mCursor;
    boolean undoOn = true;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public AdapterPersonsEvent(Context context){
        mContext = context;
        open_DB();
        mCursor = persons.getAllData(TABLE_DECLARED, null);
        track = events.get_track();
        for (int i = 0; i < getItemCount(); i++) {
            stopwatches.add(new Stopwatch());
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        item = mCursor.getString(mCursor.getColumnIndex(DataBasePersons.COLUMN_ID));

        holder.id = item;
        holder.number.setText(String.valueOf(position + 1));
        holder.sportsman_name.setText(mCursor.getString(mCursor.getColumnIndex(DataBasePersons.COLUMN_NAME)));
        holder.main_time.setText(stopwatches.get(position).timer);
        holder.lap_time.setText(stopwatches.get(position).lap_timer);
        holder.distance_point.setText("");
        holder.button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatches.get(position).start();
                holder.main_time.setText(stopwatches.get(position).timer);
                holder.lap_time.setText(stopwatches.get(position).lap_timer);
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
        String id;
        TextView sportsman_name, main_time, lap_time, distance_point, number;
        LinearLayout button_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sportsman_name = itemView.findViewById(R.id.sportsman_name);
            main_time = itemView.findViewById(R.id.main_time);
            lap_time = itemView.findViewById(R.id.lap_time);
            distance_point = itemView.findViewById(R.id.distance_point);
            number = itemView.findViewById(R.id.number);
            button_time = itemView.findViewById(R.id.catch_time);
        }
    }

    public void open_DB(){
        persons = new DataBasePersons(mContext);
        persons.open();
        events = new DataBaseEvents(mContext);
        events.open();
    }

    public class updater {
        ViewHolder mHolder;
        Handler handler = new Handler();
        public updater (ViewHolder holder) {
            
        }
    }
}