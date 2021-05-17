package ru.zverkov_studio.split;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterTrack extends RecyclerView.Adapter<AdapterTrack.ViewHolder> {

    private static final int PENDING_REMOVAL_TIMEOUT = 1000; // 3sec

    private Context mContext;
    int mSport;
    List<String> itemsPendingRemoval, content;
    boolean undoOn = true;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public AdapterTrack(Context context, List<String> stringList, int kind_sport){
        mContext = context;
        itemsPendingRemoval = new ArrayList<>();
        content = stringList;
        mSport = kind_sport;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String item = content.get(position);

        holder.track_point.setText(item);
        holder.sport.setImageResource(mSport);

    }

    public void add(String string){
        content.add(content.size() - 1, string);
        notifyItemInserted(getItemCount() - 2);
    }

    @Override
    public int getItemCount() {
        return content.size();
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
}