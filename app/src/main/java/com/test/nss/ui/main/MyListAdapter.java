package com.test.nss.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;
import com.test.nss.ui.onClickInterface2;

import java.util.Collections;
import java.util.List;

import static com.test.nss.Helper.blackishy;
import static com.test.nss.Helper.green;
import static com.test.nss.Helper.kesar;
import static com.test.nss.Helper.primaryColDark;
import static com.test.nss.Helper.red;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    //private AdapterDataMain[] listdata;

    List<AdapterDataMain> list = Collections.emptyList();
    Context mCon;
    onClickInterface2 onClickInterface;

    public MyListAdapter(List<AdapterDataMain> list, Context mCon, onClickInterface2 onClickInterface) {
        this.list = list;
        this.mCon = mCon;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_view_main, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.date.setText(list.get(position).getDate());
        holder.act.setText(list.get(position).getAct());
        //holder.state.setText(String.format("• %s", list.get(position).getState()));
        holder.hours.setText(String.format("%sh", list.get(position).getHours()));
        holder.actId.setText(list.get(position).getId());

        switch (list.get(position).getState()) {
            default:
                holder.date.setTextColor(blackishy);
                holder.hours.setTextColor(primaryColDark);
                holder.linearLayout.setBackground(ContextCompat.getDrawable(mCon, R.drawable.ic_circle));
                break;
            case "LeaderDelete":
            case "PoDelete":
            case "Finished":
                setColor(holder, red);
                holder.linearLayout.setBackground(ContextCompat.getDrawable(mCon, R.drawable.ic_circle_del));
                break;
            case "LeaderModified":
            case "PoModified":
                setColor(holder, kesar);
                holder.linearLayout.setBackground(ContextCompat.getDrawable(mCon, R.drawable.ic_circle_mod));
                break;
            case "Approved":
                setColor(holder, green);
                holder.linearLayout.setBackground(ContextCompat.getDrawable(mCon, R.drawable.ic_circle_app));
                break;
        }

        holder.mainLinear.setOnClickListener(view -> {
            onClickInterface.setClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void insert(int position, AdapterDataMain data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    private void setColor(ViewHolder holder, int color) {
        //holder.date.setTypeface(holder.date.getTypeface(), Typeface.BOLD);
        //holder.act.setTypeface(holder.act.getTypeface(), Typeface.BOLD);
        //holder.hours.setTypeface(holder.hours.getTypeface(), Typeface.BOLD);

        holder.date.setTextColor(color);
        holder.hours.setTextColor(Color.WHITE);

        //bg.setTint(R.color.black);
        //holder.act.setTextColor(color);
        //holder.hours.setTextColor(color);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(AdapterDataMain data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView act;
        public TextView hours;
        public TextView actId;
        public TextView state;

        public CardView cardView;
        public LinearLayout linearLayout, mainLinear;

        public ViewHolder(View itemView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.date);
            this.act = itemView.findViewById(R.id.act);
            this.hours = itemView.findViewById(R.id.hours);
            this.actId = itemView.findViewById(R.id.actID);
            this.state = itemView.findViewById(R.id.state);


            this.cardView = itemView.findViewById(R.id.dataCard);
            this.linearLayout = itemView.findViewById(R.id.imageLinear);
            this.mainLinear = itemView.findViewById(R.id.mainLinear);
        }
    }
}