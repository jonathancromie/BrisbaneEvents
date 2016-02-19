package com.jonathancromie.brisbaneevents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonathan on 19-Feb-16.
 */
public class CustomListAdapter extends SimpleAdapter {

    private static final String TAG_BRISBANE = "Brisbane";

    private Context mContext;
    public LayoutInflater inflater=null;

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public CustomListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.rss_item_list_row, null);

        HashMap<String, String> data = (HashMap<String, String>) getItem(position);
        ImageView imageView = (ImageView)vi.findViewById(R.id.imageView);
        TextView textLink = (TextView)vi.findViewById(R.id.page_url);
        TextView textTitle = (TextView)vi.findViewById(R.id.title);
        TextView textAddress = (TextView)vi.findViewById(R.id.address);
        TextView textDate = (TextView)vi.findViewById(R.id.date);
        TextView textBooking = (TextView)vi.findViewById(R.id.booking);

        String link = (String) data.get("link");
        String title = (String) data.get("title");
        String address = (String) data.get("address");
        String date = (String) data.get("date");
        String booking = (String) data.get("booking");
        String image = (String) data.get("image");


        textLink.setText(link);
        textTitle.setText(title);
        textAddress.setText(address);
        textDate.setText(date);
        textBooking.setText(booking);

        Picasso.with(mContext).load(image).resize(75, 75).placeholder(R.drawable.ic_photo_camera_black_24dp).error(R.drawable.ic_photo_camera_black_24dp).into(imageView);

        return vi;
    }
}
