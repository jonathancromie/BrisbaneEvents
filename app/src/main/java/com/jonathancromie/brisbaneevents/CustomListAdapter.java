package com.jonathancromie.brisbaneevents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jonathancromie on 20/02/2016.
 */
public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.DataObjectHolder> {
    private List<RSSItem> mDataset;

    public CustomListAdapter(List<RSSItem> myDataset) {
        mDataset = myDataset;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textLink;
        TextView textTitle;
        TextView textAddress;
        TextView textDate;

        public DataObjectHolder(View itemView) {
            super(itemView);

//            itemView.setClickable(true);
//            itemView.setOnClickListener(this);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textLink = (TextView) itemView.findViewById(R.id.page_url);
            textTitle = (TextView) itemView.findViewById(R.id.title);
            textDate = (TextView) itemView.findViewById(R.id.date);
            textAddress = (TextView) itemView.findViewById(R.id.address);
        }
    }



    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_item_list_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

//    String link;
//    String title;
//    String address;
//    String date;
//    String booking;
//    String image;
//    String cost;
//    String meeting_point;
//    String requirements;
//    String description;
//    String time_start;
//    String time_end;

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        Context context = holder.imageView.getContext();

        Picasso.with(context).load(mDataset.get(position).getImage()).resize(75, 75).placeholder(R.drawable.ic_photo_camera_black_24dp).error(R.drawable.ic_photo_camera_black_24dp).into(holder.imageView);
        holder.textLink.setText(mDataset.get(position).getLink());
        holder.textTitle.setText(mDataset.get(position).getTitle());
        holder.textAddress.setText(mDataset.get(position).getAddress());
        holder.textDate.setText(mDataset.get(position).getDate());

//        link = mDataset.get(position).getLink();
//        title = mDataset.get(position).getTitle();
//        address = mDataset.get(position).getAddress();
//        date = mDataset.get(position).getDate();
//        booking = mDataset.get(position).getBooking();
//        image = mDataset.get(position).getImage();
//        cost = mDataset.get(position).getCost();
//        meeting_point = mDataset.get(position).getMeetingPoint();
//        requirements = mDataset.get(position).getRequirements();
//        description = mDataset.get(position).getDescription();
//        time_start = mDataset.get(position).getTimeStart();
//        time_end = mDataset.get(position).getTimeEnd();
    }

    public void addItem(RSSItem dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
