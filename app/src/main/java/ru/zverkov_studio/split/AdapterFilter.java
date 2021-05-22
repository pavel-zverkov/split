package ru.zverkov_studio.split;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static ru.zverkov_studio.split.R.color.asphalt;
import static ru.zverkov_studio.split.R.color.blue;

public class AdapterFilter extends RecyclerView.Adapter<AdapterFilter.ViewHolder> {
    private List<CardView> items = new ArrayList<>();
    private String item_choice = "";
    private ArrayList<String> m_qualify = new ArrayList<String>();
    private Context mContext;

    public AdapterFilter(Context Context, @NonNull ArrayList<String> qualify) {
        m_qualify = qualify;
        mContext = Context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("myLog", "onBind" + position);
        holder.filter_item_text.setText(m_qualify.get(position));

        if (!items.contains(holder.filter_item_card)) {
            items.add(holder.filter_item_card);
        }

        holder.filter_item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(CardView cardView : items){
                    cardView.setCardBackgroundColor(mContext.getResources().getColor(asphalt));
                }
                //The selected card is set to colorSelected
                holder.filter_item_card.setCardBackgroundColor(mContext.getResources().getColor(blue));
                item_choice = holder.filter_item_text.getText().toString();
            }
        });
    }

    @Override
    public int getItemCount() {
        return m_qualify.size();
    }

    public String getChoice() {
        return item_choice;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView filter_item_text;
        CardView filter_item_card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            filter_item_text = itemView.findViewById(R.id.filter_item_text);
            filter_item_card = (CardView) itemView.findViewById(R.id.filter_item_card);
            Log.d("myLog", "Set item text");

        }
    }
}
