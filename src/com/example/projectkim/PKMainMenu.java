package com.example.projectkim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

public class PKMainMenu extends Activity
{
	private PKEngine engine;
	private boolean gameStarted;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Display main menu.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_project_kim);
		
		// Fire up background music.
		PKEngine.musicThread = new Thread()
		{
			public void run()
			{
				Intent bgmusic = new Intent(getApplicationContext(), PKMusic.class);
				startService(bgmusic);
				PKEngine.context = getApplicationContext();
			}
		};
		PKEngine.musicThread.start();
		
		engine = new PKEngine();
		gameStarted = false;
		
		// Set menu button options.
		//ImageButton start = (ImageButton)findViewById(R.id.btnStart);
		//ImageButton settings = (ImageButton)findViewById(R.id.btnSettings);
		//ImageButton exit = (ImageButton)findViewById(R.id.btnExit);
		
		//start.getBackground().setAlpha(PKEngine.MENU_BUTTON_ALPHA);
		//start.setHapticFeedbackEnabled(PKEngine.HAPTIC_BUTTON_FEEDBACK);
		
		//settings.getBackground().setAlpha(PKEngine.MENU_BUTTON_ALPHA);
		//settings.setHapticFeedbackEnabled(PKEngine.HAPTIC_BUTTON_FEEDBACK);
		
		//exit.getBackground().setAlpha(PKEngine.MENU_BUTTON_ALPHA);
		//exit.setHapticFeedbackEnabled(PKEngine.HAPTIC_BUTTON_FEEDBACK);
		
		/*
		start.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Start game.
				Intent game = new Intent(getApplicationContext(), PKGame.class);
				PKMainMenu.this.startActivity(game);
			}
		});
		*/
		
		/*
		settings.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Open settings window.
			}
		});
		*/
		
		/*
		exit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Exit game.
				boolean clean = false;
				clean = engine.onExit(v);
				if (clean)
				{
					int pid = android.os.Process.myPid();
					android.os.Process.killProcess(pid);
				}
			}
		});
		*/
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// Start game.
		if (!gameStarted)
		{
			Intent game = new Intent(getApplicationContext(), PKGame.class);
			PKMainMenu.this.startActivity(game);
			gameStarted = true;
		}
		
		return super.onTouchEvent(event);
	}

	@Override
	protected void onPause()
	{
		if (PKEngine.player != null) PKEngine.player.pause();
		
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		if (PKEngine.player != null) PKEngine.player.start();
		gameStarted = false;
		
		super.onResume();
	}

	@Override
	public void onBackPressed()
	{
		// Exit game.
		boolean clean = false;
		clean = engine.onExit();
		if (clean)
		{
			int pid = android.os.Process.myPid();
			android.os.Process.killProcess(pid);
		}
		
		super.onBackPressed();
	}
}
