package com.minscie.sami.stepgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Sami on 16.4.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "GAME.db";
    private static final int DB_VERSION = 101;       //Update version to recreate the database. Tables dropped and onCreate executed

    private static final String PLAYER_TABLE = "PLAYER";

    private static final String PLAYER_ID_0 = "ID";
    private static final String PLAYER_COINS_1 = "COINS";
    private static final String PLAYER_LVL_2 = "LVL";
    private static final String PLAYER_STEPS_3 = "STEPS";
    private static final String PLAYER_TOTALSTEPS_4 = "TOTAL_STEPS";


    private static final String INVENTORY_TABLE = "INVENTORY";

    private static final String INVENTORY_ID_0 = "ID";
    private static final String INVENTORY_PRICE_1 = "PRICE";
    private static final String INVENTORY_NAME_2 = "NAME";
    private static final String INVENTORY_DESC_3 = "DESC";
    private static final String INVENTORY_LVLREQ_4 = "LVLREQ";
    private static final String INVENTORY_ITEMCOUNT_5 = "ITEM_COUNT";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tables PLAYER and INVENOTRY to the Game.db insert rows
        // These queries will be done only if the data is missing. So on the first run

        db.execSQL("CREATE TABLE IF NOT EXISTS "+PLAYER_TABLE+"(" +
                " "+PLAYER_ID_0+" INTEGER PRIMARY KEY, " +
                " "+PLAYER_COINS_1 +" INTEGER, " +
                " "+PLAYER_LVL_2+" INTEGER, " +
                " "+PLAYER_STEPS_3+" INTEGER, " +
                " "+PLAYER_TOTALSTEPS_4+" INTEGER" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+INVENTORY_TABLE+"(" +
                " "+INVENTORY_ID_0+" INTEGER PRIMARY KEY, " +
                " "+INVENTORY_PRICE_1+" INTEGER, " +
                " "+INVENTORY_NAME_2+" STRING, " +
                " "+INVENTORY_DESC_3+" STRING, " +
                " "+INVENTORY_LVLREQ_4+" INTEGER, " +
                " "+ INVENTORY_ITEMCOUNT_5 +" INTEGER" +
                ")");


        db.execSQL("INSERT INTO PLAYER VALUES (" +
                "1,20, 1, 0, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "0,1, 'Fir Tree', 'Group of small fir trees'," +
                "1, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "1,1, 'Pinetree', 'Tall pinetree'," +
                "1, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "2,2, 'Oaktree', 'Large oaktree'," +
                "1, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "3,3, 'Treehouse', 'House for the childlike'," +
                "1, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "4,3, 'Thatched', 'House with a thatched roof'," +
                "5, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "5,4, 'House', 'Cozy normal house'," +
                "2, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "6,5, 'Inn', 'Place for the travellers'," +
                "3, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "7,5, 'Tavern', 'Standard drinking house'," +
                "5, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "8,5, 'Villa', 'Villa for the noble'," +
                "6, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "9,5, 'Clock', 'Clock tower'," +
                "7, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "10,5, 'Chapel', 'Holy smokes'," +
                "8, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "11,2, 'Fish stand', 'Marketplace'," +
                "1, 0)");
        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "12,3, 'Fruit stand', 'Marketplace'," +
                "2, 0)");

        db.execSQL("INSERT INTO INVENTORY VALUES (" +
                "13,4, 'Tailor', 'Marketplace'," +
                "6, 0)");




    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  //Recreate the database on version update
        Log.d("d", "Drop table executed");
        db.execSQL("drop table if exists " + PLAYER_TABLE);
        db.execSQL("drop table if exists " + INVENTORY_TABLE);
        onCreate(db);
    }





    public Cursor getInventoryItems(){                       //Method to get all data from the inventory table
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor inventory = db.rawQuery("SELECT * FROM "+INVENTORY_TABLE, null);
        return inventory;
    }
    public Cursor getPlayerData(){                          //Method to get all data from the player table
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor player = db.rawQuery("SELECT * FROM "+PLAYER_TABLE, null);
        return player;
    }


    public boolean updateCurrentSteps(int count){             //Method to update steps to the player table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PLAYER_STEPS_3, String.valueOf(count));
        db.update(PLAYER_TABLE, cv, PLAYER_ID_0+"="+1, null);
        return true;
    }

    public boolean updateTotalSteps(int count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PLAYER_TOTALSTEPS_4, String.valueOf(count));
        db.update(PLAYER_TABLE, cv, PLAYER_ID_0+"="+1, null);
        return true;
    }

    public boolean updateOnLevelUp(int count){             //Method to update level to the player table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PLAYER_LVL_2, String.valueOf(count));
        db.update(PLAYER_TABLE, cv, PLAYER_ID_0+"="+1, null);
        return true;
    }

    public boolean updateCoins(int count){             //Method to update coins to the player table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PLAYER_COINS_1, String.valueOf(count));
        db.update(PLAYER_TABLE, cv, PLAYER_ID_0+"="+1, null);
        return true;
    }


    public boolean updateItemCount(String id, int count){     //Update when you buy an item. Item can be bought if item_count < 1
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(INVENTORY_ITEMCOUNT_5, String.valueOf(count));
        db.update(INVENTORY_TABLE, cv, INVENTORY_ID_0+"="+id, null);
        return true;
    }

}

