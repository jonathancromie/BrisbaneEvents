package com.jonathancromie.brisbaneevents;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by jonathancromie on 3/01/15.
 */
public class BackgroundAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<Integer> mBackgroundIds = new ArrayList<Integer>();

    public BackgroundAdapter(Context c) {

        mContext = c;

        mBackgroundIds.add(R.drawable.mountain);
        mBackgroundIds.add(R.drawable.sea);
        mBackgroundIds.add(R.drawable.sky);
        mBackgroundIds.add(R.drawable.wood);
    }

    @Override
    public int getCount() {

        return mBackgroundIds.size();
    }

    @Override
    public Object getItem(int position) {

        return mBackgroundIds.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(8, 8, 8, 8);
        }

        else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mBackgroundIds.get(position));
        return imageView;
    }

    public ArrayList<Integer> getmBackgroundIds() {
        return mBackgroundIds;
    }

    public void setmBackgroundIds(ArrayList<Integer> mBackgroundIds) {
        this.mBackgroundIds = mBackgroundIds;
    }



}
