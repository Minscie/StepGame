package com.minscie.sami.stepgame;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Sami on 15.4.2017.
 */

public class FragmentShoplist extends android.app.Fragment {

    DatabaseHelper gameDB;

    private ListView shopListView;
    private Cursor names;

    private int level;
    private int coins;

    // Values for the shop ListView. Name, description, button values come from the database
    // Database values appended in OnCreate method

    //Number of items, Every items has to have image
    int number_of_items = 14;

    int[] IMAGES = new int[]{
            R.drawable.iconfritree, R.drawable.iconpinetree, R.drawable.iconoaktree, R.drawable.icontreehouse,
            R.drawable.iconthatched, R.drawable.iconhouse, R.drawable.iconinn, R.drawable.icontavern, R.drawable.icontavern,
            R.drawable.iconclock, R.drawable.iconchapel, R.drawable.iconfish, R.drawable.iconfruit, R.drawable.icontailor
    };
    private String[] NAMES = new String [number_of_items];
    private String[] DESCS = new String [number_of_items];
    private String[] VALUE = new String [number_of_items];
    private String[] REQLVL = new String [number_of_items];
    private String[] ITEMCOUNT = new String [number_of_items];

    private ImageView itemImageView;
    private TextView itemNameTextView;
    private TextView itemDescTextView;
    private Button itemButton;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View shopListView = inflater.inflate(R.layout.fragment_shop, container, false);

        hideSystemUI(shopListView);


        gameDB = new DatabaseHelper(getContext());  //DatabaseHelper.java used to access the database values



        Cursor playerCursor = gameDB.getPlayerData();   //Get player data for later comparison. Does the player afford? Level high enough?
        while(playerCursor.moveToNext()){
            coins = playerCursor.getInt(1);
            level = playerCursor.getInt(2);
            playerCursor.close();
        }

        names = gameDB.getInventoryItems();  //Select * from the inventory table

        int i = 0;
        while(names.moveToNext()){                  //Place indexed values in the correct String arrays
            VALUE[i] = (names.getString(1));      // Column 2 = Prices;
            NAMES[i] = (names.getString(2));        // Column 3 = Names;
            DESCS[i] = (names.getString(3));        // Cloumn 4 = Desc;
            REQLVL[i] = (names.getString(4));       // Required level will be used to open locked items
            ITEMCOUNT[i] = (names.getString(5));    // Item count will be used to lock items after buy
            i++;
        }
        names.close();  // Close cursor
        gameDB.close(); // Close database

        //Show listviewlayout_shop in the FragmentShoplist viev by customizing the BaseAdapter
        ListView listView = (ListView)shopListView.findViewById(R.id.list_view);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        // Inflate the layout for this fragment
        return shopListView;
    }



    // Customizing ListView using BaseAdapter
    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() { // This determines no of items displayed. Every items has to have a image or the item is not shown
            return IMAGES.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }





        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // listviewlayout_shop.xml to be used in every position in the custom ListView
            convertView = getActivity().getLayoutInflater().inflate(R.layout.listviewlayout_shop,null);

            //Decalare all things in the listviewlayout_shop.xml
            itemImageView =(ImageView)convertView.findViewById(R.id.itemImageView);
            itemNameTextView = (TextView)convertView.findViewById(R.id.itemNameTextView);
            itemDescTextView = (TextView)convertView.findViewById(R.id.itemDescTextView);
            itemButton = (Button)convertView.findViewById(R.id.buyButton);

            //Set array values into the listviewlayout
            itemImageView.setImageResource(IMAGES[position]);
            itemNameTextView.setText(NAMES[position]);
            itemDescTextView.setText(DESCS[position]);


            if(Integer.valueOf(ITEMCOUNT[position]) == 1) {
                itemButton.setText("Sold Out");
            }

            else {
                itemButton.setText("$" + VALUE[position]);
            }

            if(Integer.valueOf(REQLVL[position]) > level) {
                itemButton.setText("Lvl req: "+REQLVL[position]);
            }


                //Lock the item if level is not high enough or the item has already been bought
            if(Integer.valueOf(REQLVL[position]) > level || Integer.valueOf(ITEMCOUNT[position]) != 0) {
                itemButton.setEnabled(false);
            }


            //set click listener on the buttons. Int position determines which item is clicked;
            itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(Integer.valueOf(VALUE[position]) > coins){
                        Toast toast = Toast.makeText(getActivity(), "Sorry, Not enough coins!", Toast.LENGTH_LONG );
                        toast.show();
                    }
                    else {
                        coins = coins - Integer.valueOf(VALUE[position]);
                        gameDB.updateItemCount(String.valueOf(position), 1);
                        ITEMCOUNT[position] = "1";
                        gameDB.updateCoins(coins);
                        CustomAdapter.this.notifyDataSetChanged();

                    }

                    gameDB.close(); // Close database
                }
            });


            return convertView;
        }
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
