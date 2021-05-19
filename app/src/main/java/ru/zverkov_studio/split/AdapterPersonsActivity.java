package ru.zverkov_studio.split;

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

public class AdapterPersonsActivity {//extends RecyclerView.Adapter<AdapterPersonsActivity.ViewHolder> {

    /*private static final int PENDING_REMOVAL_TIMEOUT = 1000; // 3sec

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_QUALIFY = "qualify";
    public static final String COLUMN_GENDER = "gender";

    private Context mContext;
    String item;
    String[] row = new String[5];
    ArrayList mData = new ArrayList();
    boolean undoOn = true;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public AdapterPersonsActivity(Context context){
        mContext = context;
        for(int i = 0; i < data.get_data().size(); i++){
            mData.add((String[]) data.get_data().get(i));
        }
        activity_data = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        row = (String[]) mData.get(position);
        item = row[0];

        holder.point = 0;
        holder.length = 2;

        holder.id = item;
        holder.number.setText(String.valueOf(position + 1));
        holder.sportsman_name.setText(row[1]);
        holder.main_time.setText("Старт");
        holder.lap_time.setText("");
        holder.distance_point.setText("");
        holder.button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stopwatch stopwatch = new Stopwatch(holder.main_time, holder.lap_time);
            }
        });
    }

    public void change(Cursor cursor){
        notifyDataSetChanged();
    }

    public void remove(int position) {
        activity_data.remove_from_activity((String[]) mData.get(position));
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        String id;
        TextView sportsman_name, main_time, lap_time, distance_point, number;
        LinearLayout button_time;
        int point, length;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sportsman_name = itemView.findViewById(R.id.sportsman_name);
            main_time = itemView.findViewById(R.id.main_time);
            lap_time = itemView.findViewById(R.id.lap_time);
            distance_point = itemView.findViewById(R.id.distance_point);
            number = itemView.findViewById(R.id.number);
            button_time = itemView.findViewById(R.id.catch_time);
        }
    }*/
}