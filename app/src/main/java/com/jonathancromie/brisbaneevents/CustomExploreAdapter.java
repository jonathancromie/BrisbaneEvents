package com.jonathancromie.brisbaneevents;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathancromie on 27/02/2016.
 */
public class CustomExploreAdapter extends RecyclerView.Adapter<CustomExploreAdapter.ViewHolder> {

    List<Event> events = new ArrayList<Event>();
    Context mContext;

    public CustomExploreAdapter(List<Event> events, Context context) {
        this.events = events;
        this.mContext = context;
    }

    @Override
    public CustomExploreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event,parent,false);

        ViewHolder itemHolder = new ViewHolder(v,viewType);

        return itemHolder;
    }

    @Override
    public void onBindViewHolder(final CustomExploreAdapter.ViewHolder holder, int position) {
        holder.description.setText(events.get(position).getDescription());
        holder.date.setText(events.get(position).getDate());
        holder.address.setText(events.get(position).getAddress());
        holder.cost.setText(events.get(position).getCost());
        holder.note.setText(events.get(position).getNote());

        int primaryColor = mContext.getResources().getColor(R.color.colorPrimary);

        holder.address.setTextColor(primaryColor);
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + holder.address.getText().toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                    v.getContext().startActivity(mapIntent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        TextView date;
        TextView address;
        TextView cost;
        TextView note;


        public ViewHolder(View itemView, int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);
            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
            address = (TextView) itemView.findViewById(R.id.address);
            cost = (TextView) itemView.findViewById(R.id.cost);
            note = (TextView) itemView.findViewById(R.id.note);

        }
    }
}
