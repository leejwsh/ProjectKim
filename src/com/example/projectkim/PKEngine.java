package com.example.projectkim;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;

public class PKEngine
{
	// Constants for threads.
	public static final int GAME_THREAD_DELAY = 1000;
	public static final int GAME_THREAD_FPS_SLEEP = (1000/60);
	public static volatile Thread musicThread;
	
	// Constants for buttons.
	public static final int MENU_BUTTON_ALPHA = 0;
	public static final boolean HAPTIC_BUTTON_FEEDBACK = true;
	
	// Constants for audio.
	public static final int SPLASH_SCREEN_MUSIC = R.raw.loop;
	public static final int R_VOLUME = 100;
	public static final int L_VOLUME = 100;
	public static final boolean LOOP_BACKGROUND_MUSIC = true;
	public static Context context;
	
	// Constants for background.
	public static final int BACKGROUND_LAYER_ONE = R.drawable.background;
	
	// Constants for PoV map.
	public static final int POV_MAP = R.drawable.povmap;
	public static final int TREASURE_CHEST = R.drawable.btn_chest;
	public static final int POV_MAP_WIDTH = 3;
	public static final int POV_MAP_HEIGHT = 3;
	public static final float POV_MAP_TEXTURE[] = { 0.0f, 1.0f - 3.0f / (POV_MAP_HEIGHT + 2),
													3.0f / (POV_MAP_WIDTH + 2), 1.0f - 3.0f / (POV_MAP_HEIGHT + 2),
													3.0f / (POV_MAP_WIDTH + 2), 1.0f,
													0.0f, 1.0f };
	
	// Constants for player sprite.
	public static final int PLAYER_SPRITE = R.drawable.spritesheet_treasurehunter_temp;
	public static final float PLAYER_TEXTURE[] = { 0.0f, 0.0f, 0.167f, 0.0f, 0.167f, 0.167f, 0.0f, 0.167f };
	public static final float SPRITE_STATIONARY_1[] = { 0.333f, 0.833f };
	public static final float SPRITE_STATIONARY_2[] = { 0.667f, 0.333f };
	public static final float SPRITE_LEFT_1[] = { 0.167f, 0.5f };
	public static final float SPRITE_LEFT_2[] = { 0.0f, 0.5f };
	public static final float SPRITE_RIGHT_1[] = { 0.667f, 0.667f };
	public static final float SPRITE_RIGHT_2[] = { 0.833f, 0.833f };
	public static final float SPRITE_UP_1[] = { 0.5f, 0.667f };
	public static final float SPRITE_UP_2[] = { 0.333f, 0.667f };
	public static final float SPRITE_DOWN_1[] = { 0.0f, 0.333f };
	public static final float SPRITE_DOWN_2[] = { 0.5f, 0.333f };
	public static final int PLAYER_STATIONARY = 5;
	public static final int PLAYER_LEFT = 4;
	public static final int PLAYER_RIGHT = 6;
	public static final int PLAYER_UP = 8;
	public static final int PLAYER_DOWN = 2;
	public static final int SPRITE_TIME_BETWEEN_ANI = 250;
	public static final int SPRITE_TIME_BETWEEN_STATIONARY_ANI = 1500;
	public static final int TOTAL_ANIMATION_TIME = 1000;
	
	// Constants for main game UI.
	public static final int TREASURE_KEY = R.drawable.btn_key;
	public static final int MINI_MAP = R.drawable.minimap;
	
	// Variables for screen size.
	public static int scrHeight;
	public static int scrWidth;
	
	// Variables for audio.
	public static MediaPlayer player;
	
	// Variables for main game UI.
	public static int playerWalkAction = 0;
	public static boolean treasureKeyEvent = false;
	public static int[][] treasureLocations = new int[POV_MAP_HEIGHT][POV_MAP_WIDTH];
	
	// Kill game and exit.
	public boolean onExit(View v)
	{
		try
		{
			Intent bgmusic = new Intent(context, PKMusic.class);
			context.stopService(bgmusic);
			musicThread = null;
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
