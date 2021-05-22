package ru.zverkov_studio.split;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterTrack extends RecyclerView.Adapter<AdapterTrack.ViewHolder> {

    private static final int PENDING_REMOVAL_TIMEOUT = 1000; // 3sec

    private Context mContext;
    DataBaseEvents events;
    private static final String TABLE_TRACK = "track_points_table";
    int mSport;
    int icon_sport;

    List<String> itemsPendingRemoval;
    List<String> track;
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.track_point.setText(track.get(position));
        holder.sport.setImageResource(mSport);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            side_highlight = itemView.findViewById(R.id.side_highlight);
            track_point = itemView.findViewById(R.id.track_point);
            sport = itemView.findViewById(R.id.sport);
        }
    }

    public void open_DB(){
        events = new DataBaseEvents(mContext);
        events.open();
    }
}