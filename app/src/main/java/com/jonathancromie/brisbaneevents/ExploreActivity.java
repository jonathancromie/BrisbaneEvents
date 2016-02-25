package com.jonathancromie.brisbaneevents;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ExploreActivity extends AppCompatActivity {
//    private String link;
    private String title;
    private String address;
    private String date;
    private String booking;
    private String image;
    private String cost;
    private String meeting_point;
    private String requirements;
    private String description;
    private String time_start;
    private String time_end;

    private TextView txtDescription;
    private TextView txtDate;
    private TextView txtAddress;
    private TextView txtCost;
    private TextView txtNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtDescription = (TextView) findViewById(R.id.description);
        txtDate = (TextView) findViewById(R.id.date);
        txtAddress = (TextView) findViewById(R.id.address);
        txtCost = (TextView) findViewById(R.id.cost);
        txtNote = (TextView) findViewById(R.id.note);

        Intent in = getIntent();
        title = in.getStringExtra("title");
        address = in.getStringExtra("address");
        date = in.getStringExtra("date");
        booking = in.getStringExtra("booking");
        image = in.getStringExtra("image");
        cost = in.getStringExtra("cost");
        meeting_point = in.getStringExtra("meeting_point");
        requirements = in.getStringExtra("requirements");
        description = in.getStringExtra("description");
        time_start = in.getStringExtra("time_start");
        time_end = in.getStringExtra("time_end");

        time_start = time_start.replace("T", " ");
        time_end = time_end.replace("T", " ");

        setTitle(title);
        txtDescription.setText(description);
        txtDate.setText(date);
        txtAddress.setText(address);
        txtCost.setText(cost);
        txtNote.setText("Meeting point: " + meeting_point + "\n" +
                        "Requirements: " + requirements + "\n" +
                        "Bookings: " + booking);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                addToCalendar(time_start, time_end);
            }
        });
    }

    public void addToCalendar(String start, String end) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date timeStart = null;
        Date timeEnd = null;
        try {
            timeStart = df.parse(start);
            timeEnd = df.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar beginTime = Calendar.getInstance(TimeZone.getDefault());
        beginTime.setTime(timeStart);
        Calendar endTime = Calendar.getInstance(TimeZone.getDefault());
        endTime.setTime(timeEnd);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(Events.TITLE, title)
                .putExtra(Events.DESCRIPTION, description)
                .putExtra(Events.EVENT_LOCATION, address)
                .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_explore, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle action buttons
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_about:
                showDialog();
                return true;
            case R.id.action_share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void share() {
        Intent in = new Intent();
        in.setAction(Intent.ACTION_SEND);
        in.putExtra(Intent.EXTRA_TEXT, title + "\n\n" +
                description + "\n" +
                date + "\n" +
                address + "\n" +
                cost + "\n" +
                "Meeting point: " + meeting_point + "\n" +
                "Requirements: " + requirements + "\n" +
                "Booking: " + booking);
        in.setType("text/plain");
        startActivity(in);
        finish();
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
}
