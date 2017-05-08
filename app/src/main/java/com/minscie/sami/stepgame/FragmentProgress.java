package com.minscie.sami.stepgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


// This class handles all the progress at the bottom bar. Progress can go over maximum if the users doesn't collect the coin from a level up
// Therefore total steps may vary from the steps

public class FragmentProgress extends android.app.Fragment implements SensorEventListener{

    private int steps;
    private int total_steps;
    private int initial_total_steps;

    private int to_next_lvl;
    private int lvl_modifier = 100; //100 * level , change this to make the game longer also needs to be changed in the coins view
    private int level;
    private int old_level = -1;


    // Declare Database helper
    DatabaseHelper gameDB;

    // Declare sensor manager;
    SensorManager SM;

    // Declare textview for points
    TextView tv_points;

    private int sensorValue;

    //Declare progressBar
    private ProgressBar progressBar;
    private int initial_steps;

    SharedPreferences prefs = null;

    private RelativeLayout this_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        gameDB = new DatabaseHelper(getContext());  //DatabaseHelper used to access the database values

        Cursor playerCursor = gameDB.getPlayerData();
        while(playerCursor.moveToNext()){
            total_steps = playerCursor.getInt(4);
            playerCursor.close();
        }

        prefs = getActivity().getSharedPreferences(
                "com.minscie.sami.stepgame", Context.MODE_PRIVATE);

        prefs.edit().putBoolean("firstrun", true).apply();

        Log.d("d","10"+ initial_steps);
        //Inflate view with the correct layout
        View progressView = inflater.inflate(R.layout.fragment_progress, container, false);

        //Init textview
        tv_points = (TextView) progressView.findViewById(R.id.pointsTextView);

        //Init progressBar
        progressBar = (ProgressBar) progressView.findViewById(R.id.progressBar);

        //Init sensor manager with a sensor service
        SM = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        //Set on click listener on the progress bar to open pop-up
        this_layout = (RelativeLayout)progressView.findViewById(R.id.fragment_progress);
        this_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BottomInfo.class));
                getActivity().overridePendingTransition(R.anim.bottominfo_slide_up,1);  // Slide up animation
            }
        });

        //Return the inflated layout for this fragment
        return progressView;

    }

    @Override
    public void onResume(){
        super.onResume();
        Cursor playerCursor = gameDB.getPlayerData();
        while(playerCursor.moveToNext()){
            level = playerCursor.getInt(2);
            to_next_lvl = playerCursor.getInt(2) * lvl_modifier;
            steps = playerCursor.getInt(3);
            total_steps = playerCursor.getInt(4);
            progressBar.setMax(to_next_lvl);
            playerCursor.close();
        }

        Sensor countSensor = SM.getDefaultSensor(Sensor.TYPE_STEP_COUNTER); //Init step counter sensor;
        if(countSensor != null) {
            SM.registerListener(this, countSensor, SM.SENSOR_DELAY_NORMAL); //Start getting step from hardware
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        gameDB.updateCurrentSteps(steps);
        gameDB.updateTotalSteps(total_steps);   // Save to database on pause
        gameDB.close();

        SM.unregisterListener(this); //Stops listening steps from hardware
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        sensorValue = (int)event.values[0];     //Get step counter values on sensor event

        if (prefs.getBoolean("firstrun", true)) {
            // Run the only on first run then set 'firstrun' as false

            initial_steps = sensorValue - steps;
            old_level = level;
            initial_total_steps = sensorValue - total_steps;

            // apply prefs
            prefs.edit().putBoolean("firstrun", false).apply();
        }



        Cursor playerCursor = gameDB.getPlayerData();   // Get player data from the database
        while(playerCursor.moveToNext()){
            level = playerCursor.getInt(2);
            steps = playerCursor.getInt(3);
            total_steps = playerCursor.getInt(4);
            playerCursor.close();
        }
        to_next_lvl = level * lvl_modifier;

        steps = sensorValue - initial_steps;
        total_steps = sensorValue - initial_total_steps;



        gameDB.updateCurrentSteps(steps);
        gameDB.updateTotalSteps(total_steps);

        tv_points.setText(Integer.toString(steps)+" / "+(to_next_lvl)); //Update progress

        progressBar.setProgress(steps);     //Do these things when level up
        if(level > old_level) {
            initial_steps = sensorValue;
            steps = sensorValue - initial_steps;    // Resets steps to 0
            progressBar.setProgress(0);             // Set progressbar to 0
            progressBar.setMax(to_next_lvl);        // Set progress bar maximum to next level
            old_level = level;                      //End leveling up
            tv_points.setText("LEVELED UP!");       //Small notification for the user on the progressbar, will disappear on next step taken
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}
