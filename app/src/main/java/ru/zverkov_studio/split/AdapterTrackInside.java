package ru.zverkov_studio.split;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdapterTrackInside extends RecyclerView.Adapter<AdapterTrackInside.ViewHolder> {

    private static final int PENDING_REMOVAL_TIMEOUT = 1000; // 3sec
    private static boolean mStatus;
    private static int mPosition;

    private Context mContext;
    DataBasePersons persons;
    private static final String TABLE_TRACK = "track_points_table";
    int mSport;
    int icon_sport;
    long MillisecondTime_main, UpdateTime = 0L ;
    int Hours, Seconds, Minutes, MilliSeconds ;


    List<String> itemsPendingRemoval;
    ArrayList<Integer> expandable = new ArrayList<>();

    HashMap<String, Long> data;
    Object[] names;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public AdapterTrackInside(Context context, int position, boolean status){
        mContext = context;
        mStatus = status;
        mPosition = position;
        open_DB();

        data = sortByValue(persons.get_times(position));
        Log.d("AdapterInside", String.valueOf(data));
        names = data.keySet().toArray();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inside_point_track, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(String.valueOf(names[position]));
        if (mPosition == 0){
            if (position == 0){
                holder.time.setText(String.valueOf(set_time(data.get(names[position]) - data.get(names[0]))));
            }
            else {
                holder.time.setText("+" + String.valueOf(set_time(data.get(names[position]) - data.get(names[0]))));
            }
        }
        else {
            if (mStatus == true){
                    holder.time.setText(String.valueOf(set_time(data.get(names[position]) - data.get(names[0]))));
                }
            else{
                holder.time.setText(String.valueOf(set_time(data.get(names[position]))));
            }
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_person_name);
            time = itemView.findViewById(R.id.list_person_time);
        }
    }

    public String set_time(long time){
        MillisecondTime_main = time;
        UpdateTime = MillisecondTime_main;
        Seconds = (int) (UpdateTime / 1000);
        Minutes = Seconds / 60;
        Hours = Minutes / 60;
        Seconds = Seconds % 60;
        MilliSeconds = (int) (UpdateTime % 1000 / 100);
        return (String.format("%02d", Hours) + ":"
                + String.format("%02d", Minutes) + ":"
                + String.format("%02d", Seconds) + "."
                + String.format("%d", MilliSeconds));
    }

    public void open_DB(){
        persons = new DataBasePersons(mContext);
        persons.open();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static HashMap<String, Long> sortByValue(HashMap<String, Long> unsortedMap)
    {
        HashMap<String, Long> sortedMap =
                unsortedMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));
        for (String name: sortedMap.keySet()){
            Log.d("Sort", name + " " + sortedMap.get(name));
        }
        return sortedMap;
    }

}