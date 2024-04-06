package com.test.nss.ui.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;
import com.test.nss.ui.onClickInterface2;

import java.util.Collections;
import java.util.List;

public class MyListAdapterAct extends RecyclerView.Adapter<MyListAdapterAct.ViewHolder> {
    //private AdapterDataAct[] listdata;
    List<AdapterDataAct> list = Collections.emptyList();
    onClickInterface2 onClickInterface2;
    Context mCon;

    public MyListAdapterAct(List<AdapterDataAct> list, Context mCon, onClickInterface2 onClickInterface2) {
        this.list = list;
        this.mCon = mCon;
        this.onClickInterface2 = onClickInterface2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_view_act, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.act.setText(list.get(position).getAct());
        holder.date.setText(list.get(position).getHours());
        holder.endDate.setText(list.get(position).getEndDate());
        holder.actDataCard.setOnLongClickListener(view -> {
            onClickInterface2.setClick(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void insert(int position, AdapterDataAct data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(AdapterDataAct data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView act;
        public TextView endDate;
        public CardView actDataCard;

        public ViewHolder(View itemView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.date);
            this.act = itemView.findViewById(R.id.act);
            this.endDate = itemView.findViewById(R.id.endDate);

            actDataCard = itemView.findViewById(R.id.actDataCard);
        }
    }
}
