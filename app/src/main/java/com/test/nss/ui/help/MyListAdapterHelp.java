package com.test.nss.ui.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.test.nss.R;
import com.test.nss.ui.onClickInterface;

import java.util.Collections;
import java.util.List;

public class MyListAdapterHelp extends RecyclerView.Adapter<MyListAdapterHelp.ViewHolder> {
    //private AdapterDataAct[] listdata;
    List<AdapterDataHelp> list = Collections.emptyList();
    Context mCon;
    onClickInterface onClickInterface;
    boolean isShimmer = true;

    public MyListAdapterHelp(List<AdapterDataHelp> list, Context mCon, onClickInterface onClickInterface) {
        this.list = list;
        this.mCon = mCon;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_view_leader_help, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isShimmer) {
            holder.shimmerCon.startShimmer();
        } else {
            holder.leadName.setBackground(null);
            holder.leadEmail.setBackground(null);
            holder.leadCont.setBackground(null);
            holder.clgNameLead.setBackground(null);

            holder.shimmerCon.stopShimmer();
            holder.shimmerCon.setShimmer(null);

            holder.leader.setText("Leader");
            holder.leadName.setText(list.get(position).getLeadName());
            holder.leadEmail.setText(String.format("Email: %s", list.get(position).getLeadEmail()));
            holder.leadCont.setText(String.format("Contact No: %s", list.get(position).getLeadCont()));
            holder.clgNameLead.setText(list.get(position).getLeadClg());
            holder.leadCont.setOnClickListener(view -> onClickInterface.setClick(list.get(position).getLeadCont()));
        }
    }

    @Override
    public int getItemCount() {
        int SHIMMER_NUMBER = list.size();
        return isShimmer ? SHIMMER_NUMBER : list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView leadEmail;
        public TextView leadName;
        public TextView clgNameLead;
        public TextView leadCont;
        public TextView leader;
        public ShimmerFrameLayout shimmerCon;

        public CardView actDataCard;

        public ViewHolder(View itemView) {
            super(itemView);
            this.leadEmail = itemView.findViewById(R.id.lead_email);
            this.leadName = itemView.findViewById(R.id.lead_name);
            this.clgNameLead = itemView.findViewById(R.id.clgNameLead);
            this.leadCont = itemView.findViewById(R.id.lead_contact);
            this.shimmerCon = itemView.findViewById(R.id.shimmer_con);
            this.leader = itemView.findViewById(R.id.leader);

            this.actDataCard = itemView.findViewById(R.id.leaderHelpDataCard);
        }
    }
}
