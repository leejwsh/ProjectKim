package com.example.projectkim;

import com.example.framework.DynamicGameObject;

public class FCKim extends DynamicGameObject
{
    public static final int KIM_STATE_JUMP = 0;
    public static final int KIM_STATE_FALL = 1;
    public static final int KIM_STATE_HIT = 2;
    public static final float KIM_JUMP_VELOCITY = 11;    
    public static final float KIM_MOVE_VELOCITY = 20;
    public static final float KIM_WIDTH = 0.8f;
    public static final float KIM_HEIGHT = 0.8f;

    int state;
    float stateTime;    

    public FCKim(float x, float y)
    {
        super(x, y, KIM_WIDTH, KIM_HEIGHT);
        state = KIM_STATE_FALL;
        stateTime = 0;        
    }

    public void update(float deltaTime)
    {     
        velocity.add(FCWorld.gravity.x * deltaTime, FCWorld.gravity.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
        
        if (velocity.y > 0 && state != KIM_STATE_HIT)
        {
            if (state != KIM_STATE_JUMP)
            {
                state = KIM_STATE_JUMP;
                stateTime = 0;
            }
        }
        
        if (velocity.y < 0 && state != KIM_STATE_HIT)
        {
            if (state != KIM_STATE_FALL)
            {
                state = KIM_STATE_FALL;
                stateTime = 0;
            }
        }
        
        if (position.x < 0)
            position.x = FCWorld.WORLD_WIDTH;
        if (position.x > FCWorld.WORLD_WIDTH)
            position.x = 0;
        
        stateTime += deltaTime;
    }

    /*public void hitSquirrel() {
        velocity.set(0,0);
        state = BOB_STATE_HIT;        
        stateTime = 0;
    }*/
    
    public void hitPlatform()
    {
        velocity.y = KIM_JUMP_VELOCITY;
        state = KIM_STATE_JUMP;
        stateTime = 0;
    }

    /*public void hitSpring()
    {
        velocity.y = BOB_JUMP_VELOCITY * 1.5f;
        state = BOB_STATE_JUMP;
        stateTime = 0;   
    }*/
}
