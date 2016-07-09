package com.yp3y5akh0v.games.hitbox.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.yp3y5akh0v.games.hitbox.HitBox;
import com.yp3y5akh0v.games.hitbox.handlers.ContactEvent;
import com.yp3y5akh0v.games.hitbox.handlers.ContactListener;
import com.yp3y5akh0v.games.hitbox.screens.FlowField;
import com.yp3y5akh0v.games.hitbox.screens.GameScreen;
import com.yp3y5akh0v.games.hitbox.screens.Node;

import java.util.HashMap;

import static com.yp3y5akh0v.games.hitbox.constans.Constants.*;

public class Bot extends Actor {

    public Texture texture;
    public Rectangle rect;

    public ContactEvent contactEvent;
    public ContactListener contactListener;

    public FlowField flowField;

    public float curDT;
    public int curStatus;
    public int curDirect;

    public float x0Left, x1Left, x0Right, x1Right;
    public float y0Down, y1Down, y0Up, y1Up;

    public Bot(Texture texture, Rectangle rect,
               ContactListener contactListener,
               FlowField flowField) {
        this.texture = texture;
        this.rect = rect;
        this.contactListener = contactListener;
        this.flowField = flowField;
        curDT = 0;
        curStatus = STOP;
        curDirect = UNKNOWN;
        setBounds(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
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
        if (curStatus == STOP) {
            GameScreen gs = getGameScreen();
            HitBox hitbox = gs.hitbox;
            HashMap<Vector2, Actor> vectorActor = gs.vectorActor;
            Node botNode = flowField.mapNode.get(getDiscretePosition());
            switch (botNode.dir) {
                case UP:
                    y0Up = getY();
                    y1Up = y0Up + gs.tilePixelHeight;
                    Vector2 vnUp = new Vector2(getX(), y1Up);
                    if (vectorActor.containsKey(vnUp)) {
                        Actor actor = vectorActor.get(vnUp);
                        fireContactEvent(actor);
                        if (actor instanceof Player) {
                            ((Player) actor).explosion();
                            hitbox.soundManager.play("bot.eat");
                            curDirect = UNKNOWN;
                        } else if (actor instanceof Bot) {
                            curDirect = UNKNOWN;
                        } else if (actor instanceof Box) {
                            curDirect = UNKNOWN;
                        }
                    } else {
                        vectorActor.remove(getDiscretePosition());
                        curDirect = UP;
                        curStatus = MOVE;
                        vectorActor.put(vnUp, this);
                    }
                    break;
                case RIGHT:
                    x0Right = getX();
                    x1Right = x0Right + gs.tilePixelWidth;
                    Vector2 vnRight = new Vector2(x1Right, getY());
                    if (vectorActor.containsKey(vnRight)) {
                        Actor actor = vectorActor.get(vnRight);
                        fireContactEvent(actor);
                        if (actor instanceof Player) {
                            curDirect = UNKNOWN;
                            ((Player) actor).explosion();
                            hitbox.soundManager.play("bot.eat");
                        } else if (actor instanceof Bot) {
                            curDirect = UNKNOWN;
                        } else if (actor instanceof Box) {
                            curDirect = UNKNOWN;
                        }
                    } else {
                        vectorActor.remove(getDiscretePosition());
                        curDirect = RIGHT;
                        curStatus = MOVE;
                        vectorActor.put(vnRight, this);
                    }
                    break;
                case DOWN:
                    y0Down = getY();
                    y1Down = y0Down - gs.tilePixelHeight;
                    Vector2 vnDown = new Vector2(getX(), y1Down);
                    if (vectorActor.containsKey(vnDown)) {
                        Actor actor = vectorActor.get(vnDown);
                        fireContactEvent(actor);
                        if (actor instanceof Player) {
                            curDirect = UNKNOWN;
                            ((Player) actor).explosion();
                            hitbox.soundManager.play("bot.eat");
                        } else if (actor instanceof Bot) {
                            curDirect = UNKNOWN;
                        } else if (actor instanceof Box) {
                            curDirect = UNKNOWN;
                        }
                    } else {
                        vectorActor.remove(getDiscretePosition());
                        curDirect = DOWN;
                        curStatus = MOVE;
                        vectorActor.put(vnDown, this);
                    }
                    break;
                case LEFT:
                    x0Left = getX();
                    x1Left = x0Left - gs.tilePixelWidth;
                    Vector2 vnLeft = new Vector2(x1Left, getY());
                    if (vectorActor.containsKey(vnLeft)) {
                        Actor actor = vectorActor.get(vnLeft);
                        fireContactEvent(actor);
                        if (actor instanceof Player) {
                            curDirect = UNKNOWN;
                            ((Player) actor).explosion();
                            hitbox.soundManager.play("bot.eat");
                        } else if (actor instanceof Bot) {
                            curDirect = UNKNOWN;
                        } else if (actor instanceof Box) {
                            curDirect = UNKNOWN;
                        }
                    } else {
                        vectorActor.remove(getDiscretePosition());
                        curDirect = LEFT;
                        curStatus = MOVE;
                        vectorActor.put(vnLeft, this);
                    }
                    break;
            }
        } else if (curStatus == MOVE) {
            curDT += delta * BOT_DEFAULT_SPEED * TRANSITION_TIME;
            switch (curDirect) {
                case UP:
                    if (curDT < 1) {
                        float curX = getX(), curY = y0Up + (y1Up - y0Up) * curDT;
                        setPosition(curX, curY);
                    } else {
                        setPosition(getX(), y1Up);
                        curDT = 0;
                        curStatus = STOP;
                    }
                    break;
                case RIGHT:
                    if (curDT < 1) {
                        float curX = x0Right + (x1Right - x0Right) * curDT, curY = getY();
                        setPosition(curX, curY);
                    } else {
                        setPosition(x1Right, getY());
                        curDT = 0;
                        curStatus = STOP;
                    }
                    break;
                case DOWN:
                    if (curDT < 1) {
                        float curX = getX(), curY = y0Down + (y1Down - y0Down) * curDT;
                        setPosition(curX, curY);
                    } else {
                        setPosition(getX(), y1Down);
                        curDT = 0;
                        curStatus = STOP;
                    }
                    break;
                case LEFT:
                    if (curDT < 1) {
                        float curX = x0Left + (x1Left - x0Left) * curDT, curY = getY();
                        setPosition(curX, curY);
                    } else {
                        setPosition(x1Left, getY());
                        curDT = 0;
                        curStatus = STOP;
                    }
                    break;
            }
        }
    }

    public void explosion() {
        curStatus = DEATH;
        GameScreen gs = getGameScreen();
        gs.vectorActor.remove(getDiscretePosition());
        gs.bots.removeValue(this, true);
        gs.countBotLabel.setText("Bot: " + gs.bots.size);
        gs.countBotLabel.setPosition(gs.gameStage.getWidth() - gs.countBotLabel.getPrefWidth(),
                gs.gameStage.getHeight() - gs.countBotLabel.getPrefHeight());
        gs.gameStage.getRoot().removeActor(this);
        HitBox hitbox = gs.hitbox;
        hitbox.soundManager.play("bot.death", 0.5f, 1f, 0f);
        if (gs.bots.size == 0) {
            gs.isWin = true;
            gs.winLabel.setText("Great job!\nTime score: " + (long) gs.timeElapsed + " s");
            gs.winLabel.setVisible(true);
            gs.imDoneTextButton.setVisible(true);
            gs.nextTextButton.setVisible(true);
            hitbox.musicManager.stop("game");
            hitbox.soundManager.play("win");
            hitbox.timeScore.add((long) gs.timeElapsed);
        }
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    public GameScreen getGameScreen() {
        return ((GameScreen) contactListener);
    }

    public void fireContactEvent(Actor actor) {
        contactEvent = new ContactEvent(this, actor);
        contactListener.beginContact(contactEvent);
    }

}
