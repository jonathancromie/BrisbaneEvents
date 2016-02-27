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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class EventFragment extends Fragment implements SearchView.OnQueryTextListener {
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
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter.setFilter(rssItems);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sort_title:
                sortTitle();
                return true;
            case R.id.action_sort_date:
                sortDate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sortTitle() {
        progressBar.setVisibility(View.VISIBLE);
        animation.start ();
        Collections.sort(rssItems, new Comparator<RSSItem>() {

            @Override
            public int compare(RSSItem lhs, RSSItem rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
        progressBar.clearAnimation();
        progressBar.setVisibility(View.INVISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void sortDate() {
        progressBar.setVisibility(View.VISIBLE);
        animation.start ();
        Collections.sort(rssItems, new Comparator<RSSItem>() {

            @Override
            public int compare(RSSItem lhs, RSSItem rhs) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String str1 = lhs.getTimeStart();
                str1 = str1.replace("T", " ");
                String str2 = rhs.getTimeStart();
                str2 = str2.replace("T", " ");

                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = df.parse(str1);
                    date2 = df.parse(str2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return date1.compareTo(date2);
            }
        });
        progressBar.clearAnimation();
        progressBar.setVisibility(View.INVISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Here is where we are going to implement our filter logic
        final List<RSSItem> filteredModeList = filter(rssItems, query);
        adapter.setFilter(filteredModeList);

        return true;

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    private List<RSSItem> filter(List<RSSItem> models, String query) {
        query = query.toLowerCase();

        final List<RSSItem> filteredModelList = new ArrayList<>();
        for (RSSItem model : models) {
            final String title = model.getTitle().toLowerCase();
            final String address = model.getAddress().toLowerCase();
            if (title.contains(query) || address.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
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
