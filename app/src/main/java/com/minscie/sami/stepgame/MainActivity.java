package com.minscie.sami.stepgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment;                      // Set coin collecting to load on create
        fragment = new FragmentCoins();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_place, fragment);
        ft.commit();

                                                // Set progress bar to load on create
        fragment = new FragmentProgress();
        FragmentTransaction ft2 = fm.beginTransaction();
        ft2.replace(R.id.fragment_place2, fragment);
        ft2.commit();


    }

    public void ChangeFragment(View view){
    Fragment fragment;

        if( view == findViewById(R.id.coinsButton)){         //On button click switch to coin collecting fragment
            fragment = new FragmentCoins();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_place, fragment);
            ft.commit();
        }

        if( view == findViewById(R.id.worldButton)){      //On button click switch to world builder fragment
            fragment = new FragmentWorld();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_place, fragment);
            ft.commit();

        }
        if( view == findViewById(R.id.shopButton)){         //On button click open shop
            fragment = new FragmentShoplist();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragment_place, fragment);
            ft.commit();

        }
    }



}
