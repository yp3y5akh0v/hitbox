package com.yp3y5akh0v.games.hitbox.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.user.client.Window;
import com.yp3y5akh0v.games.hitbox.HitBox;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(HitBox.GAME_WIDTH, HitBox.GAME_HEIGHT);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new HitBox();
    }

    @Override
    public void exit() {
        Window.alert("You can only exit the application, just hit close tab...");
    }
}