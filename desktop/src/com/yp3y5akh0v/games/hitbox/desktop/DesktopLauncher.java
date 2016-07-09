package com.yp3y5akh0v.games.hitbox.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yp3y5akh0v.games.hitbox.HitBox;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = HitBox.TITLE;
        config.backgroundFPS = 60;
        config.foregroundFPS = 60;
        config.width = HitBox.GAME_WIDTH;
        config.height = HitBox.GAME_HEIGHT;
        new LwjglApplication(new HitBox(), config);
    }
}
