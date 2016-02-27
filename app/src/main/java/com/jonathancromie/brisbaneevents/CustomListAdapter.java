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
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jonathancromie on 20/02/2016.
 */
public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.DataObjectHolder> {
    private List<RSSItem> mDataset;
    private List<RSSItem> visibleObjects;

    public CustomListAdapter(List<RSSItem> myDataset) {
        this.mDataset = myDataset;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textLink;
        TextView textTitle;
        TextView textAddress;
        TextView textDate;

        public DataObjectHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textLink = (TextView) itemView.findViewById(R.id.page_url);
            textTitle = (TextView) itemView.findViewById(R.id.title);
            textDate = (TextView) itemView.findViewById(R.id.date);
            textAddress = (TextView) itemView.findViewById(R.id.address);
        }

        public void bind(RSSItem model) {
            Context context = imageView.getContext();
            Picasso.with(context).load(model.getImage()).resize(75, 75).placeholder(R.drawable.ic_photo_camera_black_24dp).error(R.drawable.ic_photo_camera_black_24dp).into(imageView);
            textLink.setText(model.getLink());
            textTitle.setText(model.getTitle());
            textAddress.setText(model.getAddress());
            textDate.setText(model.getDate());
        }
    }



    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_item_list_row, parent, false);
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final RSSItem model = mDataset.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void setFilter(List<RSSItem> items) {
        mDataset = new ArrayList<>();
        mDataset.addAll(items);
        notifyDataSetChanged();
    }

    public void sort(Comparator<? super RSSItem> comparator) {
        Collections.sort(mDataset, comparator);
        notifyItemRangeChanged(0, getItemCount());
    }
}
