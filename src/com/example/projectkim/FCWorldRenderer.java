package com.example.projectkim;

import javax.microedition.khronos.opengles.GL10;

import com.example.framework.gl.Animation;
import com.example.framework.gl.Camera2D;
import com.example.framework.gl.SpriteBatcher;
import com.example.framework.gl.TextureRegion;
import com.example.framework.impl.GLGraphics;

public class FCWorldRenderer
{
    static final float FRUSTUM_WIDTH = 10;
    static final float FRUSTUM_HEIGHT = 15;    
    GLGraphics glGraphics;
    FCWorld world;
    Camera2D cam;
    SpriteBatcher batcher;    
    long startTime = System.currentTimeMillis();
    int state = 3;
    boolean renderCoin = false;
    
    public FCWorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, FCWorld world)
    {
        this.glGraphics = glGraphics;
        this.world = world;
        this.cam = new Camera2D(glGraphics, FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
        this.batcher = batcher;        
    }
    
    public void render()
    {
        if (world.kim.position.y > cam.position.y)
            cam.position.y = world.kim.position.y;
        cam.setViewportAndMatrices();
        renderBackground();      
        renderTimer();
        
        if (renderCoin)  
        	renderObjects();
        
    }

    public void renderBackground()
    {
        batcher.beginBatch(FCAssets.background);
        batcher.drawSprite(cam.position.x, cam.position.y,
                           FRUSTUM_WIDTH, FRUSTUM_HEIGHT, 
                           FCAssets.backgroundRegion);
        batcher.endBatch();
    }
    
    public void renderTimer()
    {
    	GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        long currentTime = System.currentTimeMillis() - startTime;
        
    	if (currentTime <= 1000)
    	{
    		batcher.beginBatch(FCAssets.items);
        	batcher.drawSprite(5f, 7.5f, 3, 3, FCAssets.count3);
        	batcher.endBatch();
    	}
    	else if (currentTime > 1000 && currentTime <= 2000)
    	{
    		batcher.beginBatch(FCAssets.items);
        	batcher.drawSprite(5f, 7.5f, 3, 3, FCAssets.count2);
        	batcher.endBatch();
    	}
    	else if (currentTime > 2000 && currentTime <= 3000)
    	{
    		batcher.beginBatch(FCAssets.items);
        	batcher.drawSprite(5f, 7.5f, 3, 3, FCAssets.count1);
        	batcher.endBatch();
    	} else
        	renderCoin = true;
    	
    	gl.glDisable(GL10.GL_BLEND);
    }

    public void renderObjects()
    {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(FCAssets.items);
        renderKim();
        renderItems();
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void renderKim()
    {
        TextureRegion keyFrame;
        switch(world.kim.state)
        {
	        case FCKim.KIM_STATE_FALL:
	            keyFrame = FCAssets.bobFall.getKeyFrame(world.kim.stateTime, Animation.ANIMATION_LOOPING);
	            break;
	        case FCKim.KIM_STATE_JUMP:
	            keyFrame = FCAssets.bobJump.getKeyFrame(world.kim.stateTime, Animation.ANIMATION_LOOPING);
	            break;
	        case FCKim.KIM_STATE_HIT:
	        default:
	            keyFrame = FCAssets.bobHit;
        }
        
        float side = world.kim.velocity.x < 0? -1: 1;        
        batcher.drawSprite(world.kim.position.x, world.kim.position.y, side * 1, 1, keyFrame);        
    }

    private void renderItems()
    {               
        int len = world.coins.size();
        for(int i = 0; i < len; i++)
        {
            FCCoin coin = world.coins.get(i);
            TextureRegion keyFrame = FCAssets.coinAnim.getKeyFrame(coin.stateTime, Animation.ANIMATION_LOOPING);
            batcher.drawSprite(coin.position.x, coin.position.y, 1, 1, keyFrame);
        }
    }
}
