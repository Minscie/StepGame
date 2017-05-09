# Step Game
---------
##### Author: Sami Hurmerinta
##### Technologies: Java, Android
##### Summary: A game demonstration created during Android Application Development T100AB76-3014 course in Metropolia Leppävaara  
##### Source: https://github.com/minscie/stepgame

## Prerequisite

##### Android Studio 2.3.1 or newer
##### Minimum SDK version: 23 or newer
##### Build tools version 25.0.0 or newer
##### SDK verson lower than the 23 has not been tested and the development was done mainly on a high end phone.

## How it works?

- This application counts your steps while showing the progress in realtime. By default you reach levels from each steps == (1000 * level) taken.
- You get a coin as a reward from each level. An animated coin will eventually appear in the COINS view to be collected.
- With the coins you're able to purchase items from the SHOP view. Only one item each in this demo.
- When you buy an item, the item will appear as an imageview in the top left corner of the WORLD view. You can freely change the location of the items and build your own village.
- By touching the PROGRESSBAR you're able to see your stats like level, coins and total steps taken.

There is 1 main activity, which holds the constant buttons and the changeable fragments WORLD, SHOP and COINS. Clickable bottom progressbar is also a fragment in the main activity which opens resized activity as a pop-up.

For various reasons the game was set to be played only in a portrait mode. To immerse the player into the game, all activites are in fullscreen mode, hiding the default android navigation and top bar.


## SQLite database layout

![database image](https://github.com/Minscie/StepGame/blob/master/Game_Database.PNG)
>>>>>>> origin/master

=======

## Changes after the presentation
- Fixed one bug. Firtree was visible at start
- Modified the level modifier from 100 to 1000. To level up 10x slower
- Modified the SQLite database starting coins to 20 so you can't buy everything at start