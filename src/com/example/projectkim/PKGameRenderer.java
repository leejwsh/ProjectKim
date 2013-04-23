package com.example.projectkim;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView.Renderer;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TableLayout;

public class PKGameRenderer implements Renderer
{
	// Variables for text.
	private PKTexFont font;//, timerFont;
	
	// Variables for background.
	private PKImage background = new PKImage();
	
	// Variables for PoV map.
	private PKImage povMap = new PKImage(PKEngine.POV_MAP_TEXTURE);
	private PKImage treasureChest = new PKImage();
	private PKImage addGold = new PKImage(0.2f, 0.2f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.ADD_GOLD_HEIGHT / PKEngine.ADD_GOLD_WIDTH, 1.0f, 1.0f);
	private PKImage player = new PKImage(PKEngine.PLAYER_TEXTURE);
	private int playerPosition = 0;
	private int globalEventPosition = 49;
	private ArrayList<Integer> playerNewPos = new ArrayList<Integer>();
	private boolean animStart = false;
	private boolean addChestScore = false;
	private int animRunTime = 0;
	private float[] povMapCoords = new float[2];
	private float[] treasureChestOffset = new float[2];
	private float[] spriteCoords = new float[2];
	private String checkKeyReply;
	private String checkChestReply;
	private int playerScore;
	private int numKeys;
	
	// Variables for rest of the elements of UI.
	private PKImage overlayTop = new PKImage(1.0f, 0.6f * (PKEngine.scrHeight - PKEngine.scrWidth) / PKEngine.scrHeight, 1.0f, 1.0f);
	private PKImage overlayBtm = new PKImage();
	private PKImage treasureKey = new PKImage(0.08f, 0.08f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.TREASURE_KEY_HEIGHT / PKEngine.TREASURE_KEY_WIDTH, 1.0f, 1.0f);
	private PKImage goldCoin = new PKImage(0.08f, 0.08f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.GOLD_COIN_HEIGHT / PKEngine.GOLD_COIN_WIDTH, 1.0f, 1.0f);
	private PKImage miniMap = new PKImage(PKEngine.MINI_MAP_SCALE, PKEngine.MINI_MAP_SCALE * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MINI_MAP_HEIGHT / PKEngine.MINI_MAP_WIDTH, 1.0f, 1.0f);
	private PKImage miniMapMarker = new PKImage(PKEngine.MINI_MAP_GRID_SIZE, PKEngine.MINI_MAP_GRID_SIZE * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MINI_MAP_MARKER_HEIGHT / PKEngine.MINI_MAP_MARKER_WIDTH, 1.0f, 1.0f);
	private PKImage miniMapEventMarker = new PKImage(PKEngine.MINI_MAP_GRID_SIZE, PKEngine.MINI_MAP_GRID_SIZE * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MINI_MAP_MARKER_HEIGHT / PKEngine.MINI_MAP_MARKER_WIDTH, 1.0f, 1.0f);
	private PKImage mascot = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MASCOT_HEIGHT / PKEngine.MASCOT_WIDTH, 1.0f, 1.0f);
	private PKImage msgOpenChestSuccess = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MASCOT_HEIGHT / PKEngine.MASCOT_WIDTH, 1.0f, 1.0f);
	private PKImage msgNoChest = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MASCOT_HEIGHT / PKEngine.MASCOT_WIDTH, 1.0f, 1.0f);
	private PKImage msgNoKey = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MASCOT_HEIGHT / PKEngine.MASCOT_WIDTH, 1.0f, 1.0f);
	private PKImage msgCorrectKey = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MASCOT_HEIGHT / PKEngine.MASCOT_WIDTH, 1.0f, 1.0f);
	private PKImage msgInvalidKey = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MASCOT_HEIGHT / PKEngine.MASCOT_WIDTH, 1.0f, 1.0f);
	private PKImage msgKnowSrOne = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MASCOT_HEIGHT / PKEngine.MASCOT_WIDTH, 1.0f, 1.0f);
	private PKImage msgKnowHCI = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MASCOT_HEIGHT / PKEngine.MASCOT_WIDTH, 1.0f, 1.0f);
	private PKImage msgKnowStudLounge = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MASCOT_HEIGHT / PKEngine.MASCOT_WIDTH, 1.0f, 1.0f);
	private PKImage msgKnowSmallSR = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MASCOT_HEIGHT / PKEngine.MASCOT_WIDTH, 1.0f, 1.0f);
	private PKImage msgEventAnnouncement = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MASCOT_HEIGHT / PKEngine.MASCOT_WIDTH, 1.0f, 1.0f);
	private PKImage msgEnterMiniGame = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MASCOT_HEIGHT / PKEngine.MASCOT_WIDTH, 1.0f, 1.0f);
	private PKImage resultWin = new PKImage();
	private PKImage resultLose = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.RESULT_HEIGHT / PKEngine.RESULT_WIDTH, 1.0f, 1.0f);
	private PKImage loadingMsg = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.LOADING_MSG_HEIGHT / PKEngine.LOADING_MSG_WIDTH, 1.0f, 1.0f);
	private PKImage choosePlayerMsg = new PKImage(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.LOADING_MSG_HEIGHT / PKEngine.LOADING_MSG_WIDTH, 1.0f, 1.0f);
	
	private int msgIndicator = 0;
	private int currentEvent = 0;
	private boolean logOn = false;
	private boolean globalMapIndicator = false;
	private boolean enterMiniGame = false;
	private boolean announceEvent = false;
	private boolean showEndGameMsg = false;
	
	// Variables for time.
	private long loopStart = 0;
	private long loopEnd = 0;
	private long loopRunTime = 0;
	private long startTime = 0;
	private long startAddScoreTime = 0;
	private long startMsgTime = 0;
	private long blinkTime = 0;
	private boolean startGameTimer = false;
	private boolean startLoginTimer = false;
	private boolean startFallingCoinTimer = false;
	private boolean startMiniGame = false;
	private boolean startBlinkTime = false;
	private int test = 0;
	
	private Context mContext;
	
	public PKGameRenderer(Context context)
	{
		mContext = context;
	}
	
	@Override
	public void onDrawFrame(GL10 gl)
	{
		// Set game to run at fps specified by GAME_THREAD_FPS_SLEEP.
		loopStart = System.currentTimeMillis();
		try
		{
			if (loopRunTime < PKEngine.GAME_THREAD_FPS_SLEEP) Thread.sleep(PKEngine.GAME_THREAD_FPS_SLEEP);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		// globalEvent code: Stores the current global game status
		// 0 = pre-game
		// 1 = countdown stage, once the first player logon to the server // 10
		// 2 = game starts // total game duration = 60 sec
		// 3 = falling coins starts // 10 sec mark
		// 4 = falling coins ends // 10 sec duration
		// 5 = game end
		
		// Updates current Event
		System.out.println(""+PKEngine.client.getGlobalEventStatus());
		currentEvent = PKEngine.client.getGlobalEventStatus();
		switch (currentEvent)
		{
			case 1:
				if (!startLoginTimer)
					startLoginTimer = true;
				break;
			case 2:
				// starts gameTimer
				if (!logOn)
				{
					PKEngine.playerID = 0; // TODO: Assign player an ID if player did not choose a valid ID before countdown ends.
				}
				if (!startGameTimer)
				{
					startLoginTimer = false;
					startGameTimer = true;
				}
				break;
			case 3:
				announceEvent = true;
				if (!startBlinkTime)
	        	{
					globalMapIndicator = true;
	        		blinkTime = System.currentTimeMillis();
	        		startBlinkTime = true;
	        	}
				if (System.currentTimeMillis() - blinkTime >= 500)
				{
					globalMapIndicator = !globalMapIndicator;
					blinkTime = System.currentTimeMillis();
				}
				// System.out.println("System.currentTimeMillis(): " + System.currentTimeMillis());
				// System.out.println("blinkTime: " + blinkTime);
				if (playerPosition == globalEventPosition && enterMiniGame)
					startMiniGame = true;
				if (!startFallingCoinTimer)
				{
					startFallingCoinTimer = true;
				}
				break;
			case 4:
				globalMapIndicator = false;
				startMiniGame = false;
				enterMiniGame = false;
				announceEvent = false;
				test = 0;
				break;
			case 5:
				startGameTimer = false;
				showEndGameMsg = true;
				PKEngine.gameEnd = true;
				break;
		}
		
		if (!showEndGameMsg)
		{
			if (System.currentTimeMillis() - startTime >= 500)
			{
				// Update positions on server.
				try
				{
					PKEngine.client.mapUpdateEvent(PKEngine.playerID);
					//PKEngine.client.scoreUpdateEvent(PKEngine.PLAYER_ID, 500);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				startTime = System.currentTimeMillis();
			}
		}
		
		// Update player location.
		playerPosition = PKEngine.client.getPlayerLocation(PKEngine.playerID);
		
		// Update player stats.
		playerScore = PKEngine.client.getPlayerScore(PKEngine.playerID);
		numKeys = PKEngine.client.getPlayerKeyNum(PKEngine.playerID);
		
		// Clear OpenGL buffers.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		// Draw background.
		//drawBackground(gl);
		
		// Update treasure chest locations.
		updateTreasureLocation();
		
		// Draw PoV map.
		drawPovMap(gl);
		drawPlayer(gl);
		if (addChestScore)
			drawChestScore(gl, startAddScoreTime);
		
		// Draw rest elements of UI.
		drawOverlay(gl);
		drawTreasureKey(gl);
		drawGoldCoin(gl);
		drawMiniMap(gl);
		printLocationName(gl);
		if (startLoginTimer)
		{
			if (PKEngine.playerID > 0 && !logOn)
			{
				try
				{
					if (PKEngine.client.loginEvent(PKEngine.playerID).equalsIgnoreCase("Successful"))
						logOn = true;
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			drawLoginMsg(gl);
			printLoginInfo(gl);
		}
		if (startGameTimer)
			printGameTime(gl);
		if (startMiniGame && test == 0)
		{
			test = 1;
			new MiniGame().execute();
		}
		
		if (showEndGameMsg)
		{
			drawEndGame(gl);
			printEndGameInfo(gl);
		} else
		{
			printKeys(gl);
			printScore(gl);
		}
		// Set blending.
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		// Check loop run time.
		loopEnd = System.currentTimeMillis();
		loopRunTime = loopEnd - loopStart;
	}
	
	private class MiniGame extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute() {
			Intent miniGame = new Intent(mContext, FallingCoinsActivity.class);
			mContext.startActivity(miniGame);
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... arg0)
		{
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		// Set up viewport.
		gl.glViewport(0, 0, width, height);
		
		// Set up camera.
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, 1.0f, 0.0f, 1.0f, -1.0f, 1.0f);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		// Depth settings.
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		
		// Blending settings.
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		// Loads fonts
		font = new PKTexFont(PKEngine.context, gl);
		//timerFont = new TexFont(PKEngine.context, gl);
		try
		{
			font.LoadFontAlt("digital2.bff", gl);
			//timerFont.LoadFontAlt("digital2.bff", gl);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Load textures.
		background.loadTexture(gl, PKEngine.BACKGROUND_LAYER_ONE, PKEngine.context, GL10.GL_REPEAT);
		povMap.loadTexture(gl, PKEngine.POV_MAP, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		overlayTop.loadTexture(gl, PKEngine.OVERLAY_TOP, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		overlayBtm.loadTexture(gl, PKEngine.OVERLAY_BTM, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		player.loadTexture(gl, PKEngine.PLAYER_SPRITE, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		treasureChest.loadTexture(gl, PKEngine.TREASURE_CHEST_CLOSED, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		addGold.loadTexture(gl, PKEngine.ADD_GOLD, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		treasureKey.loadTexture(gl, PKEngine.TREASURE_KEY, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		goldCoin.loadTexture(gl, PKEngine.GOLD_COIN, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		miniMap.loadTexture(gl, PKEngine.MINI_MAP, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		miniMapMarker.loadTexture(gl, PKEngine.MINI_MAP_MARKER, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		miniMapEventMarker.loadTexture(gl, PKEngine.MINI_MAP_EVENT, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		mascot.loadTexture(gl, PKEngine.MASCOT, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		msgOpenChestSuccess.loadTexture(gl, PKEngine.MASCOT_OPEN_CHEST_SUCCESS, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		msgNoChest.loadTexture(gl, PKEngine.MASCOT_NO_CHEST, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		msgNoKey.loadTexture(gl, PKEngine.MASCOT_NO_KEY, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		msgCorrectKey.loadTexture(gl, PKEngine.MASCOT_CORRECT_KEY, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		msgInvalidKey.loadTexture(gl, PKEngine.MASCOT_INVALID_KEY, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		msgKnowSrOne.loadTexture(gl, PKEngine.MASCOT_LEARN_SR1, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		msgKnowHCI.loadTexture(gl, PKEngine.MASCOT_LEARN_SR1, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		msgKnowStudLounge.loadTexture(gl, PKEngine.MASCOT_LEARN_SR1, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		msgKnowSmallSR.loadTexture(gl, PKEngine.MASCOT_LEARN_SR1, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		msgEventAnnouncement.loadTexture(gl, PKEngine.MASCOT_EVENT_ANNOUNCEMENT, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		msgEnterMiniGame.loadTexture(gl, PKEngine.MASCOT_ENTER_FALLING_COIN, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		resultWin.loadTexture(gl, PKEngine.RESULT_WIN, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		resultLose.loadTexture(gl, PKEngine.RESULT_LOSE, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		loadingMsg.loadTexture(gl, PKEngine.LOADING_MSG, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		choosePlayerMsg.loadTexture(gl, PKEngine.CHOOSE_PLAYER_MSG, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		
		// Loads mascot images.
		// [0] - mascot //Default
		// [1] - Open Chest Success
		// [2] - No Chests
		// [3] - No Keys
		// [4] - Correct Key
		// [5] - Invalid Key
		// [6] - Did You Know - Seminar Room 1
		// [7] - Did You Know - HCI
		// [8] - Did You Know - Student Lounge
		// [9] - Did You Know - Small Seminar Room
		PKEngine.mascotImages.add(mascot);
		PKEngine.mascotImages.add(msgOpenChestSuccess);
		PKEngine.mascotImages.add(msgNoChest);
		PKEngine.mascotImages.add(msgNoKey);
		PKEngine.mascotImages.add(msgCorrectKey);
		PKEngine.mascotImages.add(msgInvalidKey);
		PKEngine.mascotImages.add(msgKnowSrOne);
		PKEngine.mascotImages.add(msgKnowHCI);
		PKEngine.mascotImages.add(msgKnowStudLounge);
		PKEngine.mascotImages.add(msgKnowSmallSR);
		
		startTime = System.currentTimeMillis();
		
		// Initialise position and player on server.
		if (currentEvent == 0)
		{
			PKEngine.playerID = 0;
			logOn = false;
			try
			{
				PKEngine.client.mapUpdateEvent(PKEngine.playerID);
				PKEngine.client.loginEvent(PKEngine.playerID);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// Initialisation for player position.
		playerPosition = PKEngine.client.getPlayerLocation(PKEngine.playerID);
		playerNewPos.add(playerPosition);
		povMapCoords[0] = playerPosition % PKEngine.POV_MAP_WIDTH * 1.0f / (PKEngine.POV_MAP_WIDTH + 2);
		povMapCoords[1] = playerPosition / PKEngine.POV_MAP_WIDTH * -1.0f / (PKEngine.POV_MAP_HEIGHT + 2);
		
		// Initialisation of treasure locations.
		updateTreasureLocation();
		
		// Initialisation of player score.
		playerScore = 0;
		
		PKEngine.totalPlayers = PKEngine.client.getTotalNumOfPlayersSupported();
	}
	
	private void updateTreasureLocation()
	{
		for (int i = 0; i < PKEngine.POV_MAP_HEIGHT; i++)
		{
			for (int j = 0; j < PKEngine.POV_MAP_WIDTH; j++)
			{
				PKEngine.treasureLocations[i][j] = PKEngine.client.getTreasureList2D()[i][j];
			}
		}
	}
	
	private void drawBackground(GL10 gl)
	{
		// Draw background.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		background.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	
	private void printLocationName(GL10 gl)
	{
		//font.SetScale(1.0f);
		//font.PrintAt(gl, "<LOCATION NAME>", (PKEngine.scrWidth - font.GetTextLength("<LOCATION NAME>")) / 2, PKEngine.scrWidth - font.GetTextHeight());
		//font.PrintAt(gl, "<LOCATION NAME>", 0.5f * (PKEngine.scrWidth - font.GetTextLength("<LOCATION NAME>")), 0.0f);
		//font.PrintAt(gl, "<LOCATION NAME>", 0.5f, 0.5f);
	}
	
	private void printScore(GL10 gl)
	{
		font.SetScale(1.5f);
		font.PrintAt(gl, String.valueOf(playerScore), 0.13f * PKEngine.scrWidth, 0.025f * PKEngine.scrHeight - font.fntCellHeight);
		// x = (0.05f + 0.08f) * PKEngine.scrWidth
		// y = 0.025f * PKEngine.scrHeight
	}

	private void printKeys(GL10 gl)
	{
		font.SetScale(1.5f);
		font.PrintAt(gl, String.valueOf(numKeys), 0.13f * PKEngine.scrWidth, 0.1f * PKEngine.scrHeight - font.fntCellHeight);
		// x = (0.05f + 0.08f) * PKEngine.scrWidth
		// y = 0.1f * PKEngine.scrHeight
	}
	
	private void printGameTime(GL10 gl)
	{
		//timerFont.SetScale(2.0f);
		font.SetScale(2.0f);
		//timerFont.PrintAt(gl, "TIME", 0.05f * PKEngine.scrWidth, 0.83f * PKEngine.scrHeight);
		font.PrintAt(gl, "TIME", 0.05f * PKEngine.scrWidth, 0.83f * PKEngine.scrHeight);
		
		int timeFromServer = PKEngine.client.getCurrentInGameTime();
		int minutes = timeFromServer / 60;
		int seconds = timeFromServer % 60;
		//timerFont.PrintAt(gl, "" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds), 0.05f * PKEngine.scrWidth, 0.83f * PKEngine.scrHeight - timerFont.fntCellHeight);
		font.PrintAt(gl, "" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds), 0.05f * PKEngine.scrWidth, 0.83f * PKEngine.scrHeight - font.fntCellHeight);
	}
	
	private void printLoginInfo(GL10 gl)
	{
		font.SetScale(3.0f);
		font.PrintAt(gl, String.valueOf(PKEngine.client.getCurrentPreGameTime()), 0.5f * PKEngine.scrWidth, 0.25f * PKEngine.scrHeight);
		
		font.SetScale(1.0f);
		font.PrintAt(gl, "NO. OF PLAYERS: " + String.valueOf(PKEngine.client.getNumPlayerLogon()), (PKEngine.scrWidth - font.GetTextLength("NO. OF PLAYERS: ")) / 2, 0.25f * PKEngine.scrHeight);
	}
	
	private void printEndGameInfo(GL10 gl) {
		font.SetScale(3.0f);
		font.PrintAt(gl, "YOUR RANK: " + String.valueOf(PKEngine.client.getRankingOfGivenPlayer(PKEngine.playerID)), (PKEngine.scrWidth - font.GetTextLength("YOUR RANK: ")) / 2, 0.4f * PKEngine.scrHeight);
	}
	
	private void drawLoginMsg(GL10 gl)
	{
		/*for (int i = 0; i < totalPlayers; i++)
		{
			if (PKEngine.client.checkPlayerLogonStatus(PKEngine.playerID))
			{
				//PKGame.players[i].setAlpha(0.5f);
				//PKGame.players[i].setEnabled(false);
			}
		}*/
		
		// Draw Loading Msg Overlay
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		if (PKEngine.playerID == 0)
			choosePlayerMsg.draw(gl);
		else
			loadingMsg.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	
	private void drawPlayer(GL10 gl)
	{
		// Draw player sprite.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.2f, 0.2f * PKEngine.scrWidth / PKEngine.scrHeight, 1.0f);
		gl.glTranslatef(2.0f, 2.5f * PKEngine.scrHeight / PKEngine.scrWidth - 2.5f, 0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		switch (PKEngine.playerWalkAction)
		{
			case PKEngine.PLAYER_LEFT:
				gl.glTranslatef(0.167f, 0.833f, 0.0f);
				if (playerPosition % PKEngine.POV_MAP_WIDTH > 0)
				{
					// Not at left boundary.
					// Move player one position to the left.
					playerPosition -= 1;
				}
				break;
			case PKEngine.PLAYER_RIGHT:
				gl.glTranslatef(0.833f, 0.833f, 0.0f);
				if (playerPosition % PKEngine.POV_MAP_WIDTH < PKEngine.POV_MAP_WIDTH - 1)
				{
					// Not at right boundary.
					// Move player one position to the right.
					playerPosition += 1;
				}
				break;
			case PKEngine.PLAYER_UP:
				gl.glTranslatef(0.5f, 0.667f, 0.0f);
				if (playerPosition / PKEngine.POV_MAP_WIDTH > 0)
				{
					// Not at top boundary.
					// Move player one position up.
					playerPosition -= PKEngine.POV_MAP_WIDTH;
				}
				break;
			case PKEngine.PLAYER_DOWN:
				gl.glTranslatef(0.5f, 0.333f, 0.0f);
				if (playerPosition / PKEngine.POV_MAP_WIDTH < PKEngine.POV_MAP_HEIGHT - 1)
				{
					// Not at bottom boundary.
					// Move player one position down.
					playerPosition += PKEngine.POV_MAP_WIDTH;
				}
				break;
			case PKEngine.PLAYER_STATIONARY:
				gl.glTranslatef(0.333f, 0.833f, 0.0f);
				break;
			default:
				gl.glTranslatef(0.333f, 0.833f, 0.0f);
				break;
		}
		
		//player.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	
	private void drawPovMap(GL10 gl)
	{
		if (playerPosition != playerNewPos.get(playerNewPos.size() - 1)) playerNewPos.add(playerPosition);
		
		if (playerNewPos.size() > 1)
		{
			// Need to animate map movement.
			if (!animStart)
			{
				animRunTime = 0;
				animStart = true;
			}
			
			// Check direction of player movement.
			if (playerNewPos.get(0) - playerNewPos.get(1) == 1)
			{
				// Player moved left, move map to the right.
				if (animRunTime / PKEngine.SPRITE_TIME_BETWEEN_ANI % 2 == 0) spriteCoords = PKEngine.SPRITE_LEFT_1;
				else spriteCoords = PKEngine.SPRITE_LEFT_2;
				povMapCoords[0] -= 1.0f / (PKEngine.POV_MAP_WIDTH + 2) / (1.0f * PKEngine.TOTAL_ANIMATION_TIME / loopRunTime);
				treasureChestOffset[0] += 1.0f * loopRunTime / PKEngine.TOTAL_ANIMATION_TIME;
			}
			if (playerNewPos.get(0) - playerNewPos.get(1) == -1)
			{
				// Player moved right, move map to the left.
				if (animRunTime / PKEngine.SPRITE_TIME_BETWEEN_ANI % 2 == 0) spriteCoords = PKEngine.SPRITE_RIGHT_1;
				else spriteCoords = PKEngine.SPRITE_RIGHT_2;
				povMapCoords[0] += 1.0f / (PKEngine.POV_MAP_WIDTH + 2) / (1.0f * PKEngine.TOTAL_ANIMATION_TIME / loopRunTime);
				treasureChestOffset[0] -= 1.0f * loopRunTime / PKEngine.TOTAL_ANIMATION_TIME;
			}
			if (playerNewPos.get(0) - playerNewPos.get(1) == PKEngine.POV_MAP_WIDTH)
			{
				// Player moved up, move map down.
				if (animRunTime / PKEngine.SPRITE_TIME_BETWEEN_ANI % 2 == 0) spriteCoords = PKEngine.SPRITE_UP_1;
				else spriteCoords = PKEngine.SPRITE_UP_2;
				povMapCoords[1] += 1.0f / (PKEngine.POV_MAP_HEIGHT + 2) / (1.0f * PKEngine.TOTAL_ANIMATION_TIME / loopRunTime);
				treasureChestOffset[1] -= 1.0f * loopRunTime / PKEngine.TOTAL_ANIMATION_TIME;
			}
			if (playerNewPos.get(0) - playerNewPos.get(1) == -PKEngine.POV_MAP_WIDTH)
			{
				// Player moved down, move map up.
				if (animRunTime / PKEngine.SPRITE_TIME_BETWEEN_ANI % 2 == 0) spriteCoords = PKEngine.SPRITE_DOWN_1;
				else spriteCoords = PKEngine.SPRITE_DOWN_2;
				povMapCoords[1] -= 1.0f / (PKEngine.POV_MAP_HEIGHT + 2) / (1.0f * PKEngine.TOTAL_ANIMATION_TIME / loopRunTime);
				treasureChestOffset[1] += 1.0f * loopRunTime / PKEngine.TOTAL_ANIMATION_TIME;
			}
			
			animRunTime += loopRunTime;
			
			if (animRunTime > PKEngine.TOTAL_ANIMATION_TIME)
			{
				playerNewPos.remove(0);
				povMapCoords[0] = playerNewPos.get(0) % PKEngine.POV_MAP_WIDTH * 1.0f / (PKEngine.POV_MAP_WIDTH + 2);
				povMapCoords[1] = playerNewPos.get(0) / PKEngine.POV_MAP_WIDTH * -1.0f / (PKEngine.POV_MAP_HEIGHT + 2);
				treasureChestOffset[0] = 0.0f;
				treasureChestOffset[1] = 0.0f;
				animStart = false;
			}
		}
		else
		{
			if (animRunTime / PKEngine.SPRITE_TIME_BETWEEN_STATIONARY_ANI % 2 == 0) spriteCoords = PKEngine.SPRITE_STATIONARY_1;
			else spriteCoords = PKEngine.SPRITE_STATIONARY_2;
			
			animRunTime += loopRunTime;
		}
		
		// Draw PoV map.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight, 1.0f);
		gl.glTranslatef(0.0f, 0.5f * PKEngine.scrHeight / PKEngine.scrWidth - 0.5f, 0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(povMapCoords[0], povMapCoords[1], 0.0f);
		
		povMap.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		
		// Draw player sprite.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.25f, 0.25f * PKEngine.scrWidth / PKEngine.scrHeight, 1.0f);
		gl.glTranslatef(1.5f, 2.0f * PKEngine.scrHeight / PKEngine.scrWidth - 0.5f, 0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glTranslatef(spriteCoords[0], spriteCoords[1], 0.0f);
		
		player.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		
		// Draw all treasure chests on the PoV map.
		for (int i = playerNewPos.get(0) / PKEngine.POV_MAP_WIDTH - 2; i < playerNewPos.get(0) / PKEngine.POV_MAP_WIDTH + 3; i++)
		{
			for (int j = playerNewPos.get(0) % PKEngine.POV_MAP_WIDTH - 2; j < playerNewPos.get(0) % PKEngine.POV_MAP_WIDTH + 3; j++)
			{
				if (i >= 0 && j >= 0 && i < PKEngine.POV_MAP_HEIGHT && j < PKEngine.POV_MAP_WIDTH)
					if (PKEngine.treasureLocations[i][j] == 1)
						drawTreasureChest(gl,
										  j - playerNewPos.get(0) % PKEngine.POV_MAP_WIDTH + 2,
										  i - playerNewPos.get(0) / PKEngine.POV_MAP_WIDTH + 2,
										  treasureChestOffset[0],
										  treasureChestOffset[1]);
			}
		}
	}
	
	private void drawChestScore(GL10 gl, long startTime) {
		// Draw +50.
		if (System.currentTimeMillis() - startTime < 2000)
		{
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glTranslatef(0.5f, 0.5f, 0.0f);
			
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			
			addGold.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();
		} else
			addChestScore = false;
	}
	
	private void drawTreasureChest(GL10 gl, int relativePosX, int relativePosY, float xOffset, float yOffset)
	{
		// Draw a single treasure chest.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.2f, 0.2f * PKEngine.scrWidth / PKEngine.scrHeight, 1.0f);
		gl.glTranslatef(-1.333f + 1.667f * (relativePosX + xOffset), // (middlePos - 2 * 5 /3) + 5 / 3 * (relativePosX + xOffset)
						2.0f * PKEngine.scrHeight / PKEngine.scrWidth + 3.333f + 1.667f * (-relativePosY + yOffset),
						0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		treasureChest.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	
	private void drawOverlay(GL10 gl)
	{
		// Draw top overlay.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(1.0f, 1.0f, 1.0f);
		gl.glTranslatef(0.0f, 1.0f - 0.6f * (PKEngine.scrHeight - PKEngine.scrWidth) / PKEngine.scrHeight, 0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		overlayTop.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		
		// Draw bottom overlay.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight, 1.0f);
		gl.glTranslatef(0.0f, 0.5f * PKEngine.scrHeight / PKEngine.scrWidth - 1.5f, 0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		overlayBtm.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		
		// Draw mascot for global event announcement.
		if (announceEvent)
		{
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			
			msgEventAnnouncement.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();
		}
		
		// Draw mascot for entering global event.
		if (playerPosition == globalEventPosition && currentEvent == 3)
		{
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			
			msgEnterMiniGame.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();
		}
		
		// Draw mascot.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		if (System.currentTimeMillis() - startMsgTime > 5000)
			msgIndicator = 0;
		PKEngine.mascotImages.get(msgIndicator).draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	
	private void drawTreasureKey(GL10 gl)
	{
		// Draw treasure key.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glTranslatef(0.05f, 0.1f, 0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		treasureKey.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	
	private void drawGoldCoin(GL10 gl)
	{
		// Draw gold coin.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glTranslatef(0.05f, 0.025f, 0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		goldCoin.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	
	private void drawMiniMap(GL10 gl)
	{
		// Draw mini map.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glTranslatef(PKEngine.MINI_MAP_X_OFFSET + 1.0f - PKEngine.MINI_MAP_SCALE,
						PKEngine.MINI_MAP_Y_OFFSET + 1.0f - PKEngine.MINI_MAP_SCALE * PKEngine.scrWidth / PKEngine.scrHeight * PKEngine.MINI_MAP_HEIGHT / PKEngine.MINI_MAP_WIDTH,
						0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		miniMap.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		
		// Draw mini map marker.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glTranslatef(PKEngine.MINI_MAP_X_OFFSET + 1.0f - PKEngine.MINI_MAP_SCALE + PKEngine.MINI_MAP_GRID_SIZE,
						PKEngine.MINI_MAP_Y_OFFSET + 1.0f - 2.0f * PKEngine.MINI_MAP_GRID_SIZE * PKEngine.scrWidth / PKEngine.scrHeight,
						0.0f); // Translate marker to position 0 of map.
		gl.glTranslatef(playerPosition % PKEngine.POV_MAP_WIDTH * PKEngine.MINI_MAP_GRID_SIZE,
						-playerPosition / PKEngine.POV_MAP_WIDTH * PKEngine.MINI_MAP_GRID_SIZE * PKEngine.scrWidth / PKEngine.scrHeight,
						0.0f); // Translate marker to player position.
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		miniMapMarker.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
		
		// Draw global event marker.
		if (globalMapIndicator)
		{
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushMatrix();
			gl.glTranslatef(PKEngine.MINI_MAP_X_OFFSET + 1.0f - PKEngine.MINI_MAP_SCALE + PKEngine.MINI_MAP_GRID_SIZE,
							PKEngine.MINI_MAP_Y_OFFSET + 1.0f - 2.0f * PKEngine.MINI_MAP_GRID_SIZE * PKEngine.scrWidth / PKEngine.scrHeight,
							0.0f); // Translate marker to position 0 of map.
			gl.glTranslatef(globalEventPosition % PKEngine.POV_MAP_WIDTH * PKEngine.MINI_MAP_GRID_SIZE,
							-globalEventPosition / PKEngine.POV_MAP_WIDTH * PKEngine.MINI_MAP_GRID_SIZE * PKEngine.scrWidth / PKEngine.scrHeight,
							0.0f); // Translate marker to global event position.
			
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			
			// Map Pos 49
			miniMapEventMarker.draw(gl);
			gl.glPopMatrix();
			gl.glLoadIdentity();
		}
	}

	private void drawEndGame(GL10 gl) {
		// Draw End Game Result
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		if (PKEngine.client.getRankingOfGivenPlayer(PKEngine.playerID) == 1){
			resultWin.draw(gl);
		} else
			resultLose.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	
	public String openChest() throws Exception
	{
		OpenChestThread openChestThread = new OpenChestThread();
		openChestThread.start();
		while (openChestThread.isAlive()){}
		return checkChestReply;
	}
	
	private class OpenChestThread extends Thread
	{
		public void run()
		{
			try
			{
				checkChestReply = PKEngine.client.openTreasureEvent(PKEngine.playerID);
				if (checkChestReply.equalsIgnoreCase("Successful"))
				{
					startAddScoreTime = System.currentTimeMillis();
					msgIndicator = 1;
					addChestScore = true;
				} else if (checkChestReply.equalsIgnoreCase("NoChest"))
				{
					
					msgIndicator = 2;
				} else
					msgIndicator = 3;
				startMsgTime = System.currentTimeMillis();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public String verifyKey(int keyCode) throws Exception 
	{
		AddKeyCodeThread addKeyCodeThread = new AddKeyCodeThread(keyCode);
		addKeyCodeThread.start();
		while (addKeyCodeThread.isAlive()){}
		return checkKeyReply;
	}

	private class AddKeyCodeThread extends Thread
	{

		int keyCode;
		AddKeyCodeThread(int keyCode)
		{
			this.keyCode = keyCode;
		}

		public void run()
		{
			try
			{
				checkKeyReply = PKEngine.client.addKeyCodeEvent(PKEngine.playerID, keyCode);
				if (checkKeyReply.equalsIgnoreCase("Successful"))
					msgIndicator = 4;
				else
					msgIndicator = 5;
				startMsgTime = System.currentTimeMillis();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void setPlayerLogOn()
	{
		PlayerLogOnThread playerLogOnThread = new PlayerLogOnThread();
		playerLogOnThread.start();
		while (playerLogOnThread.isAlive()){}
	}
	
	private class PlayerLogOnThread extends Thread
	{
		public void run()
		{
			try
			{
				PKEngine.client.loginEvent(PKEngine.playerID);
				logOn = true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}	
		}
	}
	
	public void checkMiniGame()
	{
		if (playerPosition == globalEventPosition && currentEvent == 3)
		{
			enterMiniGame = true;
		}
	}
	
	public int getCurrentEvent()
	{
		return currentEvent;
	}

	public boolean getPlayerLogOn()
	{
		return logOn;
	}
}
