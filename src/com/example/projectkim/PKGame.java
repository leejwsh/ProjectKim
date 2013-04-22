package com.example.projectkim;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

public class PKGame extends Activity
{
	private PKGameView gameView;
	private PKGameRenderer renderer;
	private TableLayout playerSelectionInput, keypad, keypadInput;
	private Button addKey;
	private ImageButton p1, p2, p3, p4, num0, num1, num2, num3, num4, num5, num6, num7, num8, num9;
	private ImageButton[] players = new ImageButton[4];
	private ImageButton[] keyInput = new ImageButton[4];
	private int[] numbers = new int[10];
	private int[] playersSelected = new int[4];
	private String keyCode = "";
	private int currentKeyPos;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		PKEngine.gameEnd = false;
		
		new Connection().execute();
		renderer = new PKGameRenderer(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.game);
		gameView = (PKGameView)findViewById(R.id.PKGameView);
		gameView.setRenderer(renderer);
		new Login().execute();
		
		Button openChest = (Button)findViewById(R.id.btnOpenChest);
		openChest.setVisibility(View.VISIBLE);
		openChest.setBackgroundColor(Color.TRANSPARENT);
		
		Button startMiniGame = (Button)findViewById(R.id.enterMiniGame);
		startMiniGame.setVisibility(View.VISIBLE);
		startMiniGame.setBackgroundColor(Color.TRANSPARENT);
		
		addKey = (Button)findViewById(R.id.btnKey);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		params.leftMargin = 0;
		params.rightMargin = (int) (PKEngine.scrWidth * 0.82f);
		params.topMargin = (int) (PKEngine.scrHeight * 0.78f);
		params.bottomMargin = (int) (PKEngine.scrHeight * 0.07f);
		addKey.setLayoutParams(params);
		addKey.setVisibility(View.VISIBLE);
		addKey.setBackgroundColor(Color.TRANSPARENT);
		
		playerSelectionInput = (TableLayout)findViewById(R.id.playerSelect);
		p1 = (ImageButton)findViewById(R.id.player1);
		p2 = (ImageButton)findViewById(R.id.player2);
		p3 = (ImageButton)findViewById(R.id.player3);
		p4 = (ImageButton)findViewById(R.id.player4);
		players[0] = p1;
		players[1] = p2;
		players[2] = p3;
		players[3] = p4;
		
		playersSelected[0] = R.drawable.player1;
		playersSelected[1] = R.drawable.player2;
		playersSelected[2] = R.drawable.player3;
		playersSelected[3] = R.drawable.player4;
		
		keypad = (TableLayout)findViewById(R.id.keypad);
		keypadInput = (TableLayout)findViewById(R.id.keypadInput);
		num0 = (ImageButton)findViewById(R.id.num0);
		num1 = (ImageButton)findViewById(R.id.num1);
		num2 = (ImageButton)findViewById(R.id.num2);
		num3 = (ImageButton)findViewById(R.id.num3);
		num4 = (ImageButton)findViewById(R.id.num4);
		num5 = (ImageButton)findViewById(R.id.num5);
		num6 = (ImageButton)findViewById(R.id.num6);
		num7 = (ImageButton)findViewById(R.id.num7);
		num8 = (ImageButton)findViewById(R.id.num8);
		num9 = (ImageButton)findViewById(R.id.num9);
		numbers[0] = R.drawable.num0;
		numbers[1] = R.drawable.num1;
		numbers[2] = R.drawable.num2;
		numbers[3] = R.drawable.num3;
		numbers[4] = R.drawable.num4;
		numbers[5] = R.drawable.num5;
		numbers[6] = R.drawable.num6;
		numbers[7] = R.drawable.num7;
		numbers[8] = R.drawable.num8;
		numbers[9] = R.drawable.num9;
		keyInput[0] = (ImageButton)findViewById(R.id.blank0);
		keyInput[1] = (ImageButton)findViewById(R.id.blank1);
		keyInput[2] = (ImageButton)findViewById(R.id.blank2);
		keyInput[3] = (ImageButton)findViewById(R.id.blank3);
		ImageButton numDel = (ImageButton)findViewById(R.id.numDel);
		ImageButton numEnter = (ImageButton)findViewById(R.id.numEnter);
		currentKeyPos = 0;
	}

	public void buttonOnClick(View v) throws Exception
	{
		switch (v.getId())
		{
			case R.id.btnOpenChest:
				if (renderer.getCurrentEvent() != 1)
				{
					//String chestReply = renderer.openChest(); // testing purpose
					renderer.openChest();
				}
				// For testing purposes.
				/*if (chestReply.equalsIgnoreCase("Successful"))
				{
					Toast msg = Toast.makeText(PKGame.this, "Successfully Opened!", Toast.LENGTH_SHORT);
					msg.show();
				} else if (chestReply.equalsIgnoreCase("NoChest"))
				{
					Toast msg = Toast.makeText(PKGame.this, "Are you sure there's a chest here?", Toast.LENGTH_SHORT);
					msg.show();
				} else
				{
					Toast msg = Toast.makeText(PKGame.this, "You have no key. Go look for one!", Toast.LENGTH_SHORT);
					msg.show();
				}*/
				break;
			case R.id.player1:
				PKEngine.playerID = 1;
				//renderer.setPlayerLogOn();
				playerSelectionInput.setVisibility(View.INVISIBLE);
				break;
			case R.id.player2:
				PKEngine.playerID = 2;
				//renderer.setPlayerLogOn();
				playerSelectionInput.setVisibility(View.INVISIBLE);
				break;
			case R.id.player3:
				PKEngine.playerID = 3;
				//renderer.setPlayerLogOn();
				playerSelectionInput.setVisibility(View.INVISIBLE);
				break;
			case R.id.player4:
				PKEngine.playerID = 4;
				//renderer.setPlayerLogOn();
				playerSelectionInput.setVisibility(View.INVISIBLE);
				break;
			case R.id.btnKey:
				if (renderer.getCurrentEvent() != 1)
				{
					if (keypad.getVisibility() == View.INVISIBLE)
					{
						keypad.setVisibility(View.VISIBLE);
						keypadInput.setVisibility(View.VISIBLE);
					}
					else
					{
						keypad.setVisibility(View.INVISIBLE);
						keypadInput.setVisibility(View.INVISIBLE);
						resetKeyCode();
						currentKeyPos = 0;
					}
				}
				break;
			case R.id.enterMiniGame:
				renderer.checkMiniGame();
				break;
			case R.id.num0:
				addNumber(0,currentKeyPos);
				if (currentKeyPos < 4)
					currentKeyPos++;
				break;
			case R.id.num1:
				addNumber(1,currentKeyPos);
				if (currentKeyPos < 4)
					currentKeyPos++;
				break;
			case R.id.num2:
				addNumber(2,currentKeyPos);
				if (currentKeyPos < 4)
					currentKeyPos++;
				break;
			case R.id.num3:
				addNumber(3,currentKeyPos);
				if (currentKeyPos < 4)
					currentKeyPos++;
				break;
			case R.id.num4:
				addNumber(4,currentKeyPos);
				if (currentKeyPos < 4)
					currentKeyPos++;
				break;
			case R.id.num5:
				addNumber(5,currentKeyPos);
				if (currentKeyPos < 4)
					currentKeyPos++;
				break;
			case R.id.num6:
				addNumber(6,currentKeyPos);
				if (currentKeyPos < 4)
					currentKeyPos++;
				break;
			case R.id.num7:
				addNumber(7,currentKeyPos);
				if (currentKeyPos < 4)
					currentKeyPos++;
				break;
			case R.id.num8:
				addNumber(8,currentKeyPos);
				if (currentKeyPos < 4)
					currentKeyPos++;
				break;
			case R.id.num9:
				addNumber(9,currentKeyPos);
				if (currentKeyPos < 4)
					currentKeyPos++;
				break;
			case R.id.numDel:
				if (currentKeyPos > 0)
				{
					deleteNumber(currentKeyPos-1);
					currentKeyPos--;
				}
				break;
			case R.id.numEnter:
				if (!keyCode.equalsIgnoreCase(""))
				{
					String keyReply = renderer.verifyKey(Integer.valueOf(keyCode));
					if (keyReply.equalsIgnoreCase("Successful"))
					{
						// For testing purposes.
						/*Toast msg = Toast.makeText(PKGame.this, "+1 key!", Toast.LENGTH_SHORT);
						msg.show();*/
						keypad.setVisibility(View.INVISIBLE);
						keypadInput.setVisibility(View.INVISIBLE);
						resetKeyCode();
						currentKeyPos = 0;
					} else
					{
						// For testing purposes.
						/*Toast msg = Toast.makeText(PKGame.this, "Keycode " + keyCode + " does not exist", Toast.LENGTH_SHORT);
						msg.show();*/
					}
				} else
				{
					// For testing purposes.
					/*Toast msg = Toast.makeText(PKGame.this, "No code entered!", Toast.LENGTH_SHORT);
					msg.show();*/
				}
				break;
		}	
	}
	
	private void resetKeyCode()
	{
		for (int i=0; i<keyInput.length; i++)
		{
			keyInput[i].setImageResource(R.drawable.numblank);
		}
		keyCode = "";
	}

	private void deleteNumber(int currentKeyPos)
	{
		keyInput[currentKeyPos].setImageResource(R.drawable.numblank);
		keyCode = keyCode.substring(0, currentKeyPos);
		System.out.println("KeyCode: " + keyCode);
	}

	private void addNumber(int num, int currentKeyPos)
	{
		if (currentKeyPos < 4){
			keyInput[currentKeyPos].setImageResource(numbers[num]);
			keyCode = keyCode.concat(String.valueOf(num));
			System.out.println("KeyCode: " + keyCode);
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
				PKEngine.client = new PKGameClient();
				PKEngine.isConnected = true;
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
	
	private class Login extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... arg0)
		{
			while (renderer.getCurrentEvent() <= 1 && !renderer.getPlayerLogOn())
			{
				if (renderer.getCurrentEvent() == 1)
					setTableVisibility(View.VISIBLE);	
				for (int i = 0; i < PKEngine.totalPlayers; i++)
				{
					if (PKEngine.client.checkPlayerLogonStatus(i+1))
					{
						setSelectionFade(i, 0.5f);
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			setTableVisibility(View.INVISIBLE);
			super.onPostExecute(result);
		}
	}
	
	private void setTableVisibility(final int visible) {
		playerSelectionInput.getHandler().post(new Runnable() {
		    public void run() {
		        playerSelectionInput.setVisibility(visible);
		    }
		});
	}
	
	public void setSelectionFade(final int player, final float alpha) {
		players[player].getHandler().post(new Runnable() {
		    public void run() {
		        //players[player].setAlpha(alpha);
		    	players[player].setBackgroundResource(playersSelected[player]);
		        players[player].setEnabled(false);
		    }
		});
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
		if (PKEngine.gameEnd)
		{
			PKGame.super.onBackPressed();
		}
		
		// For testing purposes.
		/*float x = event.getX();
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
		}*/
		
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
