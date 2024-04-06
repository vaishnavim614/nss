package com.test.nss.ui.leader;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.Helper;
import com.test.nss.R;

import java.util.Collections;
import java.util.List;

import static com.test.nss.Helper.blackGrey;

public class MyListAdapterVolunteer extends RecyclerView.Adapter<MyListAdapterVolunteer.ViewHolder> {
    //private AdapterDataMain[] listdata;

    List<AdapterDataVolunteer> list = Collections.emptyList();
    Context mCon;

    public MyListAdapterVolunteer(List<AdapterDataVolunteer> list, Context mCon) {
        this.list = list;
        this.mCon = mCon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_view_vol, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.dateVol.setText(list.get(position).getDate());
        holder.actVol.setText(list.get(position).getAct());
        holder.hoursVol.setText(list.get(position).getHours());
        holder.actIDVol.setText(list.get(position).getId());
        holder.actVolState.setText(list.get(position).getState());

        switch (list.get(position).getState()) {
            case "Approved":
                holder.approvedVol.setText(mCon.getString(R.string.yes));
                setColor(holder, Helper.green);
                break;
            case "PoDelete":
            case "LeaderDelete":
            case "Deleted":
                holder.approvedVol.setText(mCon.getString(R.string.no));
                setColor(holder, Helper.red);
                break;
            case "PoModified":
            case "LeaderModified":
            case "Modified":
                holder.approvedVol.setText(mCon.getString(R.string.yes));
                setColor(holder, Helper.kesar);
                break;
            case "Submitted":
            default:
                setColor(holder, Helper.white);
                holder.approvedVol.setText(mCon.getString(R.string.no));
                holder.dateVol.setTextColor(blackGrey);
                holder.actVol.setTextColor(blackGrey);
                holder.hoursVol.setTextColor(blackGrey);
                holder.approvedVol.setTextColor(blackGrey);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void setColor(ViewHolder holder, int color) {
        holder.dateVol.setTypeface(holder.dateVol.getTypeface(), Typeface.BOLD);
        holder.actVol.setTypeface(holder.actVol.getTypeface(), Typeface.BOLD);
        holder.hoursVol.setTypeface(holder.hoursVol.getTypeface(), Typeface.BOLD);
        holder.approvedVol.setTypeface(holder.approvedVol.getTypeface(), Typeface.BOLD);

        holder.dateVol.setTextColor(color);
        holder.actVol.setTextColor(color);
        holder.hoursVol.setTextColor(color);
        holder.approvedVol.setTextColor(color);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateVol;
        public TextView actVol;
        public TextView hoursVol;
        public TextView actIDVol;
        public TextView approvedVol;
        public TextView actVolState;

        public LinearLayout linearLayoutVol;

        public ViewHolder(View itemView) {
            super(itemView);
            this.dateVol = itemView.findViewById(R.id.dateVol);
            this.actVol = itemView.findViewById(R.id.actVol);
            this.hoursVol = itemView.findViewById(R.id.hoursVol);
            this.actIDVol = itemView.findViewById(R.id.actIDVol);
            this.approvedVol = itemView.findViewById(R.id.approvedVol);
            this.actVolState = itemView.findViewById(R.id.actVolState);

            linearLayoutVol = itemView.findViewById(R.id.dataVolLinear);
        }
    }
}