package com.minscie.sami.stepgame;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Sami on 17.4.2017.
 */

public class BottomInfo extends Activity {

    DatabaseHelper gameDB;

    TextView levelTV;
    TextView coinsTV;
    TextView totalstepsTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideSystemUI(getWindow().getDecorView());

        gameDB = new DatabaseHelper(this);  //DatabaseHelper.java used to access the database values


        //Touch outside of the layout closes activity http://stackoverflow.com/questions/4650246/how-to-cancel-an-dialog-themed-like-activity-when-touched-outside-the-window

        // Make us non-modal, so that others can receive touch events.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        getWindow().getAttributes().gravity=Gravity.BOTTOM;
        // Note that flag changes must happen *before* the content view is set.
        setContentView(R.layout.activity_bottominfo);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.widthPixels;

        getWindow().setLayout((width), (int) (height*.5)); // Set this pop-up activity height to half of the screen width;

        int level = 0;
        int coins = 0;
        int total_steps = 0;

        Cursor playerCursor = gameDB.getPlayerData();
        while(playerCursor.moveToNext()){
            level = playerCursor.getInt(2);    //See databasehelper for indexes
            coins = playerCursor.getInt(1);
            total_steps = playerCursor.getInt(4);
            playerCursor.close();
        }

        levelTV = (TextView)findViewById(R.id.levelValue);
        coinsTV = (TextView)findViewById(R.id.coinsValue);
        totalstepsTV = (TextView)findViewById(R.id.totalStepsValue);

        levelTV.setText(Integer.toString(level));
        coinsTV.setText(Integer.toString(coins));
        totalstepsTV.setText(Integer.toString(total_steps));

        Button creditsButton = (Button)findViewById(R.id.creditsButton);
        final Intent intent = new Intent(this, CreditsInfo.class);

        creditsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                overridePendingTransition(R.anim.bottominfo_slide_up,1);  // Slide up animation
            }
        });

    }


    public boolean onTouchEvent(MotionEvent event) {                    // Listen touch events outside of the layout
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            this.finish();                                          // Close pop-up if touch detected
            return true;
        }
        return super.onTouchEvent(event);
    }





    // This snippet hides the system bars.
    private void hideSystemUI(View mDecorView) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }


}
