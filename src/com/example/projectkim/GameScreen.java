package com.example.projectkim;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.example.framework.Game;
import com.example.framework.Input.TouchEvent;
import com.example.framework.gl.Camera2D;
import com.example.framework.gl.FPSCounter;
import com.example.framework.gl.SpriteBatcher;
import com.example.framework.impl.GLScreen;
import com.example.framework.math.OverlapTester;
import com.example.framework.math.Rectangle;
import com.example.framework.math.Vector2;
import com.example.projectkim.World.WorldListener;

public class GameScreen extends GLScreen {
    static final int GAME_READY = 0;    
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;
  
    int state;
    Camera2D guiCam;
    Vector2 touchPoint;
    SpriteBatcher batcher;    
    World world;
    WorldListener worldListener;
    WorldRenderer renderer;    
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

    public GameScreen(Game game) {
        super(game);
        state = GAME_READY;
        guiCam = new Camera2D(glGraphics, 320, 480);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 1000);
        worldListener = new WorldListener() {
            public void jump() {            
                Assets.playSound(Assets.jumpSound);
            }

            public void highJump() {
                Assets.playSound(Assets.highJumpSound);
            }

            public void hit() {
                Assets.playSound(Assets.hitSound);
            }

            public void coin() {
                Assets.playSound(Assets.coinSound);
            }                      
        };
        world = new World(worldListener);
        renderer = new WorldRenderer(glGraphics, batcher, world);
        pauseBounds = new Rectangle(320- 64, 480- 64, 64, 64);
        resumeBounds = new Rectangle(160 - 96, 240, 192, 36);
        quitBounds = new Rectangle(160 - 96, 240 - 36, 192, 36);
        lastScore = 0;
        scoreString = "score: 0";
    }

    @Override
    public void update(float deltaTime) {
        if(deltaTime > 0.1f)
            deltaTime = 0.1f;
        
        switch(state) {
        case GAME_READY:
        	if (!startCountDownTimer){
        		countDownTime = System.currentTimeMillis();
        		startCountDownTimer = true;
        	}
            updateReady();
            break;
        case GAME_RUNNING:
            updateRunning(deltaTime);
            break;
       // case GAME_PAUSED:
       //     updatePaused();
       //     break;
        case GAME_LEVEL_END:
            updateLevelEnd();
            break;
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

    private void updateReady() {
 		state = GAME_RUNNING;
    }

    private void updateRunning(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if(OverlapTester.pointInRectangle(pauseBounds, touchPoint)) {
             //   Assets.playSound(Assets.clickSound);
             //   state = GAME_PAUSED;
             //   return;
            }            
        }
        
        world.update(deltaTime, game.getInput().getAccelX(), game.getTime());
        if(world.score != lastScore) {
            lastScore = world.score;
            scoreString = "" + lastScore;
        }
        if(world.state == World.WORLD_STATE_NEXT_LEVEL) {
            state = GAME_LEVEL_END;        
        }
        if(world.state == World.WORLD_STATE_GAME_OVER) {
            state = GAME_OVER;
            scoreString = "Your Score: " + lastScore;
            Settings.addScore(lastScore);
            Settings.save(game.getFileIO());
        }
    }

    private void updatePaused() {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if(OverlapTester.pointInRectangle(resumeBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_RUNNING;
                return;
            }
            
            if(OverlapTester.pointInRectangle(quitBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
            //    game.setScreen(new MainMenuScreen(game));
                return;
            
            }
        }
    }

    private void updateLevelEnd() {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {                   
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            world = new World(worldListener);
            renderer = new WorldRenderer(glGraphics, batcher, world);
            world.score = lastScore;
            state = GAME_READY;
        }
    }

    private void updateGameOver() {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {                   
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            int currentScore = PKEngine.client.getPlayerScore(PKEngine.PLAYER_ID);
            currentScore += lastScore;
            try {
				PKEngine.client.scoreUpdateEvent(PKEngine.PLAYER_ID, currentScore);
			} catch (Exception e) {
				e.printStackTrace();
			}
            game.killGame();
        }
        
        // Controls blinking of text
        if (System.currentTimeMillis() - blinkTime >= 500)
        {
        	blink = !blink;
        	blinkTime = System.currentTimeMillis();
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        renderer.render();
        
        guiCam.setViewportAndMatrices();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(Assets.items);
        switch(state) {
        case GAME_READY:
            presentReady();
            break;
        case GAME_RUNNING:
            presentRunning();
            break;
        //case GAME_PAUSED:
        //    presentPaused(); 
       //     break;
        case GAME_LEVEL_END:
            presentLevelEnd();
            break;
        case GAME_OVER:
            presentGameOver();
            break;
        }
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void presentReady() {

    }

    private void presentRunning() {
        batcher.drawSprite(320 - 32, 480 - 32, 64, 64, Assets.pause);
        Assets.font.drawText(batcher, scoreString, 16, 480-20);
/*        try {
			PKEngine.client.mapUpdateEvent(PKEngine.PLAYER_ID);
			timer = game.getTime();
			//timer = PKEngine.client.getCurrentMiniGameTime();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
        Assets.font.drawText(batcher, "Time: ", 200, 460);
        Assets.font.drawText(batcher, String.format("%02d", game.getTime()), 280, 460);
    }

    //not implemented
    private void presentPaused() {        
        batcher.drawSprite(160, 240, 192, 96, Assets.pauseMenu);
        Assets.font.drawText(batcher, scoreString, 16, 480-20);
    }

    private void presentLevelEnd() {
        String topText = "the princess is ...";
        String bottomText = "in another castle!";
        float topWidth = Assets.font.glyphWidth * topText.length();
        float bottomWidth = Assets.font.glyphWidth * bottomText.length();
        Assets.font.drawText(batcher, topText, 160 - topWidth / 2, 480 - 40);
        Assets.font.drawText(batcher, bottomText, 160 - bottomWidth / 2, 40);
    }

    private void presentGameOver() {     
        float scoreWidth = Assets.font.glyphWidth * scoreString.length();
        Assets.font.drawText(batcher, scoreString + ".", 160 - scoreWidth / 2, 220);
        if (blink)
        	Assets.font.drawText(batcher, "Tap To Exit", 140 - scoreWidth / 2, 130);
    }

    @Override
    public void pause() {
      //  if(state == GAME_RUNNING)
      //      state = GAME_PAUSED;
    }

    @Override
    public void resume() {        
    }

    @Override
    public void dispose() {       
    }
}