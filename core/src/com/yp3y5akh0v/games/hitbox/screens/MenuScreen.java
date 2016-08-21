package com.yp3y5akh0v.games.hitbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yp3y5akh0v.games.hitbox.HitBox;

import java.util.Calendar;

public class MenuScreen implements Screen {

    public final HitBox hitBox;
    public Stage stage;
    public Table uiTable;
    public Label gameTitle;
    public TextButton playTextButton;
    public TextButton quitTextButton;
    public Label copyRightLabel;
    public Skin uiSkin;

    public MenuScreen(final HitBox hitBox) {
        this.hitBox = hitBox;

//      Create stage
        stage = new Stage(new ScreenViewport());
        uiSkin = new Skin(Gdx.files.internal("font/default.json"));

//        Create game title
        gameTitle = new Label("HitBox", uiSkin, "default");

//        Create play text button
        playTextButton = new TextButton("Play", uiSkin, "default");
        playTextButton.setTouchable(Touchable.enabled);
        playTextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hitBox.screenManager.setScreen(new GameScreen("000", hitBox));
                hitBox.setScreen(hitBox.screenManager.peek());
            }
        });

//        Create quit text button
        quitTextButton = new TextButton("Quit", uiSkin, "default");
        quitTextButton.setTouchable(Touchable.enabled);
        quitTextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

//        Create copyright label
        copyRightLabel = new Label("Copyright " + Calendar.getInstance().get(Calendar.YEAR) +
                " HitBox. All rights reserved.",
                uiSkin, "default");
        copyRightLabel.setAlignment(Align.center);

        uiTable = new Table();
        uiTable.setWidth(stage.getWidth());
        uiTable.setFillParent(true);
        uiTable.add(gameTitle).colspan(2);
        uiTable.row();
        uiTable.add(playTextButton);
        uiTable.add(quitTextButton);
        uiTable.row();
        uiTable.add(copyRightLabel).colspan(2).right().padTop(50);
        stage.addActor(uiTable);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();
    }

    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        uiSkin.dispose();
    }
}
