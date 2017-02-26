package com.yp3y5akh0v.games.hitbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yp3y5akh0v.games.hitbox.HitBox;

public class MenuScreen implements Screen {

    public final HitBox hitBox;
    public Stage stage;
    public Table uiTable;
    public Label gameTitle;
    public TextButton singlePlayerTextButton;
    public TextButton quitTextButton;
    public Skin uiSkin;

    public MenuScreen(final HitBox hitBox) {
        this.hitBox = hitBox;

        stage = new Stage(new ScreenViewport());
        uiSkin = new Skin(Gdx.files.internal("font/default.json"));

        gameTitle = new Label("HitBox", uiSkin, "default");

        singlePlayerTextButton = new TextButton("Single Player", uiSkin, "default");
        singlePlayerTextButton.setTouchable(Touchable.enabled);
        singlePlayerTextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hitBox.setScreen(new GameScreen("000", hitBox));
            }
        });

        quitTextButton = new TextButton("Quit", uiSkin, "default");
        quitTextButton.setTouchable(Touchable.enabled);
        quitTextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        uiTable = new Table();
        uiTable.setWidth(stage.getWidth());
        uiTable.setFillParent(true);
        uiTable.row();
        uiTable.add(gameTitle);
        uiTable.row();
        uiTable.add(singlePlayerTextButton);
        uiTable.row();
        uiTable.add(quitTextButton);
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
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
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
