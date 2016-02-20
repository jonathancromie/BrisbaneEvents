package com.jonathancromie.brisbaneevents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jonathancromie on 20/02/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.DataObjectHolder> {
    private List<RSSItem> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        HashMap<String, String> data;
        ImageView imageView;
        TextView textLink;
        TextView textTitle;
        TextView textAddress;
        TextView textDate;
        TextView textBooking;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textLink = (TextView) itemView.findViewById(R.id.page_url);
            textTitle = (TextView) itemView.findViewById(R.id.title);
            textAddress = (TextView) itemView.findViewById(R.id.address);
            textDate = (TextView) itemView.findViewById(R.id.date);
            textBooking = (TextView) itemView.findViewById(R.id.booking);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public CustomAdapter(List<RSSItem> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_item_list_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        Context context = holder.imageView.getContext();

        Picasso.with(context).load(mDataset.get(position).getImage()).resize(75, 75).placeholder(R.drawable.ic_photo_camera_black_24dp).error(R.drawable.ic_photo_camera_black_24dp).into(holder.imageView);
        holder.textLink.setText(mDataset.get(position).getLink());
        holder.textTitle.setText(mDataset.get(position).getTitle());
        holder.textAddress.setText(mDataset.get(position).getAddress());
        holder.textDate.setText(mDataset.get(position).getDate());
        holder.textBooking.setText(mDataset.get(position).getBooking());
    }

    public void addItem(RSSItem dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
