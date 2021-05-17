package ru.zverkov_studio.split;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterClubCreate extends RecyclerView.Adapter<AdapterClubCreate.ViewHolder> {

    private static final int PENDING_REMOVAL_TIMEOUT = 1000; // 3sec

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_QUALIFY = "qualify";
    public static final String COLUMN_GENDER = "gender";

    private Context mContext;
    String item;
    String[] row = new String[5];
    ArrayList mData;
    ProxyList activity_data;
    boolean undoOn = true;
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    public AdapterClubCreate(Context context, ArrayList data, ProxyList proxy_data){
        mContext = context;
        activity_data = proxy_data;
        mData = data;
        ArrayList new_data = proxy_data.get_data();
        ArrayList<Integer> copy_data = new ArrayList<Integer>();

        for (int i = 0; i < new_data.size(); i++){
            String[] new_row = new String[5];
            String[] old_row = new String[5];
            new_row = (String[]) (new_data.get(i));
            for (int j = 0; j < data.size(); j++){
                old_row = (String[]) mData.get(j);
                if (old_row[0].equals(new_row[0])){
                    Log.d("CREATE", "equals");
                    mData.remove(j);
                    break;
                }
            }
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_club_person, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        row = (String[]) mData.get(position);
        item = row[0];

        holder.id = item;
        holder.person_name.setText(row[1]);
        holder.person_birthday.setText(row[0]);
    }

    public void change(Cursor cursor){
        notifyDataSetChanged();
    }

    public void remove(int position) {
        activity_data.add_to_activity((String[]) mData.get(position));
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        String id;
        TextView person_name, person_birthday;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            person_name = itemView.findViewById(R.id.person_name);
            person_birthday = itemView.findViewById(R.id.person_birthday);
        }
    }
}