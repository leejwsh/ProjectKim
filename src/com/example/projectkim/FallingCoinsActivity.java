package com.example.projectkim;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.os.AsyncTask;

import com.example.framework.Screen;
import com.example.framework.impl.GLGame;

public class FallingCoinsActivity extends GLGame
{
    boolean firstTimeCreate = true;
    int timer = PKEngine.client.getCurrentMiniGameTime();
    
    public Screen getStartScreen()
    {
        return new FCGameScreen(this);
    }
    
    public void killGame()
    {
    	FallingCoinsActivity.this.finish();
    }
    
    public int getTime()
    {
    	return timer;
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config)
    {         
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate)
        {
            FCSettings.load(getFileIO());
            FCAssets.load(this);
            firstTimeCreate = false;            
        } else
        {
            FCAssets.reload();
        }
        new Timer().execute();
    }
    
    private class Timer extends AsyncTask<String, Void, String>
    {
		@Override
		protected String doInBackground(String... arg0)
		{
			while(PKEngine.client.getGlobalEventStatus() == 3)
			{
				try
				{
					PKEngine.client.mapUpdateEvent(PKEngine.PLAYER_ID);
					timer = PKEngine.client.getCurrentMiniGameTime();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			return null;
		}
	}
    
    @Override
    public void onPause()
    {
        super.onPause();
        if (FCSettings.soundEnabled)
            FCAssets.music.pause();
    }
}