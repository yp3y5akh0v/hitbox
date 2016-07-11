package com.yp3y5akh0v.games.hitbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.yp3y5akh0v.games.hitbox.audio.MusicManager;
import com.yp3y5akh0v.games.hitbox.audio.SoundManager;
import com.yp3y5akh0v.games.hitbox.screens.MenuScreen;
import com.yp3y5akh0v.games.hitbox.screens.ScreenManager;

/**
 * Game HitBox
 *
 * @author Yuriy Peysakhov
 * @version 1.0.0
 **/
public class HitBox extends Game {

    public static final String TITLE = "HitBox";
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 640;

    public SoundManager soundManager;

    public MusicManager musicManager;

    public Array<Long> timeScore;

    public ScreenManager screenManager;

    @Override
    public void create() {

        // time score array for statistics in development process...
        timeScore = new Array<>();

        screenManager = new ScreenManager();

        soundManager = new SoundManager();

        soundManager.loadSound("player.death", "mp3");
        soundManager.loadSound("player.push", "mp3");
        soundManager.loadSound("player.walk", "mp3");

        soundManager.loadSound("bot.death", "mp3");
        soundManager.loadSound("bot.scream", "mp3");
        soundManager.loadSound("bot.eat", "mp3");

        soundManager.loadSound("win", "mp3");

        musicManager = new MusicManager();

        musicManager.loadMusic("game", "mp3");

        Gdx.graphics.setResizable(false);
        Gdx.graphics.setVSync(true);

        screenManager.setScreen(new MenuScreen(this));
        setScreen(screenManager.peek());
    }

    @Override
    public void dispose() {
        super.dispose();
        soundManager.dispose();
        musicManager.dispose();
    }
}