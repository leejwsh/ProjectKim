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
	
	// Constants for OpenGL.
	public static final int BACKGROUND_LAYER_ONE = R.drawable.background;
	public static float SCROLL_BACKGROUND_1 = 0.002f;
	
	// Constants for player sprite.
	public static final int PLAYER_SPRITE = R.drawable.spritesheet_treasurehunter_temp;
	public static final int PLAYER_STATIONARY = 5;
	public static final int PLAYER_LEFT = 4;
	public static final int PLAYER_RIGHT = 6;
	public static final int PLAYER_UP = 8;
	public static final int PLAYER_DOWN = 2;
	public static final int PLAYER_FRAMES_BETWEEN_ANI = 12;
	
	// Variables for audio.
	public static MediaPlayer player;
	
	// Variables for player sprite.
	public static int playerWalkAction = 0;
	
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
