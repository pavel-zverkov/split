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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterTrack extends RecyclerView.Adapter<AdapterTrack.ViewHolder> {

    private static final int PENDING_REMOVAL_TIMEOUT = 1000; // 3sec

    private Context mContext;
    DataBaseEvents events;
    DataBaseEvents persons;
    private static final String TABLE_TRACK = "track_points_table";
    int mSport;
    int icon_sport;

    List<String> itemsPendingRemoval;
    List<String> track;
    ArrayList<Integer> expandable = new ArrayList<>();

    HashMap<Integer, long[]> times;

    boolean undoOn = true;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public AdapterTrack(Context context){
        mContext = context;
        itemsPendingRemoval = new ArrayList<>();

        open_DB();
        track = events.get_track();
        mSport = mContext.getResources().getIdentifier(events.get_event_data()[2], "drawable", mContext.getPackageName());
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.track_point.setText(track.get(position));
        holder.side_highlight.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.dark_green)));
        holder.sport.setImageResource(mSport);
        if (track.get(position) == "СТАРТ"){
            holder.side_highlight.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.dark_red)));
        }
        if (track.get(position) == "ФИНИШ"){
            holder.side_highlight.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.asbestos)));
        }
        if (expandable.contains(position)){
            holder.sub_item.setVisibility(View.VISIBLE);
            AdapterTrackInside adapter = new AdapterTrackInside(mContext, position);
            holder.list_inside_point.setLayoutManager(new LinearLayoutManager(mContext));
            holder.list_inside_point.setAdapter(adapter);
        }
        else{
            holder.sub_item.setVisibility(View.GONE);
        }
        if (mContext != ActivityCreate.CONTEXT){
            holder.card_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!expandable.contains(position)){
                        expandable.add(position);
                        notifyItemChanged(position);
                    }
                    else{
                        int i = 0;
                        for (i = 0; i < expandable.size(); i++){
                            if (expandable.get(i) == position){
                                expandable.remove(i);
                                notifyItemChanged(position);
                                break;
                            }
                        }
                    }

                }
            });
        }


    }

    public void add(String string){
        events.add_point(track.size() - 1, string + " " + String.valueOf(track.size() - 1));
        notifyItemInserted(getItemCount() - 2);
    }

    public void remove(int position) {

        events.remove_point(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return track.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        String id;
        TextView side_highlight, track_point;
        ImageView sport;
        LinearLayout sub_item;
        RecyclerView list_inside_point;
        CoordinatorLayout card_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            side_highlight = itemView.findViewById(R.id.side_highlight);
            track_point = itemView.findViewById(R.id.track_point);
            sport = itemView.findViewById(R.id.sport);
            sub_item = itemView.findViewById(R.id.sub_item_track);
            list_inside_point = itemView.findViewById(R.id.list_inside_point);
            card_item = itemView.findViewById(R.id.point_card);
        }
    }

    public void open_DB(){
        events = new DataBaseEvents(mContext);
        events.open();
    }
}