package com.jonathancromie.brisbaneevents;

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

import java.util.List;

/**
 * Created by jonathancromie on 20/02/2016.
 */
public class CustomListAdapter extends RecyclerView.Adapter<CustomListAdapter.DataObjectHolder> {
    private List<RSSItem> mDataset;
//    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
//        HashMap<String, String> data;
        ImageView imageView;
        TextView textLink;
        TextView textTitle;
        TextView textAddress;
        TextView textDate;
        TextView textBooking;
        Button explore;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textLink = (TextView) itemView.findViewById(R.id.page_url);
            textTitle = (TextView) itemView.findViewById(R.id.title);
            textAddress = (TextView) itemView.findViewById(R.id.address);
            textDate = (TextView) itemView.findViewById(R.id.date);
            textBooking = (TextView) itemView.findViewById(R.id.booking);
            explore = (Button) itemView.findViewById(R.id.explore);
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
    public void onBindViewHolder(final DataObjectHolder holder, int position) {
        Context context = holder.imageView.getContext();

        Picasso.with(context).load(mDataset.get(position).getImage()).resize(75, 75).placeholder(R.drawable.ic_photo_camera_black_24dp).error(R.drawable.ic_photo_camera_black_24dp).into(holder.imageView);
        holder.textLink.setText(mDataset.get(position).getLink());
        holder.textTitle.setText(mDataset.get(position).getTitle());
        holder.textAddress.setText(mDataset.get(position).getAddress());
        holder.textDate.setText(mDataset.get(position).getDate());
        holder.textBooking.setText(mDataset.get(position).getBooking());
        holder.explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = holder.textLink.getText().toString();
                Intent in = new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse(url));
                v.getContext().startActivity(in);
//                explore(v);

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
