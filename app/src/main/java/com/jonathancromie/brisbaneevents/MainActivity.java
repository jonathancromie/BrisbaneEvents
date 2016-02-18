package com.jonathancromie.brisbaneevents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Array list for list view
    ArrayList<HashMap<String, String>> rssFeedList;

    RSSParser rssParser = new RSSParser();

    RSSFeed rssFeed;

    // array to trace sqlite ids
    String[] sqliteIds;

    public static String TAG_ID = "id";
    public static String TAG_TITLE = "title";
    public static String TAG_LINK = "link";

    // List view
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        // Hashmap for ListView
        rssFeedList = new ArrayList<HashMap<String, String>>();


        /**
         * Calling a background thread which will load
         * web sites stored in SQLite database
         * */
        new loadStoreSites().execute();

        // selecting single ListView item
        lv = (ListView) findViewById(R.id.list);

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                String sqlite_id = ((TextView) view.findViewById(R.id.sqlite_id)).getText().toString();
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), ListRSSItemsActivity.class);
                // passing sqlite row id
                in.putExtra(TAG_ID, sqlite_id);
                startActivity(in);
            }
        });
    }

    /**
     * Response from AddNewSiteActivity.java
     * if response is 100 means new site is added to sqlite
     * reload this activity again to show
     * newly added website in listview
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Background Async Task to get RSS data from URL
     * */
    class loadStoreSites extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(
                    MainActivity.this);
            pDialog.setMessage("Loading websites ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting all stored website from SQLite
         * */
        @Override
        protected String doInBackground(String... args) {
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    RSSDatabaseHandler rssDb = new RSSDatabaseHandler(
                            getApplicationContext());

                    rssDb.addSite(new Website("Fitness and strength events", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm", "http://www.trumba.com/calendars/type.rss?filterview=Fitness&mixin=688783%2c681701%2c782935%2c812762", ""));


                    // listing all websites from SQLite
                    List<Website> siteList = rssDb.getAllSites();

                    sqliteIds = new String[siteList.size()];

                    // loop through each website
                    for (int i = 0; i < siteList.size(); i++) {

                        Website s = siteList.get(i);

                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(TAG_ID, s.getId().toString());
                        map.put(TAG_TITLE, s.getTitle());
                        map.put(TAG_LINK, s.getLink());

                        // adding HashList to ArrayList
                        rssFeedList.add(map);

                        // add sqlite id to array
                        // used when deleting a website from sqlite
                        sqliteIds[i] = s.getId().toString();
                    }
                    /**
                     * Updating list view with websites
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            MainActivity.this,
                            rssFeedList, R.layout.site_list_row,
                            new String[] { TAG_ID, TAG_TITLE, TAG_LINK },
                            new int[] { R.id.sqlite_id, R.id.title, R.id.link });
                    // updating listview
                    lv.setAdapter(adapter);
                    registerForContextMenu(lv);
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
