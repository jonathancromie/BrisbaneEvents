package com.jonathancromie.brisbaneevents;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EventFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

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
    private ProgressDialog pDialog;

    public static EventFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        adapter = new CustomListAdapter(getDataSet());


//        mRecyclerView.setAdapter(adapter);

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

    private List<RSSItem> getDataSet() {
        return rssItems;
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
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading events...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
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
            }


            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String args) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            adapter = new CustomListAdapter(getDataSet());
            layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(adapter);
        }
    }
}
