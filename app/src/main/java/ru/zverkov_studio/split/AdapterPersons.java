package ru.zverkov_studio.split;

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

    private static final int PENDING_REMOVAL_TIMEOUT = 1000; // 3sec

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_QUALIFY = "qualify";
    public static final String COLUMN_GENDER = "gender";

    private Context mContext;
    String item;
    String[] row = new String[5];
    ArrayList mData = new ArrayList();
    ProxyList activity_data;
    boolean undoOn = true;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public AdapterPersons(Context context, ProxyList data){
        mContext = context;
        for(int i = 0; i < data.get_data().size(); i++){
            mData.add((String[]) data.get_data().get(i));
        }
        activity_data = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        row = (String[]) mData.get(position);
        item = row[0];

        holder.id = item;
        holder.person_name.setText(row[1]);
        holder.person_gender.setText(row[0]);
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
        TextView person_name, person_gender, number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            person_name = itemView.findViewById(R.id.person_activity_name);
            person_gender = itemView.findViewById(R.id.person_activity_gender);
        }
    }
}