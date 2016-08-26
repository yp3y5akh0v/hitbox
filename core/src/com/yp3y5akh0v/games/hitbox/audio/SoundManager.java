package com.yp3y5akh0v.games.hitbox.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class SoundManager {

    public HashMap<String, Sound> sounds;

    public SoundManager() {
        sounds = new HashMap<>();
    }

    public void loadSound(String key, String extension) {
        String partialPath = key.replaceAll("\\.", "/");
        if (!sounds.containsKey(key))
            sounds.put(key, Gdx.audio.newSound(Gdx.files.internal("sounds/" + partialPath + "." + extension)));
    }

    public Long play(String key) {
        Sound sound = sounds.get(key);
        return sound != null ? sound.play() : null;
    }

    public Long play(String key, float volume, float pitch, float pan) {
        Sound sound = sounds.get(key);
        return sound != null ? sound.play(volume, pitch, pan) : null;
    }

    public void dispose() {
        for (Sound sound : sounds.values())
            sound.dispose();
    }
}
