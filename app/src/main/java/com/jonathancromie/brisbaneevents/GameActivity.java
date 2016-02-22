package com.jonathancromie.brisbaneevents;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends ActionBarActivity {

    MyDBHandler dbHandler;

    ViewGroup main_layout;
    ImageView imageView;
    ImageButton moving_button;
    TextView score_text;
    TextView time_text;
    Random random;
    CountDownTimer timer;

    private int click = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dbHandler = new MyDBHandler(this, null, null, 1);

        time_text = (TextView) findViewById(R.id.time_text);
        time_text.setText("30");

        score_text = (TextView) findViewById(R.id.score_text);
        score_text.setText("0");

        main_layout = (ViewGroup) findViewById(R.id.main_layout);

        imageView = (ImageView) findViewById(R.id.imageView);

        if (getIntent().getByteArrayExtra("userBackground") != null) {
            byte[] b = getIntent().getExtras().getByteArray("userBackground");
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

            imageView.setImageBitmap(bitmap);
        }

        else {
            imageView.setImageResource(getIntent().getIntExtra("backgroundImageId", R.drawable.mountain));
        }

        moving_button = (ImageButton) findViewById(R.id.moving_button);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        moving_button.setLayoutParams(params);
        moving_button.setScaleType(ImageButton.ScaleType.CENTER_CROP);

        if (getIntent().getByteArrayExtra("userIcon") != null) {
            byte[] b = getIntent().getExtras().getByteArray("userIcon");
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

            moving_button.setImageBitmap(bitmap);
        }

        else {
            moving_button.setBackgroundResource(getIntent().getIntExtra("iconImageId", R.drawable.ic_android_white_24dp));
        }

        moveButton();
        score_text.setText("" + score);

    }

    public void moveButton() {
        random = new Random();

        moving_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (click == 0) {
                            startTimer();
                            click++;
                        }

                        moving_button.animate().x(random.nextInt(main_layout.getWidth() - moving_button.getWidth())).
                                y(random.nextInt(imageView.getHeight() - moving_button.getHeight()));

                        keepScore();
                    }
                }
        );
    }

    public void keepScore() {


        if (moving_button.isInTouchMode()) {
            score += 1;
            score_text.setText("" + score);
        }
    }

    public void startTimer() {
        time_text = (TextView) findViewById(R.id.time_text);

        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time_text.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                time_text.setText("0");
                saveScore();
                restart();
//                openActivity(HighScoresActivity.class);
            }
        }.start();
    }

    public void saveScore() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = prefs.getString("player_name", null).toString();
        int score = Integer.parseInt(score_text.getText().toString());

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("player_score", score);
        editor.commit();

        Scores scores = new Scores(name, score);

        dbHandler.addScore(scores);
    }

    public void restart() {

        AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
        alertDialog.setTitle("Score");
        alertDialog.setMessage("You scored " + score + " points!\nWould you like to play again?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setup();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openActivity(MainActivity.class);
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void setup() {
        click = 0;
        score = 0;
        time_text.setText("30");
        score_text.setText("0");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
            openActivity(SettingsActivity.class);
        }

        else if (id == R.id.action_background) {
            openActivity(BackgroundActivity.class);
        }

        else if (id == R.id.action_about) {
            openActivity(AboutActivity.class);
        }

        else if (id == R.id.action_high_scores) {
            openActivity(HighScoresActivity.class);
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

    @Override
    public void onBackPressed() {
        reallyExit();
    }

}
