package com.jonathancromie.brisbaneevents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class HighScoresAdapter extends RecyclerView.Adapter<HighScoresAdapter.ViewHolder> {

    private ArrayList<Scores> mDataset;
    private static Context sContext;

    // Adapter's Constructor
    public HighScoresAdapter(Context context, ArrayList<Scores> myDataset) {
        mDataset = myDataset;
        sContext = context;
    }

    // Create new views. This is invoked by the layout manager.
    @Override
    public HighScoresAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // Create a new view by inflating the row item xml.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_score, parent, false);

        // Set the view to the ViewHolder
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    // Replace the contents of a view. This is invoked by the layout manager.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Scores s = mDataset.get(position);

        holder.tt.setText("Name: ");
        holder.ttd.setText(s.getPlayerName().toString());

        holder.mt.setText("Score: ");
        holder.mtd.setText(String.valueOf(s.getScore()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Create the ViewHolder class to keep references to your views
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tt;
        public TextView ttd;
        public TextView mt;
        public TextView mtd;

        //public TextView text_setting;
        //public Switch switch_setting;

//        public TextView mNumberRowTextView;
//        public TextView mNameTextView;

        /**
         * Constructor
         * @param v The container view which holds the elements from the row item xml
         */
        public ViewHolder(View v) {
            super(v);

            tt = (TextView) v.findViewById(R.id.toptext);
            ttd = (TextView) v.findViewById(R.id.toptextdata);
            mt = (TextView) v.findViewById(R.id.middletext);
            mtd = (TextView) v.findViewById(R.id.middletextdata);



//            text_setting = (TextView) v.findViewById(R.id.text_setting);
//            switch_setting = (Switch) v.findViewById(R.id.switch_setting);

//            mNumberRowTextView = (TextView) v.findViewById(R.id.rowNumberTextView);
//            mNameTextView = (TextView) v.findViewById(R.id.nameTextView);
        }
    }
}
