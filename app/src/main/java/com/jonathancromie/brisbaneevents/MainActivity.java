package com.jonathancromie.brisbaneevents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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
//    private RecyclerView recyclerView;
//    private LinearLayoutManager linearLayoutManager;
//    private ParkAdapter parkAdapter;

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
                String title = ((TextView) view.findViewById(R.id.title)).getText().toString();
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), ListRSSItemsActivity.class);
                // passing sqlite row id
                in.putExtra(TAG_ID, sqlite_id);
                in.putExtra(TAG_TITLE, title);
                startActivity(in);
            }
        });

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
            pDialog.setMessage("Loading events ...");
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

                    rssDb.addSite(new Website("Fitness and Strength", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=Fitness&mixin=688783%2c681701%2c782935%2c812762", ""));
                    rssDb.addSite(new Website("Business", "http://www.brisbane.qld.gov.au/planning-building/planning-guidelines-tools/neighbourhood-planning/neighbourhood-plans-other-local-planning-projects/spring-hill-neighbourhood-plan",
                            "http://www.trumba.com/calendars/BiB.rss", ""));
                    rssDb.addSite(new Website("Music and concert", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=Music&filter1=_178867_&filterfield1=21859", ""));
                    rssDb.addSite(new Website("Brisbane Botanic Gardens", "http://www.brisbane.qld.gov.au/facilities-recreation/parks-venues-brisbane/parks/brisbane-botanic-gardens-mt-coot-tha/whats-brisbane-botanic-gardens-mt-coot-tha",
                            "http://www.trumba.com/calendars/type.rss?filterview=Fitness&mixin=688783%2c681701%2c782935%2c812762", ""));
                    rssDb.addSite(new Website("Arts, Crafts and Culture", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=arts&filter1=_171831_178893_&filterfield1=21859", ""));
                    rssDb.addSite(new Website("Brisbane Markets", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=Markets&filter1=_178869_&filterfield1=21859", ""));
                    rssDb.addSite(new Website("Library", "http://www.brisbane.qld.gov.au/whats-on/venue/library-events",
                            "http://www.trumba.com/calendars/libraries.rss", ""));
                    rssDb.addSite(new Website("Movies", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=movies&filter1=_178865_&filterfield1=21859", ""));
                    rssDb.addSite(new Website("Visible Ink", "http://www.brisbane.qld.gov.au/whats-on/venue/visible-ink-events",
                            "http://www.trumba.com/calendars/visble-ink.rss", ""));
                    rssDb.addSite(new Website("Teen", "http://www.brisbane.qld.gov.au/whats-on/featured/school-holiday-activities-for-kids",
                            "http://www.trumba.com/calendars/brisbane-kids.rss?filterview=teens", ""));
                    rssDb.addSite(new Website("Southbank Parklands", "http://www.brisbane.qld.gov.au/whats-brisbane/events-council-venues/parks-gardens-events/south-bank-parklands-events",
                            "http://www.trumba.com/calendars/south-bank.rss?filterview=south+bank&filter4=_464155_&filterfield4=22542", ""));
                    rssDb.addSite(new Website("Sir Thomas Brisbane Planetarium", "http://www.brisbane.qld.gov.au/facilities-recreation/arts-culture/sir-thomas-brisbane-planetarium/whats-on-at-the-planetarium",
                            "http://www.trumba.com/calendars/planetarium.rss", ""));
                    rssDb.addSite(new Website("Riverstage", "http://www.brisbane.qld.gov.au/facilities-recreation/arts-culture/riverstage/whats-riverstage",
                            "http://www.trumba.com/calendars/brisbane-riverstage.rss", ""));
                    rssDb.addSite(new Website("Museum of Brisbane", "http://www.brisbane.qld.gov.au/whats-on/venue/museum-of-brisbane",
                            "http://www.trumba.com/calendars/mob.rss", ""));
                    rssDb.addSite(new Website("LIVE program", "http://www.brisbane.qld.gov.au/whats-on/type/live",
                            "http://www.trumba.com/calendars/LIVE.rss", ""));
                    rssDb.addSite(new Website("King George Square", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=Fitness&mixin=688783%2c681701%2c782935%2c812762", ""));
                    rssDb.addSite(new Website("Kids Aged 6 to 12", "http://www.brisbane.qld.gov.au/whats-on/featured/school-holiday-activities-for-kids",
                            "http://www.trumba.com/calendars/brisbane-kids.rss?filterview=kids_6_12", ""));
                    rssDb.addSite(new Website("Infants and toddlers", "http://www.brisbane.qld.gov.au/whats-on/featured/school-holiday-activities-for-kids",
                            "http://www.trumba.com/calendars/brisbane-kids.rss?filterview=infants_toddlers", ""));
                    rssDb.addSite(new Website("Green", "http://www.brisbane.qld.gov.au/whats-on/type/green-events",
                            "http://www.trumba.com/calendars/green-events.rss?filterview=green_events", ""));
                    rssDb.addSite(new Website("GOLD program", "http://www.brisbane.qld.gov.au/whats-on/events-listed-type/sports-recreation-programs/growing-older-living-dangerously",
                            "http://www.trumba.com/calendars/gold.rss?filterview=gold", ""));
                    rssDb.addSite(new Website("GOLD n' Kids", "http://www.brisbane.qld.gov.au/whats-on/events-listed-type/sports-recreation-programs/gold-n-kids",
                            "http://www.trumba.com/calendars/gold-n-kids.rss", ""));
                    rssDb.addSite(new Website("Festivals", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=festivals&filter1=_178868_&filterfield1=21859", ""));
                    rssDb.addSite(new Website("Family", "http://www.brisbane.qld.gov.au/whats-on/audience/youth-events",
                            "http://www.trumba.com/calendars/audience-brisbane.rss?filterview=family&filter1=_178891_&filterfield1=21859", ""));
                    rssDb.addSite(new Website("Chill Out", "http://www.brisbane.qld.gov.au/whats-on/events-listed-type/sports-recreation-programs/chill-out",
                            "http://www.trumba.com/calendars/chill-out.rss", ""));
                    rssDb.addSite(new Website("Brisbane Powerhouse", "http://www.brisbane.qld.gov.au/whats-on/venue/brisbane-powerhouse",
                            "http://www.trumba.com/calendars/brisbane-powerhouse.rss", ""));
                    rssDb.addSite(new Website("Brisbane Parks", "http://www.trumba.com/calendars/brisbane-events-rss",
                            "http://www.trumba.com/calendars/brisbane-events-rss.rss?filterview=parks", ""));
                    rssDb.addSite(new Website("Brisbane City Council", "http://www.brisbane.qld.gov.au/whats-on",
                            "http://www.trumba.com/calendars/brisbane-city-council.rss", "Brisbane City Council runs a range of classes, workshops, activities and events in Brisbane. Use our what's on calendar to search for concerts, sports and fitness, green events, markets, arts, culture, craft and more. Find out what's on today, this week and next month."));
                    rssDb.addSite(new Website("Classes and Workshops", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=classses", ""));
                    rssDb.addSite(new Website("City Hall", "http://www.brisbane.qld.gov.au/facilities-recreation/parks-venues/brisbane-city-hall/whats-on-city-hall",
                            "http://www.trumba.com/calendars/city-hall.rss?filterview=city-hall&filter4=_266279_&filterfield4=22542", ""));
                    rssDb.addSite(new Website("Active Parks", "http://www.brisbane.qld.gov.au/whats-on/events-listed-type/sports-recreation-programs/active-parks-events",
                            "http://www.trumba.com/calendars/active-parks.rss", "Active Parks has exercise in Brisbane parks for all levels of ability. Get a great workout or relax with some gentle exercise. Try out recreational tree climbing, join in a boxing class, get fit with Zumba or get out on the water with kayaking."));

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
                            new int[] { R.id.sqlite_id, R.id.title});
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
