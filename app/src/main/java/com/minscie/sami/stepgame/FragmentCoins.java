package com.minscie.sami.stepgame;

import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Random;

import static com.minscie.sami.stepgame.R.id.coin_img;
import static com.minscie.sami.stepgame.R.id.idleText;
import static com.minscie.sami.stepgame.R.id.levelupText;

/**
 * Created by Sami on 11.4.2017.
 */

public class FragmentCoins extends android.app.Fragment {

    private TextView levelupTextView;
    private TextView idleTextView;
    private ImageView coinImageView;
    private AnimationDrawable coinFlipAnimation;

    private int level_modifier = 1000;   //1000 * level , change this to make the game longer also needs to be changed in the progress view

    private Animation coinAppearAnimation;
    private Animation coinClickedAnimation;
    private int i = 0;
    private DatabaseHelper gameDB;

    private MediaPlayer mp;

    // Is there a coin to be clicked or not
    boolean coin = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate view with the correct layout
        final View coinsView = inflater.inflate(R.layout.fragment_coins, container, false);

        hideSystemUI(coinsView);

        levelupTextView = (TextView) coinsView.findViewById(levelupText);
        levelupTextView.setVisibility(View.GONE);
        coinImageView = (ImageView) coinsView.findViewById(coin_img); //Init coin imageview as hidden
        coinImageView.setBackgroundResource(R.drawable.animation);
        coinImageView.setVisibility(View.GONE);
        idleTextView = (TextView)coinsView.findViewById(idleText);
        idleTextView.setVisibility(View.GONE);

        mp = MediaPlayer.create(getActivity(), R.raw.badadadink);   //Init mediaplayer

        gameDB = new DatabaseHelper(getContext());


        //Listen if coin imageview is clicked. Nothing happens if the view is empty
        coinImageView.setOnClickListener(new View.OnClickListener() { // Listen if the coin's imageview is clicked
                @Override
                public void onClick(View v) {
                    if(coin == true) {              // Coin can be collected only if theres a coin in ImageView to collected
                        coinFlipAnimation.stop();   // Oneshot animation has to be stopped before running again
                        coinFlipAnimation.start();  // Run coin flip animation again
                        coinImageView.startAnimation(coinClickedAnimation); // Run the animation that moves the flipping coin
                        coin = false;                   // There is no coin to be clicked in ImageView anymore

                        Cursor playerCursor = gameDB.getPlayerData();
                        while(playerCursor.moveToNext()) {
                            int coins = playerCursor.getInt(1);
                            gameDB.updateCoins(coins+1);
                        }

                        levelupTextView.setVisibility(View.GONE); //Hide the levelup text as the coin was collected
                    }
                }
            });



        Thread spinningThread = new Thread() {  // Purpose of this thread is to spin the coin randomly between 30sec and 2sec if the user is not doing anything
                @Override                       // Just an extra effect to make it more lively
                public void run() {
                    try {

                        while (true) {  // Run while loop if there is a coin
                            synchronized (this) {
                                Random random = new Random();
                                wait(random.nextInt(1500 - 500) + 500); // Run the UI thread randomly between 0.5 to 1.5sec

                                if(getActivity() == null) // Check if activity is no longer visible. http://stackoverflow.com/questions/23825549/nullpointerexception-on-getactivity-runonuithreadnew-runnable
                                    return;

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(coin == true) {
                                            idleTextView.setVisibility(View.GONE);
                                            coinFlipAnimation.stop();   // Oneshot animation has to be stopped before running again
                                            coinFlipAnimation.start();  // Run coin flip animation again
                                            mp.start();                 // Play /raw/badadadink.ogg sound
                                        }
                                        if (coin == false){

                                            // Show some text when there's nothing to do
                                            idleTextView.setVisibility(View.VISIBLE);
                                            i++;
                                            if(i == 1)
                                                idleTextView.setText("Keep stepping to earn coins..");
                                            else if(i == 2)
                                                idleTextView.setText("Keep stepping to earn coins...");
                                            else if(i == 3)
                                                idleTextView.setText("Keep stepping to earn coins....");
                                            else if(i == 4)
                                                idleTextView.setText("Keep stepping to earn coins.....");
                                            else if(i == 5)
                                                idleTextView.setText("Keep stepping to earn coins......");
                                            else if(i == 6) {
                                                idleTextView.setText("Keep stepping to earn coins.......");
                                                i = 0;
                                            }


                                            //Get player level and steps check if the player has leveled up
                                            //Make a coin appear if player had leveled up
                                            Cursor playerCursor = gameDB.getPlayerData();
                                            while(playerCursor.moveToNext()) {
                                                int level = playerCursor.getInt(2);
                                                int steps = playerCursor.getInt(3);
                                                playerCursor.close();
                                                if(steps >= level_modifier * level){
                                                    Log.d("Steps reached: ", String.valueOf(steps));
                                                    levelupTextView.setVisibility(View.VISIBLE);
                                                    makeCoinAppear();       // Make the hidden coin imageview visible, start animation and make it clickable
                                                    gameDB.updateOnLevelUp(level+1);
                                                }
                                            }
                                        }
                                    }
                                });

                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                ;
            }; // spinningThread end

            spinningThread.start();




        return coinsView;
    }

private void makeCoinAppear(){
    //Set animations for the coin/imageview
    coinFlipAnimation = (AnimationDrawable) coinImageView.getBackground();
    coinAppearAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.coin_appear);
    coinClickedAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.coin_clicked);

    // http://stackoverflow.com/questions/31562833/how-to-slide-an-imageview-from-left-to-right-smoothly-in-android
    coinImageView.setVisibility(View.VISIBLE);
    coinFlipAnimation.start(); // Run coin flip animation
    coinImageView.startAnimation(coinAppearAnimation);  //Run movement animation. A coin appears into ImageView
    coin = true; // There is a coin in ImageView
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
