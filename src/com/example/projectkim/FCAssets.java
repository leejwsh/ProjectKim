package com.example.projectkim;

import com.example.framework.Music;
import com.example.framework.Sound;
import com.example.framework.gl.Animation;
import com.example.framework.gl.Font;
import com.example.framework.gl.Texture;
import com.example.framework.gl.TextureRegion;
import com.example.framework.impl.GLGame;

public class FCAssets
{
    public static Texture background;
    public static TextureRegion backgroundRegion;
    
    public static Texture items;        
    public static TextureRegion mainMenu;
    public static TextureRegion pauseMenu;
    public static TextureRegion ready;
    public static TextureRegion gameOver;
    public static TextureRegion highScoresRegion;
    public static TextureRegion logo;
    public static TextureRegion soundOn;
    public static TextureRegion soundOff;
    public static TextureRegion arrow;
    public static TextureRegion pause;    
    public static Animation coinAnim;
    public static Animation bobJump;
    public static Animation bobFall;
    public static TextureRegion bobHit;  
    public static Font font;
    
    public static TextureRegion count3;
    public static TextureRegion count2;
    public static TextureRegion count1;
    public static TextureRegion go;
    
    public static Music music;
    public static Sound jumpSound;
    public static Sound highJumpSound;
    public static Sound hitSound;
    public static Sound coinSound;
    public static Sound clickSound;

    public static void load(GLGame game)
    {
        background = new Texture(game, "background.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
        
        items = new Texture(game, "items.png");        
        mainMenu = new TextureRegion(items, 0, 224, 300, 110);
        pauseMenu = new TextureRegion(items, 0, 0, 1, 1);
        ready = new TextureRegion(items, 320, 224, 192, 32);
        gameOver = new TextureRegion(items, 352, 256, 160, 96);
        highScoresRegion = new TextureRegion(FCAssets.items, 0, 257, 300, 110 / 3);
        logo = new TextureRegion(items, 0, 352, 274, 142);
        soundOff = new TextureRegion(items, 0, 0, 64, 64);
        soundOn = new TextureRegion(items, 64, 0, 64, 64);
        arrow = new TextureRegion(items, 0, 64, 64, 64);
        pause = new TextureRegion(items, 0, 0, 1, 1);

        coinAnim = new Animation(0.2f,                                 
                                 new TextureRegion(items, 128, 32, 32, 36),
                                 new TextureRegion(items, 160, 32, 32, 36),
                                 new TextureRegion(items, 192, 32, 32, 36),
                                 new TextureRegion(items, 160, 32, 32, 36));
        bobJump = new Animation(0.2f,
                                new TextureRegion(items, 0, 130, 34, 36),
                                new TextureRegion(items, 35, 130, 34, 36),
                                new TextureRegion(items, 69, 130, 34, 36),
                                new TextureRegion(items, 103, 130, 34, 36));
        bobFall = new Animation(0.2f,
        						new TextureRegion(items, 0, 130, 34, 36),
        						new TextureRegion(items, 35, 130, 34, 36),
        						new TextureRegion(items, 69, 130, 34, 36),
        						new TextureRegion(items, 103, 130, 34, 36));
        bobHit = new TextureRegion(items, 0, 130, 34, 36);
        
        count1 = new TextureRegion(items, 0, 176, 85, 85 );
        count2 = new TextureRegion(items, 90, 176, 85, 85 );
        count3 = new TextureRegion(items, 184, 176, 85, 85 );
        go     = new TextureRegion(items, 0, 261, 277, 91 );
        
        font = new Font(items, 228, 6, 16, 16, 20);
        
        music = game.getAudio().newMusic("music.mp3");
        music.setLooping(true);
        music.setVolume(0.5f);
        if (FCSettings.soundEnabled)
            music.play();
        jumpSound = game.getAudio().newSound("jump.ogg");
        highJumpSound = game.getAudio().newSound("highjump.ogg");
        hitSound = game.getAudio().newSound("hit.ogg");
        coinSound = game.getAudio().newSound("coin.ogg");
        clickSound = game.getAudio().newSound("click.ogg");       
    }       

    public static void reload()
    {
        background.reload();
        items.reload();
        if (FCSettings.soundEnabled)
            music.play();
    }

    public static void playSound(Sound sound)
    {
        if (FCSettings.soundEnabled)
            sound.play(1);
    }
}
