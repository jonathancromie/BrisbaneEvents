package com.jonathancromie.brisbaneevents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jonathancromie on 20/02/2016.
 */
public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.ViewHolder> {
    private List<RSSItem> mDataset;
    private List<RSSItem> visibleObjects;
    private Context mContext;

    public CustomListAdapter(List<RSSItem> myDataset, Context context) {
        this.mDataset = myDataset;
        this.mContext = context;
    }

    @Override
    public CustomListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_item_list_row,parent,false);

        ViewHolder itemHolder = new ViewHolder(v,viewType);

        return itemHolder;
    }

    @Override
    public void onBindViewHolder(final CustomListAdapter.ViewHolder holder, final int position) {
        Picasso.with(mContext).load(mDataset.get(position).getImage()).resize(75, 75).placeholder(R.drawable.ic_photo_camera_black_24dp).error(R.drawable.ic_photo_camera_black_24dp).into(holder.eventImage);
        holder.textLink.setText(mDataset.get(position).getLink());
        holder.textTitle.setText(mDataset.get(position).getTitle());
        holder.textAddress.setText(mDataset.get(position).getAddress());
        holder.textDate.setText(mDataset.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView textLink;
        TextView textTitle;
        TextView textAddress;
        TextView textDate;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);

            eventImage = (ImageView) itemView.findViewById(R.id.eventImage);
            textLink = (TextView) itemView.findViewById(R.id.page_url);
            textTitle = (TextView) itemView.findViewById(R.id.title);
            textDate = (TextView) itemView.findViewById(R.id.date);
            textAddress = (TextView) itemView.findViewById(R.id.address);
        }
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
