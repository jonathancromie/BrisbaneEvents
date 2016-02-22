package com.jonathancromie.brisbaneevents;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighScoresActivity extends ActionBarActivity {

    private ArrayList<Scores> scores = new ArrayList<Scores>();
    private Runnable runnable;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        setTitle("High Scores");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // improve performance if you know that changes in content
        // do not change the size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // Data set used by the adapter. This data will be displayed.

        // Create the adapter
        RecyclerView.Adapter adapter = new HighScoresAdapter(HighScoresActivity.this, scores);
        recyclerView.setAdapter(adapter);


//        adapter = new CustomAdapter(this, R.layout.list_score, scores);
//        ListView android_list = (ListView) findViewById(R.id.list_view);
//        android_list.setAdapter(adapter);

        runnable = new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
//
        Thread thread = new Thread(null, runnable, "");
        thread.start();
    }

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            queryDatabase();

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            // improve performance if you know that changes in content
            // do not change the size of the RecyclerView
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HighScoresActivity.this);
            recyclerView.setLayoutManager(mLayoutManager);

            // Data set used by the adapter. This data will be displayed.

            //ArrayList<String> mDataSet = new ArrayList<String>();
            //scores = new ArrayList<Scores>();

            // Create the adapter
            RecyclerView.Adapter adapter = new HighScoresAdapter(HighScoresActivity.this, scores);
            recyclerView.setAdapter(adapter);

//            adapter = new CustomAdapter(HighScoresActivity.this, R.layout.list_score, scores);
//            ListView android_list = (ListView) findViewById(R.id.list_view);
//
//            android_list.setAdapter(adapter);
        }
    };

    private void queryDatabase() {
        try {
            MyDBHandler dbHandler = new MyDBHandler(HighScoresActivity.this, "highscores.db", null, 1);
            database = dbHandler.getWritableDatabase();
            Cursor c = database.rawQuery("SELECT playername, playerscore FROM scores", null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String name = c.getString(c.getColumnIndex("playername")).toString();
                        int score = Integer.parseInt(c.getString(c.getColumnIndex("playerscore")).toString());
                        scores.add(new Scores(name, score));
                    }

                    while (c.moveToNext());
                }
            }

            sortScores();
        }

        catch (Exception e) {e.printStackTrace();};
    }

    private void sortScores() {

        Collections.sort(scores, new Comparator<Scores>() {
            @Override
            public int compare(Scores s, Scores s2) {
                return s2.getScore() - s.getScore();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_other, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            openActivity(GameActivity.class);
        }
        else if (id == R.id.action_about) {
            openActivity(AboutActivity.class);
        }
        else if (id == R.id.action_exit) {
            reallyExit();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void reallyExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openActivity(MainActivity.class);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.show();
    }

    @Override
    public void onBackPressed() {
        openActivity(GameActivity.class);
    }
}
