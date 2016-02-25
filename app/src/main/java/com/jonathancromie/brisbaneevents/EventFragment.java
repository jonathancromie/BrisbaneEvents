package com.jonathancromie.brisbaneevents;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EventFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    String link;
    String title;
    String address;
    String date;
    String booking;
    String image;
    String cost;
    String meeting_point;
    String requirements;
    String description;
    String time_start;
    String time_end;

    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_ADDRESS = "address";
    private static String TAG_DATE = "date";
    private static String TAG_BOOKING = "booking";
    private static String TAG_GUID = "guid"; // not used
    private static String TAG_IMAGE = "image";

    private int mPage;

    // Array list for list view
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String,String>>();

    RSSParser rssParser = new RSSParser();

    List<RSSItem> rssItems = new ArrayList<RSSItem>();

    private RecyclerView mRecyclerView;
    private CustomListAdapter adapter;
    private LinearLayoutManager layoutManager;

    // Progress Dialog
//    private ProgressDialog pDialog;
    ProgressBar progressBar;
    ObjectAnimator animation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String link = rssItems.get(position).getLink();
                        String title = rssItems.get(position).getTitle();
                        String address = rssItems.get(position).getAddress();
                        String date = rssItems.get(position).getDate();
                        String booking = rssItems.get(position).getBooking();
                        String image = rssItems.get(position).getImage();
                        String cost = rssItems.get(position).getCost();
                        String meeting_point = rssItems.get(position).getMeetingPoint();
                        String requirements = rssItems.get(position).getRequirements();
                        String description = rssItems.get(position).getDescription();
                        String time_start = rssItems.get(position).getTimeStart();
                        String time_end = rssItems.get(position).getTimeEnd();

                        Intent in = new Intent(getActivity(), ExploreActivity.class);
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
                        startActivity(in);

                    }
                })
        );

        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.circular_21));
        }



        animation = ObjectAnimator.ofInt (progressBar, "progress", 0, 500); // see this max value coming back here, we animale towards that value


        // Getting Single website from SQLite
        RSSDatabaseHandler rssDB = new RSSDatabaseHandler(getContext());


        Website site = rssDB.getSite(mPage);
        String rss_link = site.getRSSLink();
//        /**
//         * Calling a backgroung thread will loads recent articles of a website
//         * @param rss url of website
//         * */
        new loadRSSFeedItems().execute(rss_link);



        return view;
    }

    /**
     * Background Async Task to get RSS Feed Items data from URL
     * */
    class loadRSSFeedItems extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            animation.start ();
        }

        /**
         * getting all recent articles and showing them in listview
         * */
        @Override
        protected String doInBackground(String... args) {
            // rss link url
            String rss_url = args[0];

            // list of rss items
            rssItems = rssParser.getRSSFeedItems(rss_url);

            // looping through each item
            for(RSSItem item : rssItems){
                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();

                // adding each child node to HashMap key => value
                map.put(TAG_TITLE, item.getTitle());
                map.put(TAG_LINK, item.getLink());
                map.put(TAG_ADDRESS, item.getAddress());
                map.put(TAG_DATE, item.getDate());
                map.put(TAG_BOOKING, item.getBooking());
                map.put(TAG_IMAGE, item.getImage());

                // adding HashList to ArrayList
                rssItemList.add(map);

                link = item.getLink();
                title = item.getTitle();
                address = item.getAddress();
                date = item.getDate();
                booking = item.getBooking();
                image = item.getImage();
                cost = item.getCost();
                meeting_point = item.getMeetingPoint();
                requirements = item.getRequirements();
                time_start = item.getTimeStart();
                time_end = item.getTimeEnd();
            }


            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String args) {
            progressBar.clearAnimation();
            progressBar.setVisibility(View.INVISIBLE);

            adapter = new CustomListAdapter(rssItems);
            layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(adapter);

        }
    }
}
