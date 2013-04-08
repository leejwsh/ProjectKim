package com.example.projectkim;

import javax.microedition.khronos.opengles.GL10;

import com.example.framework.gl.Animation;
import com.example.framework.gl.Camera2D;
import com.example.framework.gl.SpriteBatcher;
import com.example.framework.gl.TextureRegion;
import com.example.framework.impl.GLGraphics;

public class WorldRenderer {
    static final float FRUSTUM_WIDTH = 10;
    static final float FRUSTUM_HEIGHT = 15;    
    GLGraphics glGraphics;
    World world;
    Camera2D cam;
    SpriteBatcher batcher;    
    long startTime = System.currentTimeMillis();
    int state = 3;
    boolean renderCoin = false;
    
    public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world) {
        this.glGraphics = glGraphics;
        this.world = world;
        this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.batcher = batcher;        
    }
    
    public void render() {
        if(world.bob.position.y > cam.position.y )
            cam.position.y = world.bob.position.y;
        cam.setViewportAndMatrices();
        renderBackground();      
        renderTimer();
        
        if(renderCoin)  
        renderObjects();
        
    }

    public void renderBackground() {
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(cam.position.x, cam.position.y,
                           FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                           Assets.backgroundRegion);
        batcher.endBatch();
    }
    
    public void renderTimer() {
    	GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
    	if(state==3 && System.currentTimeMillis() - startTime >= 300) {
    		batcher.beginBatch(Assets.items);
        	batcher.drawSprite(5f, 7.5f, 3, 3, Assets.count3);
        	startTime = System.currentTimeMillis();
        	state = 2;
        	batcher.endBatch();
    	}
    	else if(state==2 && System.currentTimeMillis() - startTime >= 1300) {
    		batcher.beginBatch(Assets.items);
        	batcher.drawSprite(5f, 7.5f, 3, 3, Assets.count2);
        	startTime = System.currentTimeMillis();
        	state = 1;
        	batcher.endBatch();
    	}
    	else if(state==1 && System.currentTimeMillis() - startTime >= 2300) {
    		batcher.beginBatch(Assets.items);
        	batcher.drawSprite(5f, 7.5f, 3, 3, Assets.count1);
        	startTime = System.currentTimeMillis();
        	state = 0;
        	batcher.endBatch();
        	renderCoin = true;
    	}
        
  
    	
    	gl.glDisable(GL10.GL_BLEND);
    }

    public void renderObjects() {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(Assets.items);
        renderBob();
        renderItems();
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void renderBob() {
        TextureRegion keyFrame;
        switch(world.bob.state) {
        case Bob.BOB_STATE_FALL:
            keyFrame = Assets.bobFall.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
            break;
        case Bob.BOB_STATE_JUMP:
            keyFrame = Assets.bobJump.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
            break;
        case Bob.BOB_STATE_HIT:
        default:
            keyFrame = Assets.bobHit;                       
        }
        
        float side = world.bob.velocity.x < 0? -1: 1;        
        batcher.drawSprite(world.bob.position.x, world.bob.position.y, side * 1, 1, keyFrame);        
    }

    private void renderItems() {               
        int len = world.coins.size();
        for(int i = 0; i < len; i++) {
            Coin coin = world.coins.get(i);
            TextureRegion keyFrame = Assets.coinAnim.getKeyFrame(coin.stateTime, Animation.ANIMATION_LOOPING);
            batcher.drawSprite(coin.position.x, coin.position.y, 1, 1, keyFrame);
        }
    }
}
