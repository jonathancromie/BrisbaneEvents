package com.jonathancromie.brisbaneevents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Jonathan on 18-Feb-16.
 */
public class ListRSSItemsActivity extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;

    // Array list for list view
    ArrayList<HashMap<String, String>> rssItemList = new ArrayList<HashMap<String,String>>();

    RSSParser rssParser = new RSSParser();

    List<RSSItem> rssItems = new ArrayList<RSSItem>();

    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_ADDRESS = "address";
    private static String TAG_DATE = "date";
    private static String TAG_BOOKING = "booking";
    private static String TAG_GUID = "guid"; // not used
    private static String TAG_IMAGE = "image";

    private String title;

    private RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rss_items);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // get intent data
        Intent i = getIntent();

        // SQLite Row id
        Integer site_id = Integer.parseInt(i.getStringExtra("id"));

        // Title
        title = i.getStringExtra("title");

        setTitle(title);

        // Getting Single website from SQLite
        RSSDatabaseHandler rssDB = new RSSDatabaseHandler(getApplicationContext());


        Website site = rssDB.getSite(site_id);
        String rss_link = site.getRSSLink();
        /**
         * Calling a backgroung thread will loads recent articles of a website
         * @param rss url of website
         * */
        new loadRSSFeedItems().execute(rss_link);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    private List<RSSItem> getDataSet() {
        return rssItems;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
            pDialog = new ProgressDialog(
                    ListRSSItemsActivity.this);
            pDialog.setMessage("Loading " + title);
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

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(new CustomAdapter(getDataSet()));
                    RecyclerView.ItemDecoration itemDecoration =
                            new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
                    recyclerView.addItemDecoration(itemDecoration);
                }
            });
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String args) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
        }
    }
}
