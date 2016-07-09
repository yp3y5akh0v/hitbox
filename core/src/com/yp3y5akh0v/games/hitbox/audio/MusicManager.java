package com.yp3y5akh0v.games.hitbox.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import java.util.HashMap;

public class MusicManager {

    public HashMap<String, Music> musics;

    public MusicManager() {
        musics = new HashMap<>();
    }

    public void loadMusic(String key, String extension) {
        String partialPath = key.replaceAll("\\.", "/");
        if (!musics.containsKey(key))
            musics.put(key, Gdx.audio.newMusic(Gdx.files.internal("musics/" + partialPath + "." + extension)));
    }

    public void play(String key) {
        musics.get(key).play();
    }

    public void setLooping(String key, boolean isLooping) {
        musics.get(key).setLooping(isLooping);
    }

    public void removeMusic(String key) {
        Music music = musics.get(key);
        if (music != null) {
            music.dispose();
            musics.remove(key);
        }
    }

    public void setVolume(String key, float volume) {
        musics.get(key).setVolume(volume);
    }

    public void stop(String key) {
        musics.get(key).stop();
    }

    public Music getMusic(String key) {
        return musics.get(key);
    }

    public void dispose() {
        for (Music music : musics.values())
            music.dispose();
    }
}
