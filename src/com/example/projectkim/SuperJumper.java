package com.example.projectkim;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Intent;
import android.os.AsyncTask;

import com.example.framework.Screen;
import com.example.framework.impl.GLGame;

public class SuperJumper extends GLGame {
    boolean firstTimeCreate = true;
    int timer = PKEngine.client.getCurrentMiniGameTime();
    
    public Screen getStartScreen() {
        return new GameScreen(this);
    }
    
    public void killGame() {
    	SuperJumper.this.finish();
    }
    
    public int getTime(){
    	return timer;
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {         
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Settings.load(getFileIO());
            Assets.load(this);
            firstTimeCreate = false;            
        } else {
            Assets.reload();
        }
        new Game().execute();
    }
    
    private class Game extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... arg0)
		{
			while(PKEngine.client.getGlobalEventStatus() == 3)
			{
				try {
					PKEngine.client.mapUpdateEvent(PKEngine.PLAYER_ID);
					timer = PKEngine.client.getCurrentMiniGameTime();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}
    
    @Override
    public void onPause() {
        super.onPause();
        if(Settings.soundEnabled)
            Assets.music.pause();
    }
}