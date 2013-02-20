package com.example.projectkim;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public class PKGameRenderer implements Renderer
{
	// Variables for background.
	private PKImage background = new PKImage();
	
	// Variables for player.
	private PKImage player = new PKImage(PKEngine.PLAYER_TEXTURE);
	private int playerPosition = PKEngine.POV_MAP_WIDTH * PKEngine.POV_MAP_HEIGHT - 1;
	//private int playerWalkFrames = 0;
	
	// Variables for PoV map.
	private PKImage povMap = new PKImage();
	
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
		
		// Draw stuff.
		//drawBackground(gl);
		drawPovMap(gl);
		drawPlayer(gl);
		
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
		
		// Load textures.
		background.loadTexture(gl, PKEngine.BACKGROUND_LAYER_ONE, PKEngine.context, GL10.GL_REPEAT);
		povMap.loadTexture(gl, PKEngine.POV_MAP, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
		player.loadTexture(gl, PKEngine.PLAYER_SPRITE, PKEngine.context, GL10.GL_CLAMP_TO_EDGE);
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
	
	private void drawPlayer(GL10 gl)
	{
		// Draw player sprite.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glTranslatef(0.5f, 0.5f, 0.0f);
		gl.glScalef(0.5f, 0.2f, 1.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		switch (PKEngine.playerWalkAction)
		{
			case PKEngine.PLAYER_LEFT:
				gl.glTranslatef(0.167f, 0.833f, 0.0f);
				//playerWalkFrames++;
				break;
			case PKEngine.PLAYER_RIGHT:
				gl.glTranslatef(0.833f, 0.833f, 0.0f);
				//playerWalkFrames++;
				break;
			case PKEngine.PLAYER_UP:
				gl.glTranslatef(0.5f, 0.667f, 0.0f);
				//playerWalkFrames++;
				break;
			case PKEngine.PLAYER_DOWN:
				gl.glTranslatef(0.5f, 0.333f, 0.0f);
				//playerWalkFrames++;
				break;
			case PKEngine.PLAYER_STATIONARY:
				gl.glTranslatef(0.333f, 0.833f, 0.0f);
				//playerWalkFrames++;
				break;
			default:
				gl.glTranslatef(0.333f, 0.833f, 0.0f);
				break;
		}
		
		player.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
	
	private void drawPovMap(GL10 gl)
	{
		// Draw PoV map.
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glScalef(1.0f, 1.0f * PKEngine.scrWidth / PKEngine.scrHeight, 1.0f);
		gl.glTranslatef(0.0f, 0.25f * PKEngine.scrHeight / PKEngine.scrWidth, 0.0f);
		
		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		
		switch (PKEngine.playerWalkAction)
		{
			case PKEngine.PLAYER_LEFT:
				if (playerPosition % PKEngine.POV_MAP_WIDTH > 0)
				{
					// Not at left boundary.
					// Move map one block to the right.
				}
				else
				{
					// At left boundary.
					// Do nothing.
				}
				break;
			default:
				gl.glTranslatef(0.0f, 0.0f, 0.0f);
				break;
		}
		
		povMap.draw(gl);
		gl.glPopMatrix();
		gl.glLoadIdentity();
	}
}
