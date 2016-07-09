package com.yp3y5akh0v.games.hitbox.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.yp3y5akh0v.games.hitbox.HitBox;
import com.yp3y5akh0v.games.hitbox.controllers.XBoxGamePad;
import com.yp3y5akh0v.games.hitbox.handlers.*;
import com.yp3y5akh0v.games.hitbox.screens.GameScreen;

import java.util.HashMap;

import static com.yp3y5akh0v.games.hitbox.constans.Constants.*;

public class Player extends Actor implements ControllerListener {

    //    texture for player
    public Texture texture;

    // rectangle for player's bounds
    public Rectangle rect;

    //    contact event occur if player contact with other Actors
    public ContactEvent contactEvent;

    //    contact listener in this case is GameScreen class
    public ContactListener contactListener;

    // target changed event occurs when player moved or if you simple call fire method
    public TargetChangedEvent targetChangedEvent;

    // target change listener in this case is FlowField class
    public TargetChangedListener targetChangedListener;

    // Push listener in this case is GameScreen class
    public PushListener pushListener;

    // push event occurs when player push
    public PushEvent pushEvent;

    public float curDT;
    public int curStatus;
    public int curDirect;

    public float x0Right, x1Right, x0Left, x1Left;
    public float y0Down, y1Down, y0Up, y1Up;

    public Player(Texture texture, Rectangle rect,
                  ContactListener contactListener,
                  TargetChangedListener targetChangedListener,
                  PushListener pushListener) {
        this.texture = texture;
        this.rect = rect;
        this.contactListener = contactListener;
        this.targetChangedListener = targetChangedListener;
        this.pushListener = pushListener;
        curDT = 0;
        curStatus = STOP;
        curDirect = DOWN;
        setBounds(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        Controllers.clearListeners();
        Controllers.addListener(this);
    }

    public HitBox getHitBox() {
        return getGameScreen().hitbox;
    }

    public Vector2 getDiscretePosition() {
        if (curStatus != MOVE)
            return getPosition();
        else {
            Vector2 res = null;
            switch (curDirect) {
                case UP:
                    res = new Vector2(getX(), y1Up);
                    break;
                case RIGHT:
                    res = new Vector2(x1Right, getY());
                    break;
                case DOWN:
                    res = new Vector2(getX(), y1Down);
                    break;
                case LEFT:
                    res = new Vector2(x1Left, getY());
                    break;
            }
            return res;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (curStatus == MOVE) {
            moveScript(delta);
        } else if (curStatus == STOP) {
            GameScreen gs = getGameScreen();
            HashMap<Vector2, Actor> vectorActor = gs.vectorActor;
            if (!gs.isWin) {
                if (Controllers.getControllers().size > 0) {
                    Controller gamePad = Controllers.getControllers().get(0);
                    PovDirection povDirection = gamePad.getPov(0);
                    if (povDirection == XBoxGamePad.DPAD_RIGHT) {
                        moveRight(gs, vectorActor);
                    } else if (povDirection == XBoxGamePad.DPAD_LEFT) {
                        moveLeft(gs, vectorActor);
                    } else if (povDirection == XBoxGamePad.DPAD_DOWN) {
                        moveDown(gs, vectorActor);
                    } else if (povDirection == XBoxGamePad.DPAD_UP) {
                        moveUp(gs, vectorActor);
                    }
                } else {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                        pushActor(gs, vectorActor);
                    } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                        moveRight(gs, vectorActor);
                    } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                        moveLeft(gs, vectorActor);
                    } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                        moveUp(gs, vectorActor);
                    } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                        moveDown(gs, vectorActor);
                    }
                }
            }
        }
    }

    public void moveScript(float delta) {
        curDT += delta * PLAYER_SPEED * TRANSITION_TIME;
        switch (curDirect) {
            case LEFT:
                if (curDT < 1) {
                    float curX = x0Left + (x1Left - x0Left) * curDT;
                    setPosition(curX, getY());

                } else {
                    setPosition(x1Left, getY());
                    curDT = 0;
                    curStatus = STOP;
                    fireTargetChangedEvent();
                }
                break;
            case RIGHT:
                if (curDT < 1) {
                    float curX = x0Right + (x1Right - x0Right) * curDT;
                    setPosition(curX, getY());
                } else {
                    setPosition(x1Right, getY());
                    curDT = 0;
                    curStatus = STOP;
                    fireTargetChangedEvent();
                }
                break;
            case UP:
                if (curDT < 1) {
                    float curY = y0Up + (y1Up - y0Up) * curDT;
                    setPosition(getX(), curY);
                } else {
                    setPosition(getX(), y1Up);
                    curDT = 0;
                    curStatus = STOP;
                    fireTargetChangedEvent();
                }
                break;
            case DOWN:
                if (curDT < 1) {
                    float curY = y0Down + (y1Down - y0Down) * curDT;
                    setPosition(getX(), curY);
                } else {
                    setPosition(getX(), y1Down);
                    curDT = 0;
                    curStatus = STOP;
                    fireTargetChangedEvent();
                }
                break;
        }
    }

    public void pushActor(GameScreen gs, HashMap<Vector2, Actor> vectorActor) {
        curStatus = PUSH;
        switch (curDirect) {
            case LEFT:
                x0Left = getX();
                x1Left = x0Left - gs.tilePixelWidth;
                Vector2 vnLeft = new Vector2(x1Left, getY());
                Actor nLeft = vectorActor.get(vnLeft);
                if (nLeft != null) {
                    firePushEvent(nLeft);
                    if (nLeft instanceof Box) {
                        Box box = (Box) nLeft;
                        if (box.curStatus == STOP) {
                            box.startMoveLeft();
                        }
                    }
                }
                break;
            case RIGHT:
                x0Right = getX();
                x1Right = x0Right + gs.tilePixelWidth;
                Vector2 vnRight = new Vector2(x1Right, getY());
                Actor nRight = vectorActor.get(vnRight);
                if (nRight != null) {
                    firePushEvent(nRight);
                    if (nRight instanceof Box) {
                        Box box = (Box) nRight;
                        if (box.curStatus == STOP) {
                            box.startMoveRight();
                        }
                    }
                }
                break;
            case UP:
                y0Up = getY();
                y1Up = y0Up + gs.tilePixelHeight;
                Vector2 vnUp = new Vector2(getX(), y1Up);
                Actor nUp = vectorActor.get(vnUp);
                if (nUp != null) {
                    firePushEvent(nUp);
                    if (nUp instanceof Box) {
                        Box box = (Box) nUp;
                        if (box.curStatus == STOP) {
                            box.startMoveUp();
                        }
                    }
                }
                break;
            case DOWN:
                y0Down = getY();
                y1Down = y0Down - gs.tilePixelHeight;
                Vector2 vnDown = new Vector2(getX(), y1Down);
                Actor nDown = vectorActor.get(vnDown);
                if (nDown != null) {
                    firePushEvent(nDown);
                    if (nDown instanceof Box) {
                        Box box = (Box) nDown;
                        if (box.curStatus == STOP) {
                            box.startMoveDown();
                        }
                    }
                }
                break;
        }
        getHitBox().soundManager.play("player.push");
        curStatus = STOP;
    }

    public void moveRight(GameScreen gs, HashMap<Vector2, Actor> vectorActor) {
        curDirect = RIGHT;
        x0Right = getX();
        x1Right = x0Right + gs.tilePixelWidth;
        Vector2 vnRight = new Vector2(x1Right, getY());
        Actor nRight = vectorActor.get(vnRight);
        if (nRight != null) {
            fireContactEvent(nRight);
            curStatus = STOP;
        } else {
            vectorActor.remove(getDiscretePosition());
            curStatus = MOVE;
            vectorActor.put(vnRight, this);
            getHitBox().soundManager.play("player.walk");
        }
    }

    public void moveDown(GameScreen gs, HashMap<Vector2, Actor> vectorActor) {
        curDirect = DOWN;
        y0Down = getY();
        y1Down = y0Down - gs.tilePixelHeight;
        Vector2 vnDown = new Vector2(getX(), y1Down);
        Actor nDown = vectorActor.get(vnDown);
        if (nDown != null) {
            fireContactEvent(nDown);
            curStatus = STOP;
        } else {
            vectorActor.remove(getDiscretePosition());
            curStatus = MOVE;
            vectorActor.put(vnDown, this);
            getHitBox().soundManager.play("player.walk");
        }
    }

    public void moveUp(GameScreen gs, HashMap<Vector2, Actor> vectorActor) {
        curStatus = MOVE;
        curDirect = UP;
        y0Up = getY();
        y1Up = y0Up + gs.tilePixelHeight;
        Vector2 vnUp = new Vector2(getX(), y1Up);
        Actor nUp = vectorActor.get(vnUp);
        if (nUp != null) {
            fireContactEvent(nUp);
            curStatus = STOP;
        } else {
            getHitBox().soundManager.play("player.walk");
            vectorActor.remove(getPosition());
            vectorActor.put(vnUp, this);
        }
    }

    public void moveLeft(GameScreen gs, HashMap<Vector2, Actor> vectorActor) {
        curDirect = LEFT;
        x0Left = getX();
        x1Left = x0Left - gs.tilePixelWidth;
        Vector2 vnLeft = new Vector2(x1Left, getY());
        Actor nLeft = vectorActor.get(vnLeft);
        if (nLeft != null) {
            fireContactEvent(nLeft);
            curStatus = STOP;
        } else {
            vectorActor.remove(getDiscretePosition());
            curStatus = MOVE;
            vectorActor.put(vnLeft, this);
            getHitBox().soundManager.play("player.walk");
        }
    }

    public void explosion() {
        curStatus = DEATH;
        GameScreen gs = getGameScreen();
        gs.isGameOver = true;
        gs.gameOverLabel.setVisible(true);
        gs.goToMenuTextButton.setVisible(true);
        gs.vectorActor.remove(getDiscretePosition());
        gs.gameStage.getRoot().removeActor(this);
        fireTargetRemoved();
        HitBox hitBox = getHitBox();
        hitBox.soundManager.play("player.death", 0.2f, 1f, 0f);
        hitBox.musicManager.stop("game");
    }

    public GameScreen getGameScreen() {
        return ((GameScreen) contactListener);
    }

    public void fireContactEvent(Actor actor) {
        contactEvent = new ContactEvent(this, actor);
        contactListener.beginContact(contactEvent);
    }

    public void fireTargetChangedEvent() {
        targetChangedEvent = new TargetChangedEvent(this);
        targetChangedListener.targetPositionChanged(targetChangedEvent);
    }

    public void fireTargetRemoved() {
        targetChangedEvent = new TargetChangedEvent(this);
        targetChangedListener.targetRemoved(targetChangedEvent);
    }

    public void firePushEvent(Actor pushedActor) {
        pushEvent = new PushEvent(this, pushedActor);
        pushListener.push(pushEvent);
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        GameScreen gs = getGameScreen();
        HashMap<Vector2, Actor> vectorActor = gs.vectorActor;
        if (buttonCode == XBoxGamePad.BUTTON_A && curStatus == STOP && !gs.isWin)
            pushActor(gs, vectorActor);
        return true;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
