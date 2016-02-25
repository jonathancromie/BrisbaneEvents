package com.jonathancromie.brisbaneevents;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jonathancromie on 20/02/2016.
 */
public class MainActivity extends AppCompatActivity {

    public static String TAG_ID = "id";
    public static String TAG_TITLE = "title";
    public static String TAG_LINK = "link";

    private String[] mTitles;
    private TypedArray mIcons;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private String name = "Events in Brisbane";
    private String email = "Send feedback to joncromie@gmail.com";
    private int profile = R.mipmap.ic_launcher;
    // Array list for list view
    ArrayList<HashMap<String, String>> rssFeedList = new ArrayList<HashMap<String, String>>();

    // array to trace sqlite ids
    String[] sqliteIds;
    // Progress Dialog
    private ProgressDialog pDialog;

    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        mTitles = getResources().getStringArray(R.array.titles);
        mIcons = getResources().obtainTypedArray(R.array.icons);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.left_drawer);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CustomDrawerAdapter(mTitles, mIcons, name, email, profile, this);
        mRecyclerView.setAdapter(mAdapter);

//        mRecyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL_LIST));

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {


                        if (position == 0) {

                        }
                        else {
                            CustomDrawerAdapter.selectedItem = position;
                            mAdapter.notifyDataSetChanged();
                            mDrawerLayout.closeDrawers();
                            setTitle(mTitles[position - 1]);

                            EventFragment fragment = new EventFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt(EventFragment.ARG_PAGE, position);
                            fragment.setArguments(bundle);
                            // Insert the fragment by replacing any existing fragment
                            showFragment(fragment);


                        }
                    }
                })
        );

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        /**
         * Calling a background thread which will load
         * web sites stored in SQLite database
         * */
        new loadStoreSites().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // Use this when sort button is on toolbar
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mRecyclerView);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.action_about:
                showDialog();
                return true;
            case R.id.action_exit:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setTitle(getString(R.string.app_name));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void showFragment(Fragment fragment) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    public void showDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AboutFragment newFragment = new AboutFragment();

        // The device is smaller, so show the fragment fullscreen
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // For a little polish, specify a transition animation
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity
//        transaction..replace(android.R.id.content, newFragment)
//                .addToBackStack(null).commit();
        transaction.add(newFragment, "dialog").commit();
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
//            pDialog = new ProgressDialog(
//                    MainActivity.this);
//            pDialog.setMessage("Loading sites ...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
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

                    rssDb.addSite(new Website("Active Parks", "http://www.brisbane.qld.gov.au/whats-on/events-listed-type/sports-recreation-programs/active-parks-events",
                            "http://www.trumba.com/calendars/active-parks.rss", "Active Parks has exercise in Brisbane parks for all levels of ability. Get a great workout or relax with some gentle exercise. Try out recreational tree climbing, join in a boxing class, get fit with Zumba or get out on the water with kayaking."));
                    rssDb.addSite(new Website("Arts, Crafts and Culture", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=arts&filter1=_171831_178893_&filterfield1=21859", ""));
                    rssDb.addSite(new Website("Brisbane Botanic Gardens", "http://www.brisbane.qld.gov.au/facilities-recreation/parks-venues-brisbane/parks/brisbane-botanic-gardens-mt-coot-tha/whats-brisbane-botanic-gardens-mt-coot-tha",
                            "http://www.trumba.com/calendars/brisbane-botanic-gardens.rss", ""));
                    rssDb.addSite(new Website("Brisbane City Council", "http://www.brisbane.qld.gov.au/whats-on",
                            "http://www.trumba.com/calendars/brisbane-city-council.rss", "Brisbane City Council runs a range of classes, workshops, activities and events in Brisbane. Use our what's on calendar to search for concerts, sports and fitness, green events, markets, arts, culture, craft and more. Find out what's on today, this week and next month."));
                    rssDb.addSite(new Website("Brisbane Markets", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=Markets&filter1=_178869_&filterfield1=21859", ""));
                    rssDb.addSite(new Website("Brisbane Parks", "http://www.trumba.com/calendars/brisbane-events-rss",
                            "http://www.trumba.com/calendars/brisbane-events-rss.rss?filterview=parks", ""));
                    rssDb.addSite(new Website("Brisbane Powerhouse", "http://www.brisbane.qld.gov.au/whats-on/venue/brisbane-powerhouse",
                            "http://www.trumba.com/calendars/brisbane-powerhouse.rss", ""));
                    rssDb.addSite(new Website("Business", "http://www.brisbane.qld.gov.au/planning-building/planning-guidelines-tools/neighbourhood-planning/neighbourhood-plans-other-local-planning-projects/spring-hill-neighbourhood-plan",
                            "http://www.trumba.com/calendars/BiB.rss", ""));
                    rssDb.addSite(new Website("Chill Out", "http://www.brisbane.qld.gov.au/whats-on/events-listed-type/sports-recreation-programs/chill-out",
                            "http://www.trumba.com/calendars/chill-out.rss", ""));
                    rssDb.addSite(new Website("City Hall", "http://www.brisbane.qld.gov.au/facilities-recreation/parks-venues/brisbane-city-hall/whats-on-city-hall",
                            "http://www.trumba.com/calendars/city-hall.rss?filterview=city-hall&filter4=_266279_&filterfield4=22542", ""));
                    rssDb.addSite(new Website("Classes and Workshops", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=classses", ""));
                    rssDb.addSite(new Website("Family", "http://www.brisbane.qld.gov.au/whats-on/audience/youth-events",
                            "http://www.trumba.com/calendars/audience-brisbane.rss?filterview=family&filter1=_178891_&filterfield1=21859", ""));
                    rssDb.addSite(new Website("Festivals", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=festivals&filter1=_178868_&filterfield1=21859", ""));
                    rssDb.addSite(new Website("Fitness and Strength", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=Fitness&mixin=688783%2c681701%2c782935%2c812762", ""));
                    rssDb.addSite(new Website("GOLD n' Kids", "http://www.brisbane.qld.gov.au/whats-on/events-listed-type/sports-recreation-programs/gold-n-kids",
                            "http://www.trumba.com/calendars/gold-n-kids.rss", ""));
                    rssDb.addSite(new Website("GOLD program", "http://www.brisbane.qld.gov.au/whats-on/events-listed-type/sports-recreation-programs/growing-older-living-dangerously",
                            "http://www.trumba.com/calendars/gold.rss?filterview=gold", ""));
                    rssDb.addSite(new Website("Green", "http://www.brisbane.qld.gov.au/whats-on/type/green-events",
                            "http://www.trumba.com/calendars/green-events.rss?filterview=green_events", ""));
                    rssDb.addSite(new Website("Infants and toddlers", "http://www.brisbane.qld.gov.au/whats-on/featured/school-holiday-activities-for-kids",
                            "http://www.trumba.com/calendars/brisbane-kids.rss?filterview=infants_toddlers", ""));
                    rssDb.addSite(new Website("Kids Aged 6 to 12", "http://www.brisbane.qld.gov.au/whats-on/featured/school-holiday-activities-for-kids",
                            "http://www.trumba.com/calendars/brisbane-kids.rss?filterview=kids_6_12", ""));
                    rssDb.addSite(new Website("King George Square", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/king-george-sqaure.rss?filterview=KGS&filter4=_200255_&filterfield4=22542", ""));
                    rssDb.addSite(new Website("LIVE program", "http://www.brisbane.qld.gov.au/whats-on/type/live",
                            "http://www.trumba.com/calendars/LIVE.rss", ""));
                    rssDb.addSite(new Website("Library", "http://www.brisbane.qld.gov.au/whats-on/venue/library-events",
                            "http://www.trumba.com/calendars/libraries.rss", ""));
                    rssDb.addSite(new Website("Movies", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=movies&filter1=_178865_&filterfield1=21859", ""));
                    rssDb.addSite(new Website("Museum of Brisbane", "http://www.brisbane.qld.gov.au/whats-on/venue/museum-of-brisbane",
                            "http://www.trumba.com/calendars/mob.rss", ""));
                    rssDb.addSite(new Website("Music and concert", "http://www.brisbane.qld.gov.au/whats-on/featured/Events-in-Brisbane/index.htm",
                            "http://www.trumba.com/calendars/type.rss?filterview=Music&filter1=_178867_&filterfield1=21859", ""));
                    rssDb.addSite(new Website("Riverstage", "http://www.brisbane.qld.gov.au/facilities-recreation/arts-culture/riverstage/whats-riverstage",
                            "http://www.trumba.com/calendars/brisbane-riverstage.rss", ""));
                    rssDb.addSite(new Website("Sir Thomas Brisbane Planetarium", "http://www.brisbane.qld.gov.au/facilities-recreation/arts-culture/sir-thomas-brisbane-planetarium/whats-on-at-the-planetarium",
                            "http://www.trumba.com/calendars/planetarium.rss", ""));
                    rssDb.addSite(new Website("Southbank Parklands", "http://www.brisbane.qld.gov.au/whats-brisbane/events-council-venues/parks-gardens-events/south-bank-parklands-events",
                            "http://www.trumba.com/calendars/south-bank.rss?filterview=south+bank&filter4=_464155_&filterfield4=22542", ""));
                    rssDb.addSite(new Website("Teen", "http://www.brisbane.qld.gov.au/whats-on/featured/school-holiday-activities-for-kids",
                            "http://www.trumba.com/calendars/brisbane-kids.rss?filterview=teens", ""));
                    rssDb.addSite(new Website("Visible Ink", "http://www.brisbane.qld.gov.au/whats-on/venue/visible-ink-events",
                            "http://www.trumba.com/calendars/visble-ink.rss", ""));

                    // listing all websites from SQLite
                    List<Website> siteList = rssDb.getAllSites();

                    sqliteIds = new String[siteList.size()];

                    // loop through each website
                    for (int i = 0; i < siteList.size(); i++) {

                        Website s = siteList.get(i);

//                        // creating new HashMap
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
                }
            });
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String args) {
            // dismiss the dialog after getting all products
//            pDialog.dismiss();
            EventFragment fragment = new EventFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(EventFragment.ARG_PAGE, 1);
            fragment.setArguments(bundle);

            // Insert the fragment by replacing any existing fragment
            showFragment(fragment);
            setTitle(mTitles[0]);
        }


    }
}
