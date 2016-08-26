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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.yp3y5akh0v.games.hitbox.HitBox;

public class PauseScreen implements Screen {

    public final HitBox hitbox;
    public final Screen parent;

    public Stage stage;

    public Label pausedLabel;
    public Label timeElapsedLabel;

    public TextButton resumeTextButton;
    public TextButton goToMenuTextButton;

    public Table uiTable;
    public Skin uiSkin;

    public PauseScreen(final HitBox hitbox, final Screen parent) {
        this.hitbox = hitbox;
        this.parent = parent;

        stage = new Stage(new ScreenViewport());

        uiSkin = new Skin(Gdx.files.internal("font/default.json"));

        pausedLabel = new Label("Paused", uiSkin, "default");

        timeElapsedLabel = new Label("Time elapsed: " + (long) ((GameScreen) parent)
                .timeElapsed + " s", uiSkin, "default");
        timeElapsedLabel.setAlignment(Align.center);

        resumeTextButton = new TextButton("Resume", uiSkin, "default");
        resumeTextButton.setTouchable(Touchable.enabled);
        resumeTextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((GameScreen) parent).isPaused = false;
                hitbox.setScreen(parent);
            }
        });

        goToMenuTextButton = new TextButton("Go to menu", uiSkin, "default");
        goToMenuTextButton.setTouchable(Touchable.enabled);
        goToMenuTextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hitbox.musicManager.stop("game");
                // clear time score, need to modify
                hitbox.timeScore.clear();
                hitbox.setScreen(new MenuScreen(hitbox));
            }
        });

        uiTable = new Table();
        uiTable.setWidth(stage.getWidth());
        uiTable.setFillParent(true);
        uiTable.add(pausedLabel).colspan(2);
        uiTable.row();
        uiTable.add(resumeTextButton);
        uiTable.add(goToMenuTextButton);
        uiTable.row();
        uiTable.add(timeElapsedLabel).colspan(2).center().padTop(50);
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ((GameScreen) parent).isPaused = false;
            hitbox.setScreen(parent);
        }
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
