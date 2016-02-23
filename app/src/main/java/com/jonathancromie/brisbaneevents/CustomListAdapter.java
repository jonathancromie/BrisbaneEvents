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
import android.widget.TextView;

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

//    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textLink;
        TextView textTitle;
        TextView textAddress;
        TextView textDate;
        Button explore;
        Button share;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textLink = (TextView) itemView.findViewById(R.id.page_url);
            textTitle = (TextView) itemView.findViewById(R.id.title);
            textDate = (TextView) itemView.findViewById(R.id.date);
            textAddress = (TextView) itemView.findViewById(R.id.address);
            explore = (Button) itemView.findViewById(R.id.explore);
            share = (Button) itemView.findViewById(R.id.share);
        }
    }

    public CustomListAdapter(List<RSSItem> myDataset) {
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
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        Context context = holder.imageView.getContext();

        Picasso.with(context).load(mDataset.get(position).getImage()).resize(75, 75).placeholder(R.drawable.ic_photo_camera_black_24dp).error(R.drawable.ic_photo_camera_black_24dp).into(holder.imageView);
        holder.textLink.setText(mDataset.get(position).getLink());
        holder.textTitle.setText(mDataset.get(position).getTitle());
        holder.textDate.setText(mDataset.get(position).getDate());
        holder.textAddress.setText(mDataset.get(position).getAddress());
//        holder.textBooking.setText(mDataset.get(position).getBooking());
        holder.explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = mDataset.get(position).getLink();
                String title = mDataset.get(position).getTitle();
                String address = mDataset.get(position).getAddress();
                String date = mDataset.get(position).getDate();
                String booking = mDataset.get(position).getBooking();
                String image = mDataset.get(position).getImage();
                String cost = mDataset.get(position).getCost();
                String meeting_point = mDataset.get(position).getMeetingPoint();
                String requirements = mDataset.get(position).getRequirements();
                String description = mDataset.get(position).getDescription();
                String time_start = mDataset.get(position).getTimeStart();
                String time_end = mDataset.get(position).getTimeEnd();

                Intent in = new Intent(v.getContext(), ExploreActivity.class);
                in.putExtra("link", link);
                in.putExtra("title", title);
                in.putExtra("address", address);
                in.putExtra("date", date);
                in.putExtra("booking", booking);
                in.putExtra("image", image);
                in.putExtra("cost", cost);
                in.putExtra("meeting_point", meeting_point);
                in.putExtra("requirements", requirements);
                in.putExtra("description", description);
                in.putExtra("time_start", time_start);
                in.putExtra("time_end", time_end);

                v.getContext().startActivity(in);

            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = mDataset.get(position).getLink();
                String title = mDataset.get(position).getTitle();
                String address = mDataset.get(position).getAddress();
                String date = mDataset.get(position).getDate();
                String booking = mDataset.get(position).getBooking();
                String image = mDataset.get(position).getImage();
                String cost = mDataset.get(position).getCost();
                String meeting_point = mDataset.get(position).getMeetingPoint();
                String requirements = mDataset.get(position).getRequirements();
                String description = mDataset.get(position).getDescription();

                Intent in = new Intent();
                in.setAction(Intent.ACTION_SEND);
                in.putExtra(Intent.EXTRA_TEXT, title + "\n\n" +
                                    description + "\n" +
                                    date + "\n" +
                                    address + "\n" +
                                    cost + "\n" +
                                    "Meeting point: " + meeting_point + "\n" +
                                    "Requirements: " + requirements + "\n" +
                                    "Booking: " + booking);
                in.setType("text/plain");
                v.getContext().startActivity(in);
            }
        });
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
