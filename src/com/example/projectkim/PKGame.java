package com.example.projectkim;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Window;

public class PKGame extends Activity
{
	private PKGameView gameView;
	private DisplayMetrics displaymetrics;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		gameView = new PKGameView(this);
		PKGameRenderer renderer = new PKGameRenderer();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(gameView);
		gameView.setRenderer(renderer);
		
		displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		if (PKEngine.player != null) PKEngine.player.pause();
		gameView.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if (PKEngine.player != null) PKEngine.player.start();
		gameView.onResume();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x = event.getX();
		float y = event.getY();
		//int height = displaymetrics.heightPixels;
		//int width = displaymetrics.widthPixels;
		
		if (y < 100)
		{
			// Up.
			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					PKEngine.playerWalkAction = PKEngine.PLAYER_UP;
					break;
				case MotionEvent.ACTION_UP:
					PKEngine.playerWalkAction = PKEngine.PLAYER_STATIONARY;
					break;
			}
		}
		if (y > 700)
		{
			// Down.
			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					PKEngine.playerWalkAction = PKEngine.PLAYER_DOWN;
					break;
				case MotionEvent.ACTION_UP:
					PKEngine.playerWalkAction = PKEngine.PLAYER_STATIONARY;
					break;
			}
		}
		if (x < 100)
		{
			// Left.
			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					PKEngine.playerWalkAction = PKEngine.PLAYER_LEFT;
					break;
				case MotionEvent.ACTION_UP:
					PKEngine.playerWalkAction = PKEngine.PLAYER_STATIONARY;
					break;
			}
		}
		if (x > 300)
		{
			// Right.
			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					PKEngine.playerWalkAction = PKEngine.PLAYER_RIGHT;
					break;
				case MotionEvent.ACTION_UP:
					PKEngine.playerWalkAction = PKEngine.PLAYER_STATIONARY;
					break;
			}
		}
		
		return false;
	}
}
