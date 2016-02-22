package com.jonathancromie.brisbaneevents;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.jonathancromie.brisbaneevents.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends DialogFragment {

    private TextView about;
    private int taps = 0;
    private float size = 14;

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("About")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_about, null, false);

        about = (TextView) view.findViewById(R.id.about);

        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent event) {
                        about.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
                        size += 1;
                        taps += 1;

                        if (taps == 5) {
                            Intent i = new Intent(getActivity(), StartActivity.class);
                            startActivity(i);
                        }


                        return true;
                    }


                });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        builder.setView(view);
        return builder.create();
    }



}
