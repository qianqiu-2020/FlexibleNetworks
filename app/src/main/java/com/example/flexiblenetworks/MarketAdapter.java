package com.example.flexiblenetworks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {
    private List<Titles> mTitles;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleName;

        public ViewHolder(View view) {
            super(view);
            titleName = (TextView) view.findViewById(R.id.title);
        }
    }

    public MarketAdapter(List<Titles> titleList) {
        mTitles = titleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycleview_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Titles title = mTitles.get(position);
        holder.titleName.setText(title.getName());
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }
}
