package com.jonathancromie.brisbaneevents;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jonathan on 21-Feb-16.
 */
public class CustomDrawerAdapter extends RecyclerView.Adapter<CustomDrawerAdapter.ViewHolder> implements View.OnClickListener {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context mContext;

    private static String[] titles;
    private TypedArray icons;

    private String name;
    private int profile;
    String email;

    public static int selectedItem = 0;

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        int holderId;
        TextView textView;
        ImageView icon;
        ImageView profile;
        TextView name;
        TextView email;

        public ViewHolder(View itemView, int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);
            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created



            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                icon = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                holderId = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else {
                name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                email = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.profile);// Creating Image view object from header.xml for profile pic
                holderId = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }
    }

    CustomDrawerAdapter(String titles[], TypedArray icons, String name, String email, int profile, Context context){
        this.titles = titles;
        this.icons = icons;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row,parent,false);

            ViewHolder itemHolder = new ViewHolder(v,viewType);

            return itemHolder;

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false);

            ViewHolder headerHolder = new ViewHolder(v,viewType);

            return headerHolder;


        }
        return null;
    }

    @Override
    public void onBindViewHolder(CustomDrawerAdapter.ViewHolder holder, int position) {
        if(holder.holderId ==1) {
            holder.textView.setText(titles[position-1]);
            holder.icon.setImageResource(icons.getResourceId(position-1, -1));

            int colorPrimary = mContext.getResources().getColor(R.color.colorPrimary);
            int colorPrimaryText = mContext.getResources().getColor(R.color.colorPrimaryText);
            int colorSecondaryText = mContext.getResources().getColor(R.color.colorSecondaryText);

            if (position == selectedItem) {
                holder.textView.setTextColor(colorPrimary);
                holder.icon.setColorFilter(colorPrimary);
            }

            else {
                holder.textView.setTextColor(colorPrimaryText);
//                holder.icon.setColorFilter(Color.rgb(127,127,127));
                holder.icon.setColorFilter(colorSecondaryText);
            }
        }
        else{

            holder.profile.setImageResource(profile);
            holder.name.setText(name);
            holder.email.setText(email);
            holder.email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] TO = {"joncromie@gmail.com"};
                    Intent in = new Intent(Intent.ACTION_SEND);
                    in.setData(Uri.parse("mailto:"));
                    in.setType("text/plain");
                    in.putExtra(Intent.EXTRA_EMAIL, TO);
                    in.putExtra(Intent.EXTRA_SUBJECT, "Events in Brisbane");
                    in.putExtra(Intent.EXTRA_TEXT, "Hi Jonathan, ");
                    try {
                        v.getContext().startActivity(Intent.createChooser(in, "Send email"));
                    }
                    catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(v.getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return titles.length+1;
    }

    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
