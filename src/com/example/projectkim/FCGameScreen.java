package com.example.projectkim;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.example.framework.Game;
import com.example.framework.Input.TouchEvent;
import com.example.framework.gl.Camera2D;
import com.example.framework.gl.SpriteBatcher;
import com.example.framework.impl.GLScreen;
import com.example.framework.math.OverlapTester;
import com.example.framework.math.Rectangle;
import com.example.framework.math.Vector2;
import com.example.projectkim.FCWorld.WorldListener;

public class FCGameScreen extends GLScreen
{
    static final int GAME_READY = 0;    
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;
  
    int state;
    Camera2D guiCam;
    Vector2 touchPoint;
    SpriteBatcher batcher;    
    FCWorld world;
    WorldListener worldListener;
    FCWorldRenderer renderer;    
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle quitBounds;
    int lastScore;
    String scoreString;
    boolean startBlinkTime = false;
    boolean startCountDownTimer = false;
    long countDownTime;
    long blinkTime;
    boolean blink = false;
    int timer = 0;

    public FCGameScreen(Game game)
    {
        super(game);
        state = GAME_READY;
        guiCam = new Camera2D(glGraphics, 320, 480);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 1000);
        worldListener = new WorldListener()
        {
            public void jump()
            {            
                FCAssets.playSound(FCAssets.jumpSound);
            }

            public void highJump()
            {
                FCAssets.playSound(FCAssets.highJumpSound);
            }

            public void hit()
            {
                FCAssets.playSound(FCAssets.hitSound);
            }

            public void coin()
            {
                FCAssets.playSound(FCAssets.coinSound);
            }                      
        };
        world = new FCWorld(worldListener);
        renderer = new FCWorldRenderer(glGraphics, batcher, world);
        pauseBounds = new Rectangle(320- 64, 480- 64, 64, 64);
        resumeBounds = new Rectangle(160 - 96, 240, 192, 36);
        quitBounds = new Rectangle(160 - 96, 240 - 36, 192, 36);
        lastScore = 0;
        scoreString = "Score: 0";
    }

    @Override
    public void update(float deltaTime)
    {
        if (deltaTime > 0.1f)
            deltaTime = 0.1f;
        
        switch(state)
        {
	        case GAME_READY:
	        	if (!startCountDownTimer)
	        	{
	        		countDownTime = System.currentTimeMillis();
	        		startCountDownTimer = true;
	        	}
	            updateReady();
	            break;
	        case GAME_RUNNING:
	            updateRunning(deltaTime);
	            break;
	        /*case GAME_PAUSED:
	            updatePaused();
	            break;*/
	        /*case GAME_LEVEL_END:
	            updateLevelEnd();
	            break;*/
	        case GAME_OVER:
	        	if (!startBlinkTime)
	        	{
	        		blinkTime = System.currentTimeMillis();
	        		startBlinkTime = true;
	        	}
	            updateGameOver();
	            break;
        }
    }

    private void updateReady()
    {
 		state = GAME_RUNNING;
    }

    private void updateRunning(float deltaTime)
    {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {
            TouchEvent event = touchEvents.get(i);
            if (event.type != TouchEvent.TOUCH_UP)
                continue;
            
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if (OverlapTester.pointInRectangle(pauseBounds, touchPoint)){}  
        }
        
        world.update(deltaTime, game.getInput().getAccelX(), game.getTime());
        if (world.score != lastScore)
        {
            lastScore = world.score;
            scoreString = "" + lastScore;
        }
        if (world.state == FCWorld.WORLD_STATE_NEXT_LEVEL)
        {
            state = GAME_LEVEL_END;        
        }
        if (world.state == FCWorld.WORLD_STATE_GAME_OVER)
        {
            state = GAME_OVER;
            scoreString = "Your Score: " + lastScore;
            FCSettings.addScore(lastScore);
            FCSettings.save(game.getFileIO());
        }
    }

    // Not implemented.
    /*private void updatePaused()
    {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {
            TouchEvent event = touchEvents.get(i);
            if (event.type != TouchEvent.TOUCH_UP)
                continue;
            
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if (OverlapTester.pointInRectangle(resumeBounds, touchPoint))
            {
                FCAssets.playSound(FCAssets.clickSound);
                state = GAME_RUNNING;
                return;
            }
            
            if(OverlapTester.pointInRectangle(quitBounds, touchPoint))
            {
                FCAssets.playSound(FCAssets.clickSound);
                //game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
    }*/

    // Not implemented
    /*private void updateLevelEnd()
    {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {                   
            TouchEvent event = touchEvents.get(i);
            if (event.type != TouchEvent.TOUCH_UP)
                continue;
            world = new FCWorld(worldListener);
            renderer = new FCWorldRenderer(glGraphics, batcher, world);
            world.score = lastScore;
            state = GAME_READY;
        }
    }*/

    private void updateGameOver()
    {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for (int i = 0; i < len; i++)
        {                   
            TouchEvent event = touchEvents.get(i);
            if (event.type != TouchEvent.TOUCH_UP)
                continue;
            int currentScore = PKEngine.client.getPlayerScore(PKEngine.playerID);
            currentScore += lastScore;
            // Updates the current score to the server.
            try
            {
				PKEngine.client.scoreUpdateEvent(PKEngine.playerID, currentScore);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
            game.killGame();
        }
        
        // Controls blinking of text.
        if (System.currentTimeMillis() - blinkTime >= 500)
        {
        	blink = !blink;
        	blinkTime = System.currentTimeMillis();
        }
    }

    @Override
    public void present(float deltaTime)
    {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        renderer.render();
        
        guiCam.setViewportAndMatrices();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(FCAssets.items);
        switch(state)
        {
	        /*case GAME_READY:
	            presentReady();
	            break;*/
	        case GAME_RUNNING:
	            presentRunning();
	            break;
	        /*case GAME_PAUSED:
	        	presentPaused(); 
	        	break;*/
	        /*case GAME_LEVEL_END:
	            presentLevelEnd();
	            break;*/
	        case GAME_OVER:
	            presentGameOver();
	            break;
        }
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }
    
    // Not implemented.
    /*private void presentReady() {}*/

    private void presentRunning()
    {
        batcher.drawSprite(320 - 32, 480 - 32, 64, 64, FCAssets.pause);
        FCAssets.font.drawText(batcher, scoreString, 16, 480-20);
        FCAssets.font.drawText(batcher, "Time: ", 200, 460);
        FCAssets.font.drawText(batcher, String.format("%02d", game.getTime()), 280, 460);
    }

    // Not implemented
    /*private void presentPaused()
    {        
        batcher.drawSprite(160, 240, 192, 96, FCAssets.pauseMenu);
        FCAssets.font.drawText(batcher, scoreString, 16, 480-20);
    }*/
    
    // Not implemented
    /*private void presentLevelEnd()
    {
        String topText = "the princess is ...";
        String bottomText = "in another castle!";
        float topWidth = FCAssets.font.glyphWidth * topText.length();
        float bottomWidth = FCAssets.font.glyphWidth * bottomText.length();
        FCAssets.font.drawText(batcher, topText, 160 - topWidth / 2, 480 - 40);
        FCAssets.font.drawText(batcher, bottomText, 160 - bottomWidth / 2, 40);
    }*/

    private void presentGameOver()
    {     
        float scoreWidth = FCAssets.font.glyphWidth * scoreString.length();
        FCAssets.font.drawText(batcher, scoreString + ".", 160 - scoreWidth / 2, 220);
        if (blink)
        	FCAssets.font.drawText(batcher, "Tap To Exit", 140 - scoreWidth / 2, 130);
    }

    @Override
    public void pause()
    {
    	if(state == GAME_RUNNING)
    		state = GAME_PAUSED;
    }

    @Override
    public void resume() {}

    @Override
    public void dispose() {}
}