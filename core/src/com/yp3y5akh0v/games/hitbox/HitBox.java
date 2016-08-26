package com.yp3y5akh0v.games.hitbox;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Array;
import com.yp3y5akh0v.games.hitbox.audio.MusicManager;
import com.yp3y5akh0v.games.hitbox.audio.SoundManager;
import com.yp3y5akh0v.games.hitbox.screens.MenuScreen;

/**
 * Game HitBox
 *
 * @author Yuriy Peysakhov
 * @version 1.0.1
 **/
public class HitBox extends Game {

    public static final String TITLE = "HitBox";
    public static final int GAME_WIDTH = 1024;
    public static final int GAME_HEIGHT = 800;

    public SoundManager soundManager;
    public MusicManager musicManager;
    public Array<Long> timeScore;

    @Override
    public void create() {

        // time score array for statistics in development process...
        timeScore = new Array<>();

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

        setScreen(new MenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        soundManager.dispose();
        musicManager.dispose();
    }
}