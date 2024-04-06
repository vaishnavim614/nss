package com.test.nss.ui.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AdapterModel extends RecyclerView.Adapter<AdapterModel.ViewHolder> {
    //private AdapterDataAct[] listdata;
    List<MyListAdapterModel> list = Collections.emptyList();
    Context mCon;

    public AdapterModel(List<MyListAdapterModel> list, Context mCon) {
        this.list = list;
        this.mCon = mCon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_model, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imgView.setText(list.get(position).getImg());
        holder.textData.setText(list.get(position).getText());
        holder.label.setText(String.format(Locale.ENGLISH, "Page: %d", position + 1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textData, label, imgView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textData = itemView.findViewById(R.id.text_data);
            this.imgView = itemView.findViewById(R.id.image_view);
            this.label = itemView.findViewById(R.id.label);
        }
    }
}
