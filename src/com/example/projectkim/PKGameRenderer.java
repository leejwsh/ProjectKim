package com.example.projectkim;

import java.io.IOException;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public class PKGameRenderer implements Renderer
{
	// Variables for text.
	private TexFont font;
	
	// Variables for background.
	private PKImage background = new PKImage();
	
	// Variables for PoV map.
	private PKImage povMap = new PKImage(PKEngine.POV_MAP_TEXTURE);
	private PKImage treasureChest = new PKImage();
	private PKImage player = new PKImage(PKEngine.PLAYER_TEXTURE);
	private int playerPosition = 0;
	private ArrayList<Integer> playerNewPos = new ArrayList<Integer>();
	private boolean animStart = false;
	private int animRunTime = 0;
	private float[] povMapCoords = new float[2];
	private float[] spriteCoords = new float[2];
	
	// Variables for rest elements of UI.
	private PKImage treasureKey = new PKImage();
	private PKImage miniMap = new PKImage();
	
	// Variables for time.
	private long loopStart = 0;
	private long loopEnd = 0;
	private long loopRunTime = 0;
	
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
		
		// Clear OpenGL buffers.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		// Draw background.
		//drawBackground(gl);
		
		// Draw PoV map.
		drawPovMap(gl);
		drawPlayer(gl);
		
		// Draw rest elements of UI.
		drawTreasureKey(gl);
		drawMiniMap(gl);
		printLocationName(gl);
		
		// Set blending.
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		// Check loop run time.
		loopEnd = System.currentTimeMillis();
		loopRunTime = loopEnd - loopStart;
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
		font = new TexFont(PKEngine.context, gl);
		try
		{
			font.LoadFontAlt("visitor.bff", gl);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Load textures.
		background.loadTexture(gl, PKEngine.BACKGROUND_LAYER_ONE, PKEngine.context, GL10.GL_REPEAT);
		povMap.loadTexture(gl, PKEngine.POV_MAP, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		player.loadTexture(gl, PKEngine.PLAYER_SPRITE, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		treasureChest.loadTexture(gl, PKEngine.TREASURE_CHEST, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		treasureKey.loadTexture(gl, PKEngine.TREASURE_KEY, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		miniMap.loadTexture(gl, PKEngine.MINI_MAP, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		
		// Initialisation for player position.
		playerNewPos.add(playerPosition);
		povMapCoords[0] = playerPosition % PKEngine.POV_MAP_WIDTH * 1.0f / (PKEngine.POV_MAP_WIDTH + 2);
		povMapCoords[1] = playerPosition / PKEngine.POV_MAP_WIDTH * -1.0f / (PKEngine.POV_MAP_HEIGHT + 2);
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
		font.SetScale(1.0f);
		font.PrintAt(gl, "<LOCATION NAME>", (PKEngine.scrWidth - font.GetTextLength("<LOCATION NAME>")) / 2, PKEngine.scrWidth - font.GetTextHeight());
		//font.PrintAt(gl, "<LOCATION NAME>", 0.5f * (PKEngine.scrWidth - font.GetTextLength("<LOCATION NAME>")), 0.0f);
		//font.PrintAt(gl, "<LOCATION NAME>", 0.5f, 0.5f);
	}
	
	private void drawPlayer(GL10 gl)
	{
		// Draw player sprite.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.25f, 0.25f * PKEngine.scrWidth / PKEngine.scrHeight, 1.0f);
		gl.glTranslatef(1.5f, 2.0f * PKEngine.scrHeight / PKEngine.scrWidth - 0.5f, 0.0f);
		
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
				povMapCoords[0] -= 1.0f / (PKEngine.POV_MAP_WIDTH + 2) / (PKEngine.TOTAL_ANIMATION_TIME / loopRunTime);
			}
			if (playerNewPos.get(0) - playerNewPos.get(1) == -1)
			{
				// Player moved right, move map to the left.
				if (animRunTime / PKEngine.SPRITE_TIME_BETWEEN_ANI % 2 == 0) spriteCoords = PKEngine.SPRITE_RIGHT_1;
				else spriteCoords = PKEngine.SPRITE_RIGHT_2;
				povMapCoords[0] += 1.0f / (PKEngine.POV_MAP_WIDTH + 2) / (PKEngine.TOTAL_ANIMATION_TIME / loopRunTime);
			}
			if (playerNewPos.get(0) - playerNewPos.get(1) == PKEngine.POV_MAP_WIDTH)
			{
				// Player moved up, move map down.
				if (animRunTime / PKEngine.SPRITE_TIME_BETWEEN_ANI % 2 == 0) spriteCoords = PKEngine.SPRITE_UP_1;
				else spriteCoords = PKEngine.SPRITE_UP_2;
				povMapCoords[1] += 1.0f / (PKEngine.POV_MAP_HEIGHT + 2) / (PKEngine.TOTAL_ANIMATION_TIME / loopRunTime);
			}
			if (playerNewPos.get(0) - playerNewPos.get(1) == -PKEngine.POV_MAP_WIDTH)
			{
				// Player moved down, move map up.
				if (animRunTime / PKEngine.SPRITE_TIME_BETWEEN_ANI % 2 == 0) spriteCoords = PKEngine.SPRITE_DOWN_1;
				else spriteCoords = PKEngine.SPRITE_DOWN_2;
				povMapCoords[1] -= 1.0f / (PKEngine.POV_MAP_HEIGHT + 2) / (PKEngine.TOTAL_ANIMATION_TIME / loopRunTime);
			}
			
			animRunTime += loopRunTime;
			
			if (animRunTime > PKEngine.TOTAL_ANIMATION_TIME)
			{
				playerNewPos.remove(0);
				povMapCoords[0] = playerNewPos.get(0) % PKEngine.POV_MAP_WIDTH * 1.0f / (PKEngine.POV_MAP_WIDTH + 2);
				povMapCoords[1] = playerNewPos.get(0) / PKEngine.POV_MAP_WIDTH * -1.0f / (PKEngine.POV_MAP_HEIGHT + 2);
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
		
		// Draw treasure chests.
		for (int i = 0; i < 5; i++)
		{
			for (int j = 0; j < 5; j++)
			{
				
			}
		}
		
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
	}
	
	private void drawTreasureChest(GL10 gl, float x, float y)
	{
		// Draw treasure chests.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.25f, 0.25f * PKEngine.scrWidth / PKEngine.scrHeight, 1.0f);
		gl.glTranslatef(1.5f, 2.0f * PKEngine.scrHeight / PKEngine.scrWidth - 0.5f, 0.0f);
		
		//gl.glScalef(0.25f, 0.25f * PKEngine.scrWidth / PKEngine.scrHeight, 1.0f);
		//gl.glTranslatef(1.5f, 2.0f * PKEngine.scrHeight / PKEngine.scrWidth - 0.5f, 0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		treasureChest.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	
	private void drawTreasureKey(GL10 gl)
	{
		// Draw treasure key.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.333f, 0.333f * PKEngine.scrWidth / PKEngine.scrHeight, 1.0f);
		gl.glTranslatef(0.0f, 1.5f * PKEngine.scrHeight / PKEngine.scrWidth + 1.5f, 0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		treasureKey.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	
	private void drawMiniMap(GL10 gl)
	{
		// Draw mini map.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(0.666f, 0.666f * PKEngine.scrWidth / PKEngine.scrHeight, 1.0f);
		gl.glTranslatef(0.5f, 0.75f * PKEngine.scrHeight / PKEngine.scrWidth + 0.75f, 0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		miniMap.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
}
