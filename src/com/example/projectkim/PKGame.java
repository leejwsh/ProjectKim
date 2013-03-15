package com.example.projectkim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

public class PKGame extends Activity
{
	private PKGameView gameView;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		gameView = new PKGameView(this);
		PKGameRenderer renderer = new PKGameRenderer();
		new Connection().execute();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(gameView);
		gameView.setRenderer(renderer);
	}
	
	private class Connection extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... arg0)
		{
			try
			{
				System.out.print("Establishing connection... ");
				PKEngine.client = new GameClient();
				System.out.println("Connected.");
			}
			catch (Exception e)
			{
				System.out.println(e);
				e.printStackTrace();
			}
			return null;
		}
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
		if (x > 100 && x < 160 && y > 100 && y < 160)
		{
			// Treasure key.
			PKEngine.treasureKeyEvent = true;
		}
		
		return false;
	}

	@Override
	public void onBackPressed()
	{
		new AlertDialog.Builder(this)
		.setInverseBackgroundForced(true)
		.setTitle("Quit Game?")
		.setMessage("Your progress will not be saved. Are you sure you want to return to main menu?")
		.setNegativeButton(android.R.string.no, null)
		.setPositiveButton(android.R.string.yes, new OnClickListener()
		{
			public void onClick(DialogInterface arg0, int arg1)
			{
				PKGame.super.onBackPressed();
			}
		}).create().show();
	}
}
