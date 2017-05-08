package com.minscie.sami.stepgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Sami on 11.4.2017.
 */

public class FragmentWorld extends android.app.Fragment {


    DatabaseHelper gameDB;
    private Cursor names;


    private RelativeLayout worldView;
    private ImageView castle;
    private ImageView chapel;
    private ImageView clock;
    private ImageView firtree;
    private ImageView fish;
    private ImageView fruit;
    private ImageView house;
    private ImageView inn;
    private ImageView oaktree;
    private ImageView pinetree;
    private ImageView tailor;
    private ImageView tavern;
    private ImageView thatched;
    private ImageView treehouse;
    private ImageView villa;

    int number_of_items = 14;
    private String[] ITEMS = new String[number_of_items];

    private int xDelta;
    private int yDelta;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate view with the correct layout
        final View worldView = inflater.inflate(R.layout.fragment_world, container, false);


        hideSystemUI(worldView);

        this.worldView = (RelativeLayout) worldView.findViewById(R.id.worldView);

        castle = (ImageView) worldView.findViewById(R.id.castleIV);
        chapel = (ImageView) worldView.findViewById(R.id.chapelIV);
        clock = (ImageView) worldView.findViewById(R.id.clockIV);
        firtree = (ImageView) worldView.findViewById(R.id.firtreeIV);
        fish = (ImageView) worldView.findViewById(R.id.fishIV);
        fruit = (ImageView) worldView.findViewById(R.id.fruitIV);
        house = (ImageView) worldView.findViewById(R.id.houseIV);
        inn = (ImageView) worldView.findViewById(R.id.innIV);
        oaktree = (ImageView) worldView.findViewById(R.id.oaktreeIV);
        pinetree = (ImageView) worldView.findViewById(R.id.pinetreeIV);
        tailor = (ImageView) worldView.findViewById(R.id.tailorIV);
        tavern = (ImageView) worldView.findViewById(R.id.tavernIV);
        thatched = (ImageView) worldView.findViewById(R.id.thatchedIV);
        treehouse = (ImageView) worldView.findViewById(R.id.treehouseIV);
        villa = (ImageView) worldView.findViewById(R.id.villaIV);

        gameDB = new DatabaseHelper(getContext());  //DatabaseHelper.java used to access the database values

        names = gameDB.getInventoryItems();  //Select * from the inventory table


        int i = 0;
        while (names.moveToNext()) {                  //Place indexed values in the correct String arrays
            ITEMS[i] = (names.getString(5));    // Item count
            i++;
        }
        names.close();  // Close cursor
        gameDB.close(); // Close database

        //Items that haven't been bought are set to be invisible
        //Silly repetitive code in this case
        if (Integer.valueOf(ITEMS[1]) == 0) {
            pinetree.setVisibility(View.INVISIBLE);
        }
        if (Integer.valueOf(ITEMS[2]) == 0) {
            oaktree.setVisibility(View.INVISIBLE);
        }
        if (Integer.valueOf(ITEMS[3]) == 0) {
            treehouse.setVisibility(View.INVISIBLE);
        }
        if (Integer.valueOf(ITEMS[4]) == 0) {
            thatched.setVisibility(View.INVISIBLE);
        }
        if (Integer.valueOf(ITEMS[5]) == 0) {
            house.setVisibility(View.INVISIBLE);
        }
        if (Integer.valueOf(ITEMS[6]) == 0) {
            inn.setVisibility(View.INVISIBLE);
        }
        if (Integer.valueOf(ITEMS[7]) == 0) {
            tavern.setVisibility(View.INVISIBLE);
        }
        if (Integer.valueOf(ITEMS[8]) == 0) {
            villa.setVisibility(View.INVISIBLE);
        }
        if (Integer.valueOf(ITEMS[9]) == 0) {
            clock.setVisibility(View.INVISIBLE);
        }
        if (Integer.valueOf(ITEMS[10]) == 0) {
            chapel.setVisibility(View.INVISIBLE);
        }
        if (Integer.valueOf(ITEMS[11]) == 0) {
            fish.setVisibility(View.INVISIBLE);
        }
        if (Integer.valueOf(ITEMS[12]) == 0) {
            fruit.setVisibility(View.INVISIBLE);
        }
        if (Integer.valueOf(ITEMS[13]) == 0) {
            tailor.setVisibility(View.INVISIBLE);
        }

        FrameLayout.LayoutParams layout_para = (FrameLayout.LayoutParams)   // Try to slightly center the the world (x = -200 y = -200)
                worldView.getLayoutParams();
        layout_para.leftMargin = 800 - 1000;
        layout_para.topMargin = 800 - 1000;
        this.worldView.setLayoutParams(layout_para);

        // Load last saved positions from the shared preferences
        setItemDefaultPosition(castle, "R.integer.saved_castle_x_default", "R.integer.saved_castle_y_default");
        setItemDefaultPosition(chapel, "R.integer.saved_chapel_x_default", "R.integer.saved_chapel_y_default");
        setItemDefaultPosition(clock, "R.integer.saved_clock_x_default", "R.integer.saved_clock_y_default");
        setItemDefaultPosition(firtree, "R.integer.saved_firtree_x_default", "R.integer.saved_firtree_y_default");
        setItemDefaultPosition(fish, "R.integer.saved_fish_x_default", "R.integer.saved_fish_y_default");
        setItemDefaultPosition(fruit, "R.integer.saved_fruit_x_default", "R.integer.saved_fruit_y_default");
        setItemDefaultPosition(house, "R.integer.saved_house_x_default", "R.integer.saved_house_y_default");
        setItemDefaultPosition(inn, "R.integer.saved_inn_x_default", "R.integer.saved_inn_y_default");
        setItemDefaultPosition(oaktree, "R.integer.saved_oaktree_x_default", "R.integer.saved_oaktree_y_default");
        setItemDefaultPosition(pinetree, "R.integer.saved_pinetree_x_default", "R.integer.saved_pinetree_y_default");
        setItemDefaultPosition(tailor, "R.integer.saved_tailor_x_default", "R.integer.saved_tailor_y_default");
        setItemDefaultPosition(tavern, "R.integer.saved_tavern_x_default", "R.integer.saved_tavern_y_default");
        setItemDefaultPosition(thatched, "R.integer.saved_thatched_x_default", "R.integer.saved_thatched_y_default");
        setItemDefaultPosition(treehouse, "R.integer.saved_treehouse_x_default", "R.integer.saved_treehouse_y_default");
        setItemDefaultPosition(villa, "R.integer.saved_villa_x_default", "R.integer.saved_villa_y_default");



        //Listen for touches on the background - With this you can move the whole relativelayout
        this.worldView.setOnTouchListener(new View.OnTouchListener() {       //Background touch listener to move everything on worldView on touch
            @Override
            //Looks like the camera is moving but the worldView and all its content itself is moving
            public boolean onTouch(View v, MotionEvent event) {
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        FrameLayout.LayoutParams layout_para = (FrameLayout.LayoutParams)
                                worldView.getLayoutParams();

                        xDelta = x - layout_para.leftMargin;     // Get the position of the touch
                        yDelta = y - layout_para.topMargin;
                        break;


                    case MotionEvent.ACTION_MOVE:
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) worldView.getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;   // Change the layout params to the finger postion
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        worldView.setLayoutParams(layoutParams);
                        break;
                }
                castle.invalidate();    // Have to invalidate some imageview for rendering to work properly
                                        // I chose the castle because it's always there
                return true;
            }
        });


        // Wait for the user to touch items, saving the location to the shared preferences on ACTION_UP
        itemMovementListener(castle, "R.integer.saved_castle_x_default", "R.integer.saved_castle_y_default");
        itemMovementListener(chapel, "R.integer.saved_chapel_x_default", "R.integer.saved_chapel_y_default");
        itemMovementListener(firtree, "R.integer.saved_firtree_x_default", "R.integer.saved_firtree_y_default");
        itemMovementListener(clock, "R.integer.saved_clock_x_default", "R.integer.saved_clock_y_default");
        itemMovementListener(fish, "R.integer.saved_fish_x_default", "R.integer.saved_fish_y_default");
        itemMovementListener(fruit, "R.integer.saved_fruit_x_default", "R.integer.saved_fruit_y_default");
        itemMovementListener(house, "R.integer.saved_house_x_default", "R.integer.saved_house_y_default");
        itemMovementListener(inn, "R.integer.saved_inn_x_default", "R.integer.saved_inn_y_default");
        itemMovementListener(oaktree, "R.integer.saved_oaktree_x_default", "R.integer.saved_oaktree_y_default");
        itemMovementListener(pinetree, "R.integer.saved_pinetree_x_default", "R.integer.saved_pinetree_y_default");
        itemMovementListener(tailor, "R.integer.saved_tailor_x_default", "R.integer.saved_tailor_y_default");
        itemMovementListener(tavern, "R.integer.saved_tavern_x_default", "R.integer.saved_tavern_y_default");
        itemMovementListener(thatched, "R.integer.saved_thatched_x_default", "R.integer.saved_thatched_y_default");
        itemMovementListener(treehouse, "R.integer.saved_treehouse_x_default", "R.integer.saved_treehouse_y_default");
        itemMovementListener(villa, "R.integer.saved_villa_x_default", "R.integer.saved_villa_y_default");


        return worldView;
    }

    //Method to load items saved positions
    private void setItemDefaultPosition(final ImageView view, final String variable_x, final String variable_y) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int default_xDelta = sharedPref.getInt(variable_x, -1);
        int default_yDelta = sharedPref.getInt(variable_y, -1);


        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.leftMargin = default_xDelta;
        layoutParams.topMargin = default_yDelta;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;
        view.setLayoutParams(layoutParams);

    }


    //Method to listen imageview touch and movement
    private void itemMovementListener(final ImageView item, final String variable_x, final String variable_y) {
        item.setOnTouchListener(new View.OnTouchListener() {       //Background touch listener to move everything on worldView on touch
            @Override
            //Looks like the camera is moving but the worldView and all its content itself is moving
            public boolean onTouch(View v, MotionEvent event) {
                final int x = (int) event.getRawX();
                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams layout_para = (RelativeLayout.LayoutParams)
                                item.getLayoutParams();

                        xDelta = x - layout_para.leftMargin;
                        yDelta = y - layout_para.topMargin;
                        break;


                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) item.getLayoutParams();
                        layoutParams.leftMargin = x - xDelta;
                        layoutParams.topMargin = y - yDelta;

                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        item.setLayoutParams(layoutParams);
                        break;

                    case MotionEvent.ACTION_UP:
                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);      // Save the last location of imageview to shared preferences
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt(variable_x, x - xDelta);
                        editor.putInt(variable_y, y - yDelta);
                        editor.commit();

                }
                item.invalidate();    // Have to invalidate the moved imageview
                return true;
            }
        });
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
