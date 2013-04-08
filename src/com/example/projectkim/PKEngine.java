package com.example.projectkim;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class PKEngine
{
	// Constants for connection to server.
	public static final int PLAYER_ID = 1;
	
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
	public static final int POV_MAP = R.drawable.map2;
	public static final int TREASURE_CHEST_CLOSED = R.drawable.chest;
	public static final int ADD_GOLD = R.drawable.chest_gold;
	public static final int ADD_GOLD_WIDTH = 75;
	public static final int ADD_GOLD_HEIGHT = 50;
	public static final int POV_MAP_WIDTH = 14;
	public static final int POV_MAP_HEIGHT = 7;
	public static final float POV_MAP_TEXTURE[] = { 0.0f, 1.0f - 3.0f / (POV_MAP_HEIGHT + 2),
													3.0f / (POV_MAP_WIDTH + 2), 1.0f - 3.0f / (POV_MAP_HEIGHT + 2),
													3.0f / (POV_MAP_WIDTH + 2), 1.0f,
													0.0f, 1.0f };
	
	// Constants for player sprite.
	public static final int PLAYER_SPRITE = R.drawable.spritesheet_player;
	public static final float PLAYER_TEXTURE[] = { 0.0f, 0.0f, 0.25f, 0.0f, 0.25f, 0.25f, 0.0f, 0.25f };
	public static final float SPRITE_STATIONARY_1[] = { 0.25f, 0.75f };
	public static final float SPRITE_STATIONARY_2[] = { 0.75f, 0.75f };
	public static final float SPRITE_LEFT_1[] = { 0.0f, 0.0f };
	public static final float SPRITE_LEFT_2[] = { 0.5f, 0.0f };
	public static final float SPRITE_RIGHT_1[] = { 0.0f, 0.25f };
	public static final float SPRITE_RIGHT_2[] = { 0.5f, 0.25f };
	public static final float SPRITE_UP_1[] = { 0.0f, 0.5f };
	public static final float SPRITE_UP_2[] = { 0.5f, 0.5f };
	public static final float SPRITE_DOWN_1[] = { 0.0f, 0.75f };
	public static final float SPRITE_DOWN_2[] = { 0.5f, 0.75f };
	public static final int PLAYER_STATIONARY = 5;
	public static final int PLAYER_LEFT = 4;
	public static final int PLAYER_RIGHT = 6;
	public static final int PLAYER_UP = 8;
	public static final int PLAYER_DOWN = 2;
	public static final int SPRITE_TIME_BETWEEN_ANI = 250;
	public static final int SPRITE_TIME_BETWEEN_STATIONARY_ANI = 1500;
	public static final int TOTAL_ANIMATION_TIME = 1000;
	
	// Constants for main game UI.
	public static final int OVERLAY_TOP = R.drawable.overlay_top;
	public static final int OVERLAY_TOP_WIDTH = 800;
	public static final int OVERLAY_TOP_HEIGHT = 360;
	public static final int OVERLAY_BTM = R.drawable.overlay;
	public static final int TREASURE_KEY = R.drawable.ingame_statskeyicon;
	public static final int TREASURE_KEY_WIDTH = 59;
	public static final int TREASURE_KEY_HEIGHT = 63;
	public static final int GOLD_COIN = R.drawable.ingame_statscoinicon;
	public static final int GOLD_COIN_WIDTH = 66;
	public static final int GOLD_COIN_HEIGHT = 52;
	public static final int MASCOT = R.drawable.mascot;
	public static final int MASCOT_WIDTH = 800;
	public static final int MASCOT_HEIGHT = 360;
	public static final int MASCOT_OPEN_CHEST_SUCCESS = R.drawable.mascot_open_success;
	public static final int MASCOT_NO_CHEST = R.drawable.mascot_nochest;
	public static final int MASCOT_NO_KEY = R.drawable.mascot_nokey;
	public static final int MASCOT_INVALID_KEY = R.drawable.mascot_wrongkey;
	public static final int MASCOT_CORRECT_KEY = R.drawable.mascot_correctkey;
	public static final int MASCOT_LEARN_SR1 = R.drawable.mascot_know_sr1;
	public static final int MASCOT_LEARN_STUD_LOUNGE = R.drawable.mascot_know_lounge;
	public static final int MASCOT_LEARN_HCI = R.drawable.mascot_know_hci;
	public static final int MASCOT_LEARN_SMALL_SR = R.drawable.mascot_know_smallsr;
	
	// Constants for mini map.
	public static final int MINI_MAP = R.drawable.map3;
	public static final int MINI_MAP_WIDTH = 1600;
	public static final int MINI_MAP_HEIGHT = 900;
	public static final float MINI_MAP_SCALE = 0.63f;
	public static final float MINI_MAP_X_OFFSET = -0.05f; // Translate mini map right by OFFSET.
	public static final float MINI_MAP_Y_OFFSET = 0.01f; // Translate mini map up by OFFSET.
	public static final float MINI_MAP_GRID_SIZE = MINI_MAP_SCALE / (POV_MAP_WIDTH + 2);
	public static final int MINI_MAP_MARKER = R.drawable.minimap_cross;
	public static final int MINI_MAP_MARKER_WIDTH = 100;
	public static final int MINI_MAP_MARKER_HEIGHT = 100;
	
	// Variables for connection to server.
	public static GameClient client;
	public static boolean isConnected = false;
	
	// Variables for screen size.
	public static int scrHeight;
	public static int scrWidth;
	
	// Variables for audio.
	public static MediaPlayer player;
	
	// Variables for main game UI.
	public static int playerWalkAction = 0;
	public static boolean treasureKeyEvent = false;
	public static int[][] treasureLocations = new int[POV_MAP_HEIGHT][POV_MAP_WIDTH];
	public static ArrayList<PKImage> mascotImages = new ArrayList<PKImage>();
	
	// Variables for mini game.
	public static boolean startMiniGame = false;
	public static boolean miniGameIsRunning = false;
	
	// Kill game and exit.
	public boolean onExit()
	{
		try
		{
			Intent bgmusic = new Intent(context, PKMusic.class);
			context.stopService(bgmusic);
			if (isConnected)
				client.closeSocket();
			musicThread = null;
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
}
