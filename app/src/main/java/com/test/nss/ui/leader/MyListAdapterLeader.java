package com.test.nss.ui.leader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.test.nss.R;
import com.test.nss.ui.onClickInterface2;

import java.util.Collections;
import java.util.List;

public class MyListAdapterLeader extends RecyclerView.Adapter<MyListAdapterLeader.ViewHolder> {
    //private AdapterDataAct[] listdata;
    List<AdapterDataLeader> list = Collections.emptyList();
    Context mCon;
    onClickInterface2 onClickInterface;
    boolean isShimmer = false;

    public MyListAdapterLeader(List<AdapterDataLeader> list, Context mCon, onClickInterface2 onClickInterface) {
        this.list = list;
        this.mCon = mCon;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_view_leader, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isShimmer) {
            holder.shimmerLeader.startShimmer();
            holder.shimmerLeader.showShimmer(isShimmer);

        } else {
            holder.shimmerLeader.stopShimmer();
            holder.shimmerLeader.hideShimmer();
            //holder.shimmerLeader.setShimmer(null);

            holder.volunteerVec.setText(String.format("VEC: %s", list.get(position).getvolVec()));
            holder.volunteerName.setText(list.get(position).getVolName());
            holder.actDataCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickInterface.setClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView volunteerVec;
        public TextView volunteerName;

        public LinearLayout actDataCard, linearLeaderShim;
        public ShimmerFrameLayout shimmerLeader;

        public ViewHolder(View itemView) {
            super(itemView);
            this.volunteerVec = itemView.findViewById(R.id.vol_vec);
            this.volunteerName = itemView.findViewById(R.id.vol_name);
            this.linearLeaderShim = itemView.findViewById(R.id.linearLeaderShim);
            this.shimmerLeader = itemView.findViewById(R.id.shimmer_leader);

            this.actDataCard = itemView.findViewById(R.id.cardLinear);
        }
    }
}