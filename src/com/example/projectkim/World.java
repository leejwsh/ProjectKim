package com.example.projectkim;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.framework.math.Vector2;

public class World {
    public interface WorldListener {
        public void jump();
        public void highJump();
        public void hit();
        public void coin();
    }

    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 15 * 20;    
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;
    public static final Vector2 gravity = new Vector2(0, 0);

    public final Bob bob;           
    public final List<Coin> coins; 
    public final WorldListener listener;
    public final Random rand;
    
    public float heightSoFar;
    public int score;    
    public int state;

    public World(WorldListener listener) {
        this.bob = new Bob(5, 1);        
        this.coins = new ArrayList<Coin>();        
        this.listener = listener;
        rand = new Random();
        generateLevel();
        
        this.heightSoFar = 0;
        this.score = 0;
        this.state = WORLD_STATE_RUNNING;
    }

    private void generateLevel() {
    	for(int i=0; i<10; i++) {
    		Coin coin = new Coin(i * 1, 17); 
    		coins.add(coin);
    	}

    }

    public void update(float deltaTime, float accelX) {
        updateBob(deltaTime, accelX);
        updateCoins(deltaTime );
        if (bob.state != Bob.BOB_STATE_HIT)
            checkCollisions();
        checkGameOver();
    }

    private void updateBob(float deltaTime, float accelX) {
        if (bob.state != Bob.BOB_STATE_HIT && bob.position.y <= 0.5f)
            bob.hitPlatform();
        if (bob.state != Bob.BOB_STATE_HIT)
            bob.velocity.x = -accelX / 10 * Bob.BOB_MOVE_VELOCITY;
        bob.update(deltaTime);
        heightSoFar = 0; //Math.max(bob.position.y, heightSoFar);
    }

    private void updateCoins(float deltaTime) {
        int len = coins.size();
        for (int i = 0; i < len; i++) {
         
        	coins.get(i).update(deltaTime);
            coins.get(i).position.x = i * 1;
            
            if(coins.get(i).position.y <= 1.0f) {
            	coins.get(i).position.y = 17;           
            }
            
            //control dropping speed at bottom half of screen
            if(coins.get(i).position.y <= 6.0f && coins.get(i).position.y >= 5.0f){
            	coins.get(i).position.y = coins.get(i).position.y - 0.1f;
            }
            if(coins.get(i).position.y <= 5.0f && coins.get(i).position.y >= 4.0f){
            	coins.get(i).position.y = coins.get(i).position.y - 0.15f;
            }
            if(coins.get(i).position.y <= 4.0f && coins.get(i).position.y >= 3.0f){
            	coins.get(i).position.y = coins.get(i).position.y - 0.2f;
            }
            if(coins.get(i).position.y <= 3.0f && coins.get(i).position.y >= 2.0f){
            	coins.get(i).position.y = coins.get(i).position.y - 0.25f;
            }
            if(coins.get(i).position.y <= 2.0f && coins.get(i).position.y >= 1.0f){
            	coins.get(i).position.y = coins.get(i).position.y - 0.3f;
            }
            
        }
    }

    private void checkCollisions() {
        checkItemCollisions(); 
    }


    private void checkItemCollisions() {
        int len = coins.size();
        for (int i = 0; i < len; i++) {

        	float diffX;
        	float diffY;
        	
        	if(coins.get(i).position.x - bob.position.x >= 0) {
        		diffX = coins.get(i).position.x - bob.position.x;
        	}
        	else {
        		diffX = bob.position.x - coins.get(i).position.x;
        	}
        	
        	if(coins.get(i).position.y - bob.position.y >= 0) {
            	diffY = coins.get(i).position.y - bob.position.y;
            }
            else {
            	diffY = bob.position.y - coins.get(i).position.y;
            }
        	
        	if(diffX <= 0.3f && diffY <= 0.3f) {
        		coins.get(i).position.y = 15;
                listener.coin();
                score += Coin.COIN_SCORE;
            }

        }

        if (bob.velocity.y > 0)
            return;        
    }


    private void checkGameOver() {
  /*      if (heightSoFar - 7.5f > bob.position.y) {
            state = WORLD_STATE_GAME_OVER;
        }
  */  }
}
