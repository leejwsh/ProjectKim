package com.example.projectkim;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;

public class PKEngine
{
	// Constants for threads.
	public static final int GAME_THREAD_DELAY = 1000;
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
	
	// Variables for audio.
	public static MediaPlayer player;
	
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
