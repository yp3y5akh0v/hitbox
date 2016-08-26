package com.yp3y5akh0v.games.hitbox.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yp3y5akh0v.games.hitbox.HitBox;
import com.yp3y5akh0v.games.hitbox.actors.Block;
import com.yp3y5akh0v.games.hitbox.actors.Bot;
import com.yp3y5akh0v.games.hitbox.actors.Box;
import com.yp3y5akh0v.games.hitbox.actors.Player;
import com.yp3y5akh0v.games.hitbox.handlers.ContactEvent;
import com.yp3y5akh0v.games.hitbox.handlers.ContactListener;
import com.yp3y5akh0v.games.hitbox.handlers.PushEvent;
import com.yp3y5akh0v.games.hitbox.handlers.PushListener;

import java.util.HashMap;

public class GameScreen implements Screen, ContactListener, PushListener {

    public final HitBox hitbox;

    public Stage gameStage;
    public Stage uiStage;
    public Stage winStage;
    public Stage gameOverStage;

    public Skin uiSkin;

    public Player player;
    public Array<Bot> bots;
    public Array<Block> blocks;
    public Array<Box> boxes;

    public Label countBoxLabel;
    public Label countBotLabel;
    public Label roundLabel;
    public Label winLabel;
    public Label gameOverLabel;

    public TextButton imDoneTextButton;
    public TextButton nextTextButton;
    public TextButton menuTextButton;

    public TiledMap map;
    public TiledMapRenderer mapRenderer;
    public final String levelName;
    public int mapWidth;
    public int mapHeight;
    public int tilePixelWidth;
    public int tilePixelHeight;
    public int mapPixelWidth;
    public int mapPixelHeight;

    public FlowField flowField;

    public HashMap<Vector2, Actor> vectorActor;

    public boolean isWin;
    public boolean isGameOver;
    public boolean isPaused;
    public float timeElapsed;

    public InputMultiplexer inputMultiplexer;

    public GameScreen(final String levelName, final HitBox hitbox) {

        this.levelName = levelName;
        this.hitbox = hitbox;

        isWin = false;
        isGameOver = false;
        isPaused = false;
        timeElapsed = 0;

        map = new TmxMapLoader().load("maps/levels/" + levelName + ".tmx");

        MapProperties prop = map.getProperties();

        mapWidth = prop.get("width", Integer.class);
        mapHeight = prop.get("height", Integer.class);
        tilePixelWidth = prop.get("tilewidth", Integer.class);
        tilePixelHeight = prop.get("tileheight", Integer.class);

        mapPixelWidth = mapWidth * tilePixelWidth;
        mapPixelHeight = mapHeight * tilePixelHeight;

        gameStage = new Stage(new FitViewport(mapPixelWidth, mapPixelHeight));

        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapRenderer.setView((OrthographicCamera) gameStage.getCamera());

        vectorActor = new HashMap<>();
        flowField = new FlowField(this);

        blocks = new Array<>();
        try {
            loadBlocks();
        } catch (ReflectionException e) {
            e.printStackTrace();
            Gdx.app.exit();
        }

        for (Block block : blocks)
            gameStage.addActor(block);

        boxes = new Array<>();
        try {
            loadBoxes();
        } catch (ReflectionException e) {
            e.printStackTrace();
            Gdx.app.exit();
        }

        for (Box box : boxes)
            gameStage.addActor(box);

//         Create player
        try {
            loadPlayer();
        } catch (ReflectionException e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
        gameStage.addActor(player);

        gameStage.setKeyboardFocus(player);

//         Create bots
        bots = new Array<>();
        try {
            loadBots();
        } catch (ReflectionException e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
        for (Bot bot : bots)
            gameStage.addActor(bot);

//        bot screaming :D
        if (bots.size > 0)
            hitbox.soundManager.play("bot.scream");

//        Play background music
        hitbox.musicManager.setLooping("game", true);
        hitbox.musicManager.play("game");

//        UI for Game status
        uiStage = new Stage(gameStage.getViewport());

        winStage = new Stage(gameStage.getViewport());

        gameOverStage = new Stage(gameStage.getViewport());

//        UI Skin
        uiSkin = new Skin(Gdx.files.internal("font/default.json"));

//        Count Box status
        countBoxLabel = new Label("Box: " + boxes.size, uiSkin, "default");
        countBoxLabel.setPosition(0, gameStage.getHeight() - countBoxLabel.getPrefHeight());
        uiStage.addActor(countBoxLabel);

        //        Count Bot status
        countBotLabel = new Label("Bot: " + bots.size, uiSkin, "default");
        countBotLabel.setPosition(gameStage.getWidth() - countBotLabel.getPrefWidth(),
                gameStage.getHeight() - countBotLabel.getPrefHeight());
        uiStage.addActor(countBotLabel);

//        round label
        roundLabel = new Label("Round #" + Long.parseLong(levelName), uiSkin, "default");
        roundLabel.setPosition(gameStage.getWidth() / 2 - roundLabel.getPrefWidth() / 2,
                gameStage.getHeight() - roundLabel.getPrefHeight());
        uiStage.addActor(roundLabel);

//        Win text Label
        winLabel = new Label("Great job!\nTime score: " + (long) timeElapsed + " s", uiSkin, "default");
        winLabel.setVisible(false);
        winLabel.setAlignment(Align.center);
        winLabel.setPosition(gameStage.getWidth() / 2 - winLabel.getWidth() / 2,
                gameStage.getHeight() / 2 - winLabel.getHeight() / 2);
        winStage.addActor(winLabel);

// I'm done text button
        imDoneTextButton = new TextButton("I'm done!", uiSkin, "default");
        imDoneTextButton.setPosition(gameStage.getWidth() / 4 - imDoneTextButton.getWidth() / 2,
                gameStage.getHeight() / 2 - winLabel.getHeight() - imDoneTextButton.getHeight() / 2);
        imDoneTextButton.setVisible(false);
        imDoneTextButton.setTouchable(Touchable.enabled);
        imDoneTextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // clear time score, need to modify
                hitbox.timeScore.clear();
                hitbox.setScreen(new MenuScreen(hitbox));
            }
        });
        winStage.addActor(imDoneTextButton);

        nextTextButton = new TextButton("Next", uiSkin, "default");
        nextTextButton.setPosition(3 * gameStage.getWidth() / 4 - nextTextButton.getWidth() / 2,
                gameStage.getHeight() / 2 - winLabel.getHeight() - nextTextButton.getHeight() / 2);
        nextTextButton.setVisible(false);
        nextTextButton.setTouchable(Touchable.enabled);
        nextTextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String nextLevel = String.format("%03d", Integer.parseInt(levelName) + 1);
                try {
                    hitbox.setScreen(new GameScreen(nextLevel, hitbox));
                } catch (Exception e) {
                    // clear time score, need to modify
                    hitbox.timeScore.clear();
                    hitbox.setScreen(new MenuScreen(hitbox));
                }
            }
        });
        winStage.addActor(nextTextButton);

//      Create game over label
        gameOverLabel = new Label("Game Over!", uiSkin, "default");
        gameOverLabel.setVisible(false);
        gameOverLabel.setAlignment(Align.center);
        gameOverLabel.setPosition(gameStage.getWidth() / 2 - gameOverLabel.getWidth() / 2,
                gameStage.getHeight() / 2 - gameOverLabel.getHeight() / 2);
        gameOverStage.addActor(gameOverLabel);

//        Create go to menu text button
        menuTextButton = new TextButton("Menu", uiSkin, "default");
        menuTextButton.setPosition(gameStage.getWidth() / 2 - menuTextButton.getWidth() / 2,
                gameStage.getHeight() / 2 - gameOverLabel.getHeight() - menuTextButton.getHeight() / 2);
        menuTextButton.setVisible(false);
        menuTextButton.setTouchable(Touchable.enabled);
        menuTextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // clear time score, need to modify
                hitbox.timeScore.clear();
                hitbox.setScreen(new MenuScreen(hitbox));
            }
        });
        gameOverStage.addActor(menuTextButton);

        inputMultiplexer = new InputMultiplexer(gameStage, uiStage, winStage, gameOverStage);

        //    Bot start chasing player :D !
        flowField.init();
        player.fireTargetChangedEvent();
    }

    public void loadBoxes() throws ReflectionException {
        for (Object blockObject : map.getLayers()
                .get("box_object")
                .getObjects()
                .getByType(ClassReflection.forName("com.badlogic.gdx.maps.objects.RectangleMapObject"))) {
            Rectangle rect = ((RectangleMapObject) blockObject).getRectangle();
            int row = (int) (rect.getX() / rect.getWidth());
            int column = (int) (rect.getY() / rect.getHeight());
            TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) (map.getLayers().get("box_layer")))
                    .getCell(row, column);
            Box box = new Box(cell.getTile().getTextureRegion().getTexture(),
                    rect,
                    this,
                    flowField);
            vectorActor.put(new Vector2(box.getX(), box.getY()), box);
            cell.setTile(null);
            boxes.add(box);
        }
    }

    public void loadBlocks() throws ReflectionException {
        for (Object blockObject : map.getLayers()
                .get("block_object")
                .getObjects()
                .getByType(ClassReflection.forName("com.badlogic.gdx.maps.objects.RectangleMapObject"))) {
            Rectangle rect = ((RectangleMapObject) blockObject).getRectangle();
            int row = (int) (rect.getX() / rect.getWidth());
            int column = (int) (rect.getY() / rect.getHeight());
            TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) (map.getLayers().get("block_layer")))
                    .getCell(row, column);
            Block block = new Block(cell.getTile().getTextureRegion().getTexture(), rect);
            vectorActor.put(new Vector2(block.getX(), block.getY()), block);
            cell.setTile(null);
            blocks.add(block);
        }
    }

    public void loadPlayer() throws ReflectionException {
        for (Object blockObject : map.getLayers()
                .get("player_object")
                .getObjects()
                .getByType(ClassReflection.forName("com.badlogic.gdx.maps.objects.RectangleMapObject"))) {
            Rectangle rect = ((RectangleMapObject) blockObject).getRectangle();
            int row = (int) (rect.getX() / rect.getWidth());
            int column = (int) (rect.getY() / rect.getHeight());
            TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) (map.getLayers().get("player_layer")))
                    .getCell(row, column);

            player = new Player(cell.getTile().getTextureRegion().getTexture(),
                    rect,
                    this, // ContactListener
                    flowField, // TargetListener
                    this); // PushListener

            vectorActor.put(new Vector2(player.getX(), player.getY()), player);
            cell.setTile(null);
        }
    }

    public void loadBots() throws ReflectionException {
        for (Object blockObject : map.getLayers()
                .get("bot_object")
                .getObjects()
                .getByType(ClassReflection.forName("com.badlogic.gdx.maps.objects.RectangleMapObject"))) {
            Rectangle rect = ((RectangleMapObject) blockObject).getRectangle();
            int row = (int) (rect.getX() / rect.getWidth());
            int column = (int) (rect.getY() / rect.getHeight());
            TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) (map.getLayers().get("bot_layer")))
                    .getCell(row, column);

            Bot bot = new Bot(cell.getTile().getTextureRegion().getTexture(),
                    rect,
                    this, // ContactListener
                    flowField);
            vectorActor.put(new Vector2(bot.getX(), bot.getY()), bot);
            cell.setTile(null);
            bots.add(bot);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float delta) {
        if (!isWin && !isGameOver && !isPaused)
            timeElapsed += delta;
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        if (!isPaused && Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = true;
            hitbox.musicManager.pause("game");
            hitbox.setScreen(new PauseScreen(hitbox, this));
        }
        mapRenderer.render();
        gameStage.draw();
        uiStage.draw();
        winStage.draw();
        gameOverStage.draw();
    }

    public void update(float delta) {
        gameStage.act(delta);
        uiStage.act(delta);
        winStage.act(delta);
        gameOverStage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height, true);
        uiStage.getViewport().update(width, height, true);
        winStage.getViewport().update(width, height, true);
        gameOverStage.getViewport().update(width, height, true);
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
        for (Actor actor : gameStage.getActors()) {
            if (actor instanceof Box) {
                Box box = (Box) actor;
                box.texture.dispose();
            } else if (actor instanceof Block) {
                Block block = (Block) actor;
                block.texture.dispose();
            } else if (actor instanceof Bot) {
                Bot bot = (Bot) actor;
                bot.texture.dispose();
            }
        }
        gameStage.dispose();
        map.dispose();
        player.texture.dispose();
        uiStage.dispose();
        winStage.dispose();
        gameOverStage.dispose();
        uiSkin.dispose();
    }

    @Override
    public void beginContact(ContactEvent ce) {
        Actor A = (Actor) ce.getSource(), B = (Actor) ce.getcSource();
        System.out.println("ContactListener");
        System.out.println("****************************");
        System.out.println(A.getClass().getSimpleName() + ": (" + A.getX() + ", " + A.getY() + ")");
        System.out.println(B.getClass().getSimpleName() + ": (" + B.getX() + ", " + B.getY() + ")");
        System.out.println("****************************");
        System.out.println();
    }

    @Override
    public void push(PushEvent pe) {
        Actor playerActor = (Actor) pe.getSource();
        Actor pushedActor = (Actor) pe.getPushedObject();
        System.out.println("PushListener");
        System.out.println("****************************");
        System.out.println(playerActor.getClass().getSimpleName() + ": (" + playerActor.getX() + ", " + playerActor.getY() + ")");
        System.out.println(pushedActor.getClass().getSimpleName() + ": (" + pushedActor.getX() + ", " + pushedActor.getY() + ")");
        System.out.println("****************************");
        System.out.println();
    }
}