package com.example.projectkim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class PKGame extends Activity
{
	private PKGameView gameView;
	private PKGameRenderer renderer;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		new Connection().execute();
		renderer = new PKGameRenderer();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game);
		gameView = (PKGameView)findViewById(R.id.PKGameView);
		gameView.setRenderer(renderer);
		
		Button openChest = (Button)findViewById(R.id.btnOpenChest);
		openChest.setVisibility(View.VISIBLE);
		openChest.setBackgroundColor(Color.TRANSPARENT);
	}
	
	public void buttonOnClick(View v) throws Exception
	{
		// Dun delete this [Can use this way next time if there is more buttons]
//		switch (v.getId()) {
//	      case R.id.button1:
//	        doSomething1();
//	        break;
//	      case R.id.button2:
//	        doSomething2();
//	        break;
//	      }
		boolean hasChest = renderer.openChest();
		if (hasChest)
		{
			// Testing Message
			Toast msg = Toast.makeText(PKGame.this, "Successfully Opened!", Toast.LENGTH_SHORT);
			msg.show();
		} else
		{
			// Testing Message
			Toast msg = Toast.makeText(PKGame.this, "Are you sure there's a chest here?", Toast.LENGTH_SHORT);
			msg.show();
		}
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
