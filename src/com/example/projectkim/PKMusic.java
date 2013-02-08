package com.example.projectkim;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PKMusic extends Service
{
	public static boolean isRunning = false;
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		setMusicOptions(this, PKEngine.LOOP_BACKGROUND_MUSIC, PKEngine.R_VOLUME, PKEngine.L_VOLUME, PKEngine.SPLASH_SCREEN_MUSIC);
	}
	
	public void setMusicOptions(Context context, boolean isLooped, int rVolume, int lVolume, int soundFile)
	{
		PKEngine.player = MediaPlayer.create(context, soundFile);
		PKEngine.player.setLooping(isLooped);
		PKEngine.player.setVolume(lVolume, rVolume);
	}
	
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		try
		{
			PKEngine.player.start();
			isRunning = true;
		}
		catch (Exception e)
		{
			isRunning = false;
			PKEngine.player.stop();
		}
		return 1;
	}
	
	public void onStop()
	{
		isRunning = false;
	}
	
	public IBinder onUnBind(Intent arg0)
	{
		return null;
	}
	
	public void onPause()
	{
		
	}
	
	@Override
	public void onDestroy()
	{
		PKEngine.player.stop();
		PKEngine.player.release();
	}
	
	@Override
	public void onLowMemory()
	{
		PKEngine.player.stop();
	}
}
